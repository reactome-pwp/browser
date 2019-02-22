package org.reactome.web.pwp.client.details.tabs.analysis;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.downloads.DownloadPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.Filter;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.FilteringPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.FilterAppliedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.FilterRemovedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.FilterAppliedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.FilterRemovedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.EntitiesFoundPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.InteractorsFoundPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.NotFoundPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.AnalysisResultPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.*;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.*;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.AnalysisSummaryPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ActionSelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ResourceChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ActionSelectedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ResourceChangedHandler;
import org.reactome.web.pwp.model.client.classes.Pathway;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnalysisTabDisplay extends ResizeComposite implements AnalysisTab.Display,
        ResourceChangedHandler, ActionSelectedHandler, ClickHandler,
        PathwaySelectedHandler, PathwayHoveredHandler, PathwayHoveredResetHandler,
        EntitiesPathwaySelectedHandler, InteractorsPathwaySelectedHandler,
        FilterAppliedHandler, EntitiesFoundPanel.Handler, InteractorsFoundPanel.Handler,
        FilterRemovedHandler {

    private AnalysisTab.Presenter presenter;
    private static final NumberFormat formatter = NumberFormat.getFormat( "#,###" );

    private DockLayoutPanel container;
    private DetailsTabTitle title;

    private String token;
    private List<String> resources;
    private List<String> columnNames;
    private Filter filter;

    private AnalysisSummaryPanel summaryPanel;
    private StackLayoutPanel stackPanel;

    private AnalysisResultPanel analysisResultPanel;
    private EntitiesFoundPanel entitiesEntitiesFoundPanel;
    private InteractorsFoundPanel interactorsFoundPanel;
    private NotFoundPanel notFoundPanel;
    private FilteringPanel filteringPanel;
    private DownloadPanel downloadPanel;

    private Button resultsBtn;
    private Button notFoundBtn;
    private Button downloadsBtn;
    private Label notFoundBadge;

    private List<Button> btns = new LinkedList<>();
    private DockLayoutPanel innerTabPanel;
    private DeckLayoutPanel tabContainer;

    public AnalysisTabDisplay() {
        this.title = this.getDetailTabType().getTitle();
        AnalysisTabStyleFactory.ResourceCSS css = AnalysisTabStyleFactory.RESOURCES.css();
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
        this.stackPanel.getElement().getStyle().setBackgroundColor("transparent"); //TODO Add a style in CSS
        this.stackPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);

        this.analysisResultPanel = new AnalysisResultPanel();
        this.analysisResultPanel.addEntitiesPathwaySelectedHandler(this);
        this.analysisResultPanel.addInteractorsPathwaySelectedHandler(this);
        this.analysisResultPanel.addPathwaySelectedHandler(this);
        this.analysisResultPanel.addPathwayHoveredHandler(this);
        this.analysisResultPanel.addPathwayHoveredResetHandler(this);
        this.analysisResultPanel.addFilterRemovedHandler(this);
        this.analysisResultPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.analysisResultPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);

        this.filteringPanel = new FilteringPanel();
        this.filteringPanel.addActionSelectedHandler(this);
        this.filteringPanel.addFilterAppliedHandler(this);
        this.filteringPanel.addFilterAppliedHandler(analysisResultPanel);
        this.stackPanel.add(this.filteringPanel, "Filtering", 0);

        this.entitiesEntitiesFoundPanel = new EntitiesFoundPanel(this);
        this.entitiesEntitiesFoundPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.entitiesEntitiesFoundPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);

        this.interactorsFoundPanel = new InteractorsFoundPanel(this);
        this.interactorsFoundPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.interactorsFoundPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);

        this.notFoundPanel = new NotFoundPanel();
        this.notFoundPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.notFoundPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);

        this.downloadPanel = new DownloadPanel();
        this.downloadPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.downloadPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);


        FlowPanel tabButtonsPanel = new FlowPanel();                // Tab buttons panel
