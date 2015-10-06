package org.reactome.web.pwp.client.details.tabs.analysis;

import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisResult;
import org.reactome.web.pwp.client.common.analysis.model.ResourceSummary;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.FoundPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.NotFoundPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.AnalysisResultPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwayHoveredEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwayHoveredResetEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwaySelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.ResultPathwaySelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwayHoveredHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwayHoveredResetHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwaySelectedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.ResultPathwaySelectedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.AnalysisSummaryPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.OptionSelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ResourceChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.OptionSelectedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ResourceChangedHandler;
import org.reactome.web.pwp.model.classes.Pathway;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisTabDisplay extends ResizeComposite implements AnalysisTab.Display, ResourceChangedHandler, OptionSelectedHandler,
        PathwaySelectedHandler, PathwayHoveredHandler, PathwayHoveredResetHandler,
        ResultPathwaySelectedHandler {

    private AnalysisTab.Presenter presenter;

    private DockLayoutPanel container;
    private DetailsTabTitle title;

    private String token;
    private List<String> resources;
    private List<String> columnNames;

    private AnalysisSummaryPanel summaryPanel;
    private StackLayoutPanel stackPanel;

    private AnalysisResultPanel analysisResultPanel;
    private FoundPanel pathwayPanel;
    private NotFoundPanel notFoundPanel;


    public AnalysisTabDisplay() {
        this.title = this.getDetailTabType().getTitle();
        this.container = new DockLayoutPanel(Style.Unit.EM);

        this.stackPanel = new StackLayoutPanel(Style.Unit.EM){
            /**
             * There is a problem with the layout when the data is ready
             * before the animation is finished where the table does not
             * fill the available space. This workaround fixes it
             * NOTE: Better listening to animation finished event
             */
            @Override
            public void showWidget(Widget child) {
                super.showWidget(child);
                (new Timer() {
                    @Override
                    public void run() {
                        forceLayout();
                    }
                }).schedule(getAnimationDuration() + 10);
            }
        };
        this.stackPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.stackPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);

        this.analysisResultPanel = new AnalysisResultPanel();
        this.analysisResultPanel.addResultPathwaySelectedHandler(this);
        this.analysisResultPanel.addPathwaySelectedHandler(this);
        this.analysisResultPanel.addPathwayHoveredHandler(this);
        this.analysisResultPanel.addPathwayHoveredResetHandler(this);
        this.analysisResultPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.analysisResultPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        this.stackPanel.add(this.analysisResultPanel, "Analysis Result", 0);

        this.pathwayPanel = new FoundPanel();
        this.pathwayPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.pathwayPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        this.stackPanel.add(this.pathwayPanel, "Found", 0);

        this.notFoundPanel = new NotFoundPanel();
        this.notFoundPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.notFoundPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        this.stackPanel.add(this.notFoundPanel, "Not found", 0);

        this.container.add(this.stackPanel);
        initWidget(this.container);
        setInitialState();
    }

    public DetailsTabType getDetailTabType() {
        return DetailsTabType.ANALYSIS;
    }

    @Override
    public Widget getTitleContainer() {
        return title;
    }

    @Override
    public void clearSelection() {
        if(analysisResultPanel!=null){
            this.analysisResultPanel.clearSelection();
        }
    }

    @Override
    public void selectPathway(Pathway pathway) {
        if(pathway==null) {
            this.analysisResultPanel.clearSelection();
        } else {
            this.analysisResultPanel.selectPathway(pathway.getDbId());
        }
    }

    @Override
    public void showResult(AnalysisResult analysisResult, String resource) {
        this.refreshTitle(analysisResult.getPathwaysFound());

        if(this.summaryPanel==null || !this.summaryPanel.getToken().equals(analysisResult.getSummary().getToken())){
            this.resources = new LinkedList<>();
            for (ResourceSummary resourceSummary : analysisResult.getResourceSummary()) {
                resources.add(resourceSummary.getResource());
            }
            this.columnNames = analysisResult.getExpression().getColumnNames();

            this.container.clear();
            this.summaryPanel = new AnalysisSummaryPanel(analysisResult);
            this.summaryPanel.setResource(resource);
            this.summaryPanel.addOptionSelectedHandler(this);
            this.summaryPanel.addResourceChangeHandler(this);
            this.container.addNorth(summaryPanel, 2.6);

            this.analysisResultPanel.showResult(analysisResult, resource);
            this.container.add(stackPanel);
            this.stackPanel.showWidget(this.analysisResultPanel);

            this.token = analysisResult.getSummary().getToken();
            Integer notFound = analysisResult.getIdentifiersNotFound();
            this.notFoundPanel.setAnalysisDetails(this.token, notFound);

            this.pathwayPanel.setResource(resource);
        }else{
            this.analysisResultPanel.showResult(analysisResult, resource);
            this.summaryPanel.setResource(resource);
        }
    }

    @Override
    public void setInitialState() {
        this.token = null;
        this.summaryPanel = null;
        this.title.resetCounter();

        this.container.clear();
        this.container.add(getDetailTabType().getInitialStatePanel());
    }

    @Override
    public void setPresenter(AnalysisTab.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingMessage() {
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading analysis results, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.container.clear();
        this.container.add(message);
    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void onOptionSelected(OptionSelectedEvent event) {
        switch (event.getAnalysisInfoType()){
            case PATHWAYS_FOUND:
                this.stackPanel.showWidget(this.analysisResultPanel);
                break;
            case NOT_FOUND:
                this.stackPanel.showWidget(this.notFoundPanel);
                this.notFoundPanel.showNotFound(token, columnNames);
                break;
        }
    }

    @Override
    public void onPathwayHovered(PathwayHoveredEvent event) {
        this.presenter.onPathwayHovered(event.getIdentifier());
    }

    @Override
    public void onPathwayHoveredReset(PathwayHoveredResetEvent event) {
        this.presenter.onPathwayHoveredReset();
    }

    @Override
    public void onPathwaySelected(PathwaySelectedEvent event) {
        this.presenter.onPathwaySelected(event.getIdentifier());
//        this.scrollToSelected();
    }

    @Override
    public void onResourceChanged(ResourceChangedEvent event) {
//        this.summaryPanel.setSelected(AnalysisInfoType.PATHWAYS_FOUND);
//        this.analysisResultPanel.setResource(event.getResource());
//        this.pathwayPanel.setResource(event.getResource());
        presenter.onResourceSelected(event);
    }

    @Override
    public void onPathwayFoundEntitiesSelected(ResultPathwaySelectedEvent event) {
        this.stackPanel.showWidget(this.pathwayPanel);
        Long pathwayId = event.getPathwaySummary().getDbId();
        this.pathwayPanel.setAnalysisDetails(this.token, pathwayId);
        this.pathwayPanel.showFound(this.resources, this.columnNames);
        this.summaryPanel.setDownAll(false);
    }

    private void refreshTitle(Integer foundPathways){
        String found = NumberFormat.getDecimalFormat().format(foundPathways);
        this.title.setCounter(found);
    }

    private void scrollToSelected() {
        if(this.analysisResultPanel!=null){
            this.analysisResultPanel.scrollToSelected();
        }
    }
}
