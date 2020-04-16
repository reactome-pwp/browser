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
 *      1. The datasets and various parameters are submitted to the GSA analysis method
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
    private final static String DEFAULT_ERROR_TITLE = "Oops! An error has occurred";
    private final static String DEFAULT_ERROR_MSG = "Analysis failed, please contact help@reactome.org";
    private final static int ANALYSIS_POLLING_PERIOD = 10000;
    private final static int REPORTS_POLLING_PERIOD = 3000;

    private IconButton nextBtn;
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

    private FlowPanel infoPanel;
    private FlowPanel emailInfoPanel;

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

        infoPanel = new FlowPanel();
        infoPanel.setStyleName(GSAStyleFactory.getStyle().analysisInfoPanel());
        infoPanel.add(new HTML(GSAStyleFactory.RESOURCES.analysisInfo().getText()));
        centered.add(infoPanel);

        emailInfoPanel = new FlowPanel();
        emailInfoPanel.setStyleName(GSAStyleFactory.getStyle().analysisInfoEmailPanel());
        emailInfoPanel.add(new HTML(GSAStyleFactory.RESOURCES.analysisInfoEmail().getText()));
        centered.add(emailInfoPanel);

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
            Double progress = (status.getCompleted() * 100);
            updateStatusPanel(null, "Performing analysis [" + progress.intValue() + "%]", status.getDescription());

        } else if (status.getStatus().equalsIgnoreCase("complete")) {
            isAnalysisCompleted = true;
            updateStatusPanel(null, "Analysis completed", "");
            getResultLinks();

        } else if (status.getStatus().equalsIgnoreCase("failed")) {
            isAnalysisCompleted = true;
            updateErrorPanel(DEFAULT_ERROR_TITLE, DEFAULT_ERROR_MSG, status.getDescription());
        }
        Console.info(status.getStatus() + " - " + status.getCompleted() + " - " + isAnalysisCompleted);
    }

    @Override
    public void onReportsStatusSuccess(Status reportsStatus) {
        if (reportsStatus.getStatus().equalsIgnoreCase("running")) {
            areReportsCompleted = false;
            updateReportsPanel(null);

        } else if (reportsStatus.getStatus().equalsIgnoreCase("complete")) {
            areReportsCompleted = true;
            infoPanel.setVisible(false);
            emailInfoPanel.setVisible(false);
            updateReportsPanel(reportsStatus.getReports());

        } else if (reportsStatus.getStatus().equalsIgnoreCase("failed")) {
            areReportsCompleted = true;
            infoPanel.setVisible(false);
            emailInfoPanel.setVisible(false);
        }
    }

    @Override
    public void onResultLinksSuccess(ResultLinks resultLinks) {
        if (resultLinks == null || resultLinks.getReactomeLinks() == null || resultLinks.getReactomeLinks().isEmpty()) {
            // An error has occurred and no result links are returned.
            updateErrorPanel(DEFAULT_ERROR_TITLE, DEFAULT_ERROR_MSG, "No result links returned");
        } else {
            updateStatusPanel(null, "Retrieving analysis links", resultLinks.getReactomeLinks() + " links retrieved");

            String token = resultLinks.getReactomeLinks().get(0).getToken();
            stage = STAGE.GET_RESULT;

            updateStatusPanel(null, "Retrieving results...", "");

            boolean includeDisease = wizardContext.getParameters().get("include_disease_pathways") != null && Boolean.parseBoolean(wizardContext.getParameters().get("include_disease_pathways"));
            ResultFilter analysisResultFilter = new ResultFilter();
            analysisResultFilter.setIncludeDisease(includeDisease);
            AnalysisClient.getResult(token, analysisResultFilter, AnalysisResultTable.PAGE_SIZE, 1, null, null, new AnalysisHandler.Result() {
                @Override
                public void onAnalysisResult(AnalysisResult result, long time) {
                    updateStatusPanel(GSAStyleFactory.RESOURCES.editIcon(), "Results retrieved", "");
                    updateResultsPanel(resultLinks, result);

                    if (createReports) {
                        areReportsCompleted = false;
                        reportsPanel.setVisible(true);
                        checkReportsStatusUntilCompleted();
                        wizardEventBus.fireEventFromSource(new AnalysisCompletedEvent(result, false, includeDisease), this);
                        nextBtn.setVisible(true);
                    } else {
                        wizardEventBus.fireEventFromSource(new AnalysisCompletedEvent(result, includeDisease), this);
                        Scheduler.get().scheduleDeferred(() -> wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.METHODS), this));
                    }
                }

                @Override
                public void onAnalysisError(AnalysisError error) {
                    updateErrorPanel(DEFAULT_ERROR_TITLE, DEFAULT_ERROR_MSG, error.getReason());
                }

                @Override
                public void onAnalysisServerException(String message) {
                    updateErrorPanel(DEFAULT_ERROR_TITLE, DEFAULT_ERROR_MSG, message);
                }
            });
        }
    }

    @Override
    public void onError(GSAError error) {
        isAnalysisCompleted = true;
        areReportsCompleted = true;
        AnalysisStep.this.gsaToken = null;
        updateErrorPanel(DEFAULT_ERROR_TITLE, error.getTitle(), error.getDetail());
        Console.error(error.getTitle() + " - " + error.getDetail());
    }

    @Override
    public void onException(String msg) {
        isAnalysisCompleted = true;
        areReportsCompleted = true;
        AnalysisStep.this.gsaToken = null;
        updateErrorPanel(DEFAULT_ERROR_TITLE, DEFAULT_ERROR_MSG, msg);
        Console.error(msg);
    }

    private void performAnalysis() {
        stage = STAGE.SUBMISSION;
        nextBtn.setVisible(false);
        String prop = wizardContext.getParameters().getOrDefault("create_reports", "False");
        createReports = Boolean.parseBoolean(prop);

        // If user has provided with an email then show a
        // message explaining he will get an email
        String email = wizardContext.getParameters().get("email");
        emailInfoPanel.setVisible(email != null && !email.trim().isEmpty());
        infoPanel.setVisible(true);

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
        }, ANALYSIS_POLLING_PERIOD);
    }

    private void checkReportsStatusUntilCompleted() {
        stage = STAGE.GET_REPORTS;
        if (gsaToken == null || gsaToken.isEmpty()) return;

        Scheduler.get().scheduleFixedPeriod(() -> {
            if (areReportsCompleted) return false;

            GSAClient.getAnalysisReportsStatus(gsaToken, this);
            return true;
        }, REPORTS_POLLING_PERIOD);
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
        fp.add(errorImage = new Image(GSAStyleFactory.RESOURCES.analysisErrorIcon()));
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
        infoPanel.setVisible(false);
        emailInfoPanel.setVisible(false);
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
                        anchor.setText("\u25B6 " + "Click to visualise the result of the Gene Set Analysis");
                        anchor.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                        boolean includeDisease = wizardContext.getParameters().get("include_disease_pathways") != null && Boolean.parseBoolean(wizardContext.getParameters().get("include_disease_pathways"));
                        anchor.addClickHandler(event -> wizardEventBus.fireEventFromSource(new AnalysisCompletedEvent(result, includeDisease), this));
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
        Image spinnerIcon = new Image(GSAStyleFactory.RESOURCES.spinnerIcon());
        Label title = new Label("Retrieving reports...");
        title.setStyleName(GSAStyleFactory.RESOURCES.getCSS().titleFont());

        FlowPanel flowPanel = reportsPanel;
        if (flowPanel == null) flowPanel = new FlowPanel();

        flowPanel.setStyleName(GSAStyleFactory.getStyle().reportsPanel());
        flowPanel.addStyleName(GSAStyleFactory.getStyle().reportsPanelLine());

        flowPanel.add(spinnerIcon);
        flowPanel.add(title);

        return flowPanel;
    }

    private void updateReportsPanel(List<Report> reports) {
        if (reports != null && !reports.isEmpty()) {
            reportsPanel.clear();
            reportsPanel.removeStyleName(GSAStyleFactory.getStyle().reportsPanelLine());
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
        nextBtn = new IconButton(
                "Start Again",
                GSAStyleFactory.RESOURCES.nextIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Go back to the first step",
                event -> {
                    areReportsCompleted = false;
                    reportsPanel.clear();
                    reportsPanel = getReportsPanel();
                    wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.METHODS), this);
                });
        nextBtn.setEnabled(true);
        nextBtn.setVisible(false);
        addRightButton(nextBtn);

        previousBtn = new IconButton(
                "Back",
                GSAStyleFactory.RESOURCES.previousIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Go back to your data",
                event -> {
                    isAnalysisCompleted = true;
                    areReportsCompleted = false;
                    reportsPanel.clear();
                    reportsPanel = getReportsPanel();
                    wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.OPTIONS), this);
                });
        addLeftButton(previousBtn);
    }

}
