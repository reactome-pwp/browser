package org.reactome.web.pwp.client.tools.analysis.gsa.steps;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.filter.ResultFilter;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.AnalysisResultTable;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.GSAClient;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.GSAClientHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.*;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.handlers.StepSelectedHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.style.GSAStyleFactory;

import java.util.List;

/**
 * The process is divided in the following steps:
 *      1. The datasets are submitted to the GSA analysis method
 *      2. The analysis status is checked periodically using the analysis id returned by step 1
 *      3. When analysis is completed the list of results links (along with the associated analysis token) are requested
 *      4. At last, using the received analysis token the actual results are requested
 *      5. If the user has requested the reports then the ui starts checking whether the reports are completed
 *      6. Upon completion of report generation the different report links are presented in the UI
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@SuppressWarnings("Duplicates")
public class AnalysisStep extends AbstractGSAStep implements StepSelectedHandler,
        GSAClientHandler.GSAAnalysisHandler, GSAClientHandler.GSAStatusHandler, GSAClientHandler.GSAResultLinksHandler,
        GSAClientHandler.GSAReportsStatusHandler {
    private IconButton previousBtn;

    private Widget statusPanel;
    private Image statusImage;
    private Label statusTitle;
    private Label statusDetail;

    private Widget errorPanel;
    private Image errorImage;
    private Label errorTitle;
    private Label errorMessage;
    private Label errorDetail;

    private FlowPanel resultsPanel;
    private FlowPanel reportsPanel;

    private String gsaToken;
    private boolean isAnalysisCompleted;
    private boolean areReportsCompleted;
    private boolean createReports;
    private STAGE stage = STAGE.IDLE;

    private enum STAGE {
        IDLE,
        SUBMISSION,
        PROCESSING,
        GET_LINKS,
        GET_RESULT,
        GET_REPORTS,
        COMPLETED
    }

    public AnalysisStep(GSAWizardEventBus wizardEventBus, GSAWizardContext wizardContext) {
        super(wizardEventBus, wizardContext);
        init();
        initHandlers();
    }

    @Override
    public void onStepSelected(StepSelectedEvent event) {
        if (event.getSource().equals(this) || event.getStep() != GSAStep.ANALYSIS)  {
            return;
        }
        performAnalysis();
    }

    private void init() {
        FlowPanel container = new FlowPanel();
        container.setStyleName(GSAStyleFactory.getStyle().container());

        SimplePanel title = new SimplePanel();
        title.setStyleName(GSAStyleFactory.getStyle().title());
        title.getElement().setInnerHTML("Results");
        container.add(title);

        FlowPanel centered = new FlowPanel();
        centered.setStyleName(GSAStyleFactory.getStyle().centered());
        container.add(centered);

        statusPanel = getStatusPanel();
        statusPanel.setVisible(false);
        centered.add(statusPanel);

        errorPanel = getErrorPanel();
        errorPanel.setVisible(false);
        centered.add(errorPanel);

        resultsPanel = getResultsPanel();
        resultsPanel.setVisible(false);
        centered.add(resultsPanel);

        reportsPanel = getReportsPanel();
        reportsPanel.setVisible(false);
        centered.add(reportsPanel);

        addNavigationButtons();

        add(new ScrollPanel(container));
    }

    private void initHandlers() {
        wizardEventBus.addHandler(StepSelectedEvent.TYPE, this);
    }

    @Override
    public void onAnalysisSubmissionSuccess(String gsaToken) {
        this.gsaToken = gsaToken;
        isAnalysisCompleted = false;
        areReportsCompleted = false;
        updateStatusPanel(null, "Data submission", "Data submitted successfully");
        checkStatusUntilCompleted();
    }

    @Override
    public void onStatusSuccess(Status status) {
        if (status.getStatus().equalsIgnoreCase("running")) {
            isAnalysisCompleted = false;
            updateStatusPanel(null, "Performing analysis [" + status.getCompleted() + "]", status.getDescription());

        } else if (status.getStatus().equalsIgnoreCase("complete")) {
            isAnalysisCompleted = true;
            updateStatusPanel(null, "Analysis completed", "");
            getResultLinks();

        } else if (status.getStatus().equalsIgnoreCase("failed")) {
            isAnalysisCompleted = true;
            updateErrorPanel("Oups! An error has occurred", "Analysis failed", status.getDescription());
        }
        Console.info(status.getStatus() + " - " + status.getCompleted() + " - " + isAnalysisCompleted);
    }

    @Override
    public void onReportsStatusSuccess(Status reportsStatus) {
        if (reportsStatus.getStatus().equalsIgnoreCase("running")) {
            areReportsCompleted = false;

        } else if (reportsStatus.getStatus().equalsIgnoreCase("complete")) {
            areReportsCompleted = true;
            updateReportsPanel(reportsStatus.getReports());

        } else if (reportsStatus.getStatus().equalsIgnoreCase("failed")) {
            areReportsCompleted = true;

        }
    }

    @Override
    public void onResultLinksSuccess(ResultLinks resultLinks) {
        updateStatusPanel(null, "Retrieving analysis links", resultLinks.getReactomeLinks() + " links retrieved");
        resultLinks.getReactomeLinks().forEach(k -> Console.info(k.getUrl() + " - "  + k.getName() + " - "  + k.getToken()));
        String token = resultLinks.getReactomeLinks().get(0).getToken();
        stage = STAGE.GET_RESULT;

        updateStatusPanel(null, "Retrieving results...", "");
        AnalysisClient.getResult(token, new ResultFilter(), AnalysisResultTable.PAGE_SIZE, 1, null, null, new AnalysisHandler.Result() {
            @Override
            public void onAnalysisResult(AnalysisResult result, long time) {
                updateStatusPanel(GSAStyleFactory.RESOURCES.editIcon(), "Results retrieved", "");
                updateResultsPanel(resultLinks, result);

                if (createReports) {
                    areReportsCompleted = false;
                    reportsPanel.setVisible(true);
                    checkReportsStatusUntilCompleted();
                }
//                wizardEventBus.fireEventFromSource(new AnalysisCompletedEvent(result), this);
//                Scheduler.get().scheduleDeferred(() -> {
//                    wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.METHODS), this);
//                });
            }

            @Override
            public void onAnalysisError(AnalysisError error) {

            }

            @Override
            public void onAnalysisServerException(String message) {

            }
        });
    }

    @Override
    public void onError(GSAError error) {
        isAnalysisCompleted = true;
        areReportsCompleted = true;
        AnalysisStep.this.gsaToken = null;
        updateErrorPanel("Oups! An error has occurred", error.getTitle(), error.getDetail());
        Console.error(error.getTitle() + " - " + error.getDetail());
    }

    @Override
    public void onException(String msg) {
        isAnalysisCompleted = true;
        areReportsCompleted = true;
        AnalysisStep.this.gsaToken = null;
        updateErrorPanel("Oups! An error has occurred", msg, "");
        Console.error(msg);
    }

    private void performAnalysis() {
        stage = STAGE.SUBMISSION;
        String prop = wizardContext.getParameters().getOrDefault("create_reports", "False");
        createReports = Boolean.parseBoolean(prop);
        GSAClient.analyse(wizardContext.toJSON(), this);
        updateStatusPanel(GSAStyleFactory.RESOURCES.loaderIcon(), "Data submission", "Submitting data for analysis...");
    }

    private void checkStatusUntilCompleted() {
        stage = STAGE.PROCESSING;
        if (gsaToken == null || gsaToken.isEmpty()) return;

        Scheduler.get().scheduleFixedPeriod(() -> {
            if (isAnalysisCompleted) return false;

            GSAClient.getAnalysisStatus(gsaToken, this);
            return true;
        }, 2000);
    }

    private void checkReportsStatusUntilCompleted() {
        stage = STAGE.GET_REPORTS;
        if (gsaToken == null || gsaToken.isEmpty()) return;

        Scheduler.get().scheduleFixedPeriod(() -> {
            if (areReportsCompleted) return false;

            GSAClient.getAnalysisReportsStatus(gsaToken, this);
            return true;
        }, 2000);
    }

    private void getResultLinks(){
        stage = stage.GET_LINKS;
        updateStatusPanel(null, "Retrieving analysis links", "");
        GSAClient.getAnalysisResultLinks(gsaToken, this);
    }

    private Widget getStatusPanel() {
        FlowPanel fp = new FlowPanel();
        fp.addStyleName(GSAStyleFactory.getStyle().statusPanel());
        fp.add(statusImage = new Image(GSAStyleFactory.RESOURCES.loaderIcon()));
        statusTitle = new Label("Please wait while analysing your data");
        statusTitle.setStyleName(GSAStyleFactory.getStyle().titleFont());
        fp.add(statusTitle);
        fp.add(statusDetail = new Label(""));
        return fp;
    }

    private void updateStatusPanel(ImageResource imageResource, String title, String detail) {
        if (imageResource != null) statusImage.setResource(imageResource);
        if (title != null) statusTitle.setText(title);
        if (detail != null) statusDetail.setText(detail);

        errorPanel.setVisible(false);
        resultsPanel.setVisible(false);
        reportsPanel.setVisible(false);
        statusPanel.setVisible(true);
    }

    private Widget getErrorPanel() {
        FlowPanel fp = new FlowPanel();
        fp.addStyleName(GSAStyleFactory.getStyle().errorPanel());
        fp.add(errorImage = new Image(GSAStyleFactory.RESOURCES.addIcon()));
        errorTitle = new Label("");
        errorTitle.setStyleName(GSAStyleFactory.getStyle().titleFont());
        fp.add(errorTitle);
        fp.add(errorMessage = new Label(""));
        fp.add(errorDetail = new Label(""));
        return fp;
    }

    private void updateErrorPanel(String title, String message, String detail) {
        if (title != null) errorTitle.setText(title);
        if (message != null) errorMessage.setText(message);
        if (detail != null) errorDetail.setText(detail);

        statusPanel.setVisible(false);
        resultsPanel.setVisible(false);
        reportsPanel.setVisible(false);
        errorPanel.setVisible(true);
    }

    private FlowPanel getResultsPanel() {
        FlowPanel fp = new FlowPanel();
        fp.addStyleName(GSAStyleFactory.getStyle().resultsPanel());
        return fp;
    }

    private void updateResultsPanel(ResultLinks resultLinks, AnalysisResult result) {
        if (resultsPanel != null && resultLinks != null) {
            resultsPanel.clear();
            List<Link> links = resultLinks.getReactomeLinks();
            if (links!=null) {
                for (Link link : links) {
                    Anchor anchor = new Anchor();
                    anchor.setText("\u25B6 " + link.getName());
                    anchor.setName(link.getName());
                    if (link.getName().equalsIgnoreCase("Gene Set Analysis Summary")) {
                        anchor.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                        anchor.addClickHandler(event -> wizardEventBus.fireEventFromSource(new AnalysisCompletedEvent(result), this));
                    } else {
                        anchor.setHref(link.getUrl());
                        anchor.setTarget("_blank");
                    }
                    resultsPanel.add(anchor);
                }
            }
        }

        statusPanel.setVisible(false);
        errorPanel.setVisible(false);
        resultsPanel.setVisible(true);
        reportsPanel.setVisible(false);
    }

    private FlowPanel getReportsPanel() {
        FlowPanel fp = new FlowPanel();
        fp.addStyleName(GSAStyleFactory.getStyle().reportsPanel());
        fp.add(new Image(GSAStyleFactory.RESOURCES.previousIcon()));
        fp.add(new Label("\u25B6 Retrieving reports..."));
        return fp;
    }

    private void updateReportsPanel(List<Report> reports) {
        if (reports != null && !reports.isEmpty()) {
            reportsPanel.clear();
            Label title = new Label("\u25B6 Available reports:");
            title.setStyleName(GSAStyleFactory.getStyle().titleFont());
            reportsPanel.add(title);

            for (Report link : reports) {
                Anchor anchor = new Anchor();
                anchor.setText("\u2022 " +  link.getName());
                anchor.setHref(link.getUrl());
                anchor.setTarget("_blank");
                reportsPanel.add(anchor);
            }
            statusPanel.setVisible(false);
            errorPanel.setVisible(false);
            resultsPanel.setVisible(true);
            reportsPanel.setVisible(true);
        }
    }

    private void addNavigationButtons() {
        previousBtn = new IconButton(
                "Back",
                GSAStyleFactory.RESOURCES.previousIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Go back to your data",
                event -> {
                    isAnalysisCompleted = true;
                    areReportsCompleted = true;
                    wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.OPTIONS), this);
                });
        addLeftButton(previousBtn);
    }

}