//        tabButtonsPanel.setStyleName(RESOURCES.getCSS().tabButtonsPanel());
        tabButtonsPanel.addStyleName(css.tabButtonsPanel());
        tabButtonsPanel.addStyleName(css.unselectable());
        tabButtonsPanel.add(this.resultsBtn = getTabButton("Results", "Analysis results found", AnalysisTabStyleFactory.RESOURCES.resultsIcon()));
        tabButtonsPanel.add(this.notFoundBtn = getTabButton("Not found", "Identifiers not found", AnalysisTabStyleFactory.RESOURCES.notFoundIcon()));
        tabButtonsPanel.add(this.downloadsBtn = getTabButton("Downloads", "Download the results in various formats", AnalysisTabStyleFactory.RESOURCES.downloadsIcon()));
        this.resultsBtn.addStyleName(css.tabButtonSelected());

        notFoundBadge = new Label();
        notFoundBadge.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().tabBadge());
        notFoundBtn.getElement().getFirstChildElement().appendChild(notFoundBadge.getElement());

        this.tabContainer = new DeckLayoutPanel();                 // Main tab container
        this.tabContainer.add(this.analysisResultPanel);
        this.tabContainer.add(this.notFoundPanel);
        this.tabContainer.add(this.downloadPanel);
        this.tabContainer.add(this.entitiesEntitiesFoundPanel);
        this.tabContainer.add(this.interactorsFoundPanel);

        this.tabContainer.showWidget(0);
        this.tabContainer.setAnimationVertical(true);
        this.tabContainer.setAnimationDuration(400);

        innerTabPanel = new DockLayoutPanel(Style.Unit.EM);              // Vertical tab Panel and buttons container
        innerTabPanel.addWest(tabButtonsPanel, 4.4);
        innerTabPanel.add(this.tabContainer);
        this.stackPanel.add(innerTabPanel, "", 0);

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
//            this.summaryPanel.addOptionSelectedHandler(this);
            this.summaryPanel.addResourceChangeHandler(this);
            this.summaryPanel.addActionSelectedHandler(this);
            this.container.addNorth(summaryPanel, 2.6);

            this.analysisResultPanel.showResult(analysisResult, resource);
            this.container.add(stackPanel);

            AnalysisSummary summary = analysisResult.getSummary();
            boolean speciesComparison = summary.getSpecies()!=null;
            this.notFoundBtn.setEnabled(!speciesComparison);

            this.stackPanel.showWidget(this.innerTabPanel);

            this.token = analysisResult.getSummary().getToken();
            Integer notFound = analysisResult.getIdentifiersNotFound();
            this.notFoundPanel.setAnalysisDetails(this.token, notFound);
            updateTabBadge(notFound);

            this.entitiesEntitiesFoundPanel.setResource(resource);
            this.filteringPanel.setup(analysisResult, resource);

            this.downloadPanel.showDownloadOptions(analysisResult, resource);
        } else {
            this.analysisResultPanel.showResult(analysisResult, resource);
            this.summaryPanel.setResource(resource);
            this.downloadPanel.showDownloadOptions(analysisResult, resource);
        }
    }

    @Override
    public void setSpecies(String species) {
        this.analysisResultPanel.setSpecies(species);
        this.downloadPanel.setSpecies(species);
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
    public void onClick(ClickEvent event) {
        for (Button btn : btns) {
            btn.removeStyleName(AnalysisTabStyleFactory.RESOURCES.css().tabButtonSelected());
        }
        Button btn = (Button) event.getSource();
        btn.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().tabButtonSelected());
        if (btn.equals(this.resultsBtn)) {
            this.tabContainer.showWidget(0);
        } else if(btn.equals(this.notFoundBtn)) {
            this.tabContainer.showWidget(1);
            this.notFoundPanel.showNotFound(token, columnNames);
        } else if(btn.equals(this.downloadsBtn)) {
            this.tabContainer.showWidget(2);
        }
    }

    @Override
    public void onActionSelected(ActionSelectedEvent event) {
        switch (event.getAction()) {
            case FILTERING_ON:
                filteringPanel.refresh();
                stackPanel.showWidget(filteringPanel);
                summaryPanel.showFilteringPanel(true);
                break;
            case FILTERING_OFF:
                stackPanel.showWidget(innerTabPanel);
                summaryPanel.showFilteringPanel(false);
                break;
        }
    }

    @Override
    public void onFilterApplied(FilterAppliedEvent event) {
        this.filter = event.getFilter();
//        Console.info(event.getFilter().toString());
        stackPanel.showWidget(innerTabPanel);
        summaryPanel.showFilteringPanel(false);
        presenter.onFilterChanged(event);
    }

    @Override
    public void onFilterRemoved(FilterRemovedEvent event) {
        presenter.onFilterChanged(new FilterAppliedEvent(filter));
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
//        this.entitiesEntitiesFoundPanel.setResource(event.getResource());
        presenter.onResourceSelected(event);
    }

    @Override
    public void onPathwayFoundEntitiesSelected(EntitiesPathwaySelectedEvent event) {
        tabContainer.showWidget(this.entitiesEntitiesFoundPanel);
        this.entitiesEntitiesFoundPanel.setAnalysisDetails(this.token, event.getPathwaySummary());
        this.entitiesEntitiesFoundPanel.showFoundEntities(this.resources, this.columnNames);
    }

    @Override
    public void onPathwayFoundInteractorsSelected(InteractorsPathwaySelectedEvent event) {
        tabContainer.showWidget(this.interactorsFoundPanel);
        this.interactorsFoundPanel.setAnalysisDetails(this.token, event.getPathwaySummary());
        this.interactorsFoundPanel.showFoundInteractors(this.resources, this.columnNames);
    }


    @Override
    public void onEntitiesFoundPanelClosed() {
        tabContainer.showWidget(this.analysisResultPanel);
    }

    @Override
    public void onInteractorsFoundPanelPanelClosed() {
        tabContainer.showWidget(this.analysisResultPanel);
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

    private Button getTabButton(String text, String tooltip, ImageResource imageResource){
        Image image = new Image(imageResource);
        image.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().undraggable());
        FlowPanel fp = new FlowPanel();
        fp.setTitle(tooltip);
        fp.add(image);
        fp.add(new Label(text));

        SafeHtml safeHtml = SafeHtmlUtils.fromSafeConstant(fp.toString());
        Button btn = new Button(safeHtml, this);
        btn.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().tabButton());
        this.btns.add(btn);
        return btn;
    }

    private void updateTabBadge(Integer notFound) {
        if (notFound != null && notFound > 0) {
            notFoundBadge.setText(formatter.format(notFound));
            notFoundBadge.getElement().getStyle().setDisplay(Style.Display.INITIAL);
        } else {
            notFoundBadge.getElement().getStyle().setDisplay(Style.Display.NONE);
        }
    }
}
