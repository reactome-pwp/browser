package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.diagram.common.IconButton;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.FilterAppliedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.FilterAppliedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species.Species;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ActionSelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ActionSelectedHandler;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A panel containing all the different
 * filtering options (by size, species etc)
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FilteringPanel extends LayoutPanel implements FilteringWidget.Handler, ClickHandler {
    private FlowPanel firstColumnPanel;
    private FlowPanel byVariousPanel;

    private FilteringWidget resourceFiltering;
    private FilteringWidget sizeFiltering;
    private FilteringWidget speciesFiltering;
    private FilteringWidget pValueFiltering;
    private FilteringWidget diseaseFiltering;

    private Button cancelBtn;
    private Button removeBtn;
    private Button applyBtn;

    private String token;
    private AnalysisResult analysisResult;
    private Filter initialFilter;
    private Filter newFilter;

    public FilteringPanel() {
        resourceFiltering = new ResourceFiltering(this);
        sizeFiltering = new SizeFiltering(this);
        speciesFiltering = new SpeciesFiltering(this);
        pValueFiltering = new PValueFiltering(this);
        diseaseFiltering = new DiseaseFiltering(this);
    }

    public HandlerRegistration addActionSelectedHandler(ActionSelectedHandler handler) {
        return this.addHandler(handler, ActionSelectedEvent.TYPE);
    }

    public HandlerRegistration addFilterAppliedHandler(FilterAppliedHandler handler) {
        return this.addHandler(handler, FilterAppliedEvent.TYPE);
    }

    public void setup(AnalysisResult analysisResult, Filter filter) {
        this.analysisResult = analysisResult;
        token = analysisResult.getSummary().getToken();
        initialFilter = filter;
        newFilter = initialFilter.clone();

        initUI();
    }

    public void setFilter(Filter filter) {
        initialFilter = filter;
        newFilter = initialFilter.clone();

        loadAnalysisData();
    }

    @Override
    public Filter getFilter() {
        return newFilter;
    }

    @Override
    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    @Override
    public void onResourceChanged(String resource) {
        newFilter.setResource(resource);
        updateApplyButton();
    }

    @Override
    public void onSizeChanged(int min, int max, int filterMin, int filterMax) {
        //Remove filter if necessary or update the filter with the new values
        if (filterMin == min && filterMax == max) {
            newFilter.removeFilter(Filter.Type.BY_SIZE);
        } else {
            newFilter.setSize(filterMin, filterMax);
        }

        updateApplyButton();
    }

    @Override
    public void onSpeciesChanged(List<Species> speciesList) {
        newFilter.setSpecies(speciesList.stream().map(s -> s.getSpeciesSummary().getTaxId() + "").collect(Collectors.toList()));
        updateApplyButton();
    }

    @Override
    public void onPValueChanged(double pValue) {
        if (pValue <= 1 || pValue >= 0) {
            newFilter.setPValue(pValue);
        } else {
            newFilter.removeFilter(Filter.Type.BY_PVALUE);
        }
        updateApplyButton();
    }

    @Override
    public void onIncludeDiseaseChanged(boolean includeDisease) {
        newFilter.setDisease(includeDisease);
        updateApplyButton();
    }

    @Override
    public void onClick(ClickEvent event) {
        Button btn = (Button) event.getSource();
        if (btn.equals(applyBtn)) {
            fireEvent(new FilterAppliedEvent(newFilter));
        } else if (btn.equals(cancelBtn)) {
            fireEvent(new ActionSelectedEvent(ActionSelectedEvent.Action.FILTERING_OFF));
        } else if(btn.equals(removeBtn)) {
            newFilter.removeAll();
            fireEvent(new FilterAppliedEvent(newFilter));
        }
    }

    private void initUI() {
        clear();
        FlowPanel main = new FlowPanel();
        main.setStyleName(RESOURCES.getCSS().main());

        Label mainTitle = new Label("Filter your analysis results");
        mainTitle.setStyleName(RESOURCES.getCSS().mainTitle());

        FlowPanel flexContainer = new FlowPanel();
        flexContainer.setStyleName(RESOURCES.getCSS().flexContainer());
        flexContainer.add(getFirstColumnPanel());

        flexContainer.add(speciesFiltering.initUI());
        flexContainer.add(getByVarious());

        main.add(mainTitle);
        main.add(flexContainer);
        add(main);

        updateApplyButton();
    }

    private Widget getFirstColumnPanel() {
        firstColumnPanel = new FlowPanel();
        firstColumnPanel.setStyleName(RESOURCES.getCSS().firstColumnPanel());

        firstColumnPanel.add(resourceFiltering.initUI());
        firstColumnPanel.add(sizeFiltering.initUI());

        return firstColumnPanel;
    }

    private Widget getByVarious() {
        byVariousPanel = new FlowPanel();
        byVariousPanel.setStyleName(RESOURCES.getCSS().byVariousPanel());

        byVariousPanel.add(pValueFiltering.initUI());
        byVariousPanel.add(diseaseFiltering.initUI());

        cancelBtn = new IconButton("Cancel", RESOURCES.cancelIcon());
        cancelBtn.setStyleName(RESOURCES.getCSS().applyBtn());
        cancelBtn.addClickHandler(this);
        cancelBtn.setTitle("Cancel filtering");

        removeBtn = new IconButton("Remove", RESOURCES.removeIcon());
        removeBtn.setStyleName(RESOURCES.getCSS().applyBtn());
        removeBtn.addClickHandler(this);
        removeBtn.setTitle("Remove all filters");
//        removeBtn.setVisible(false);

        applyBtn = new IconButton("Apply", RESOURCES.applyIcon());
        applyBtn.setStyleName(RESOURCES.getCSS().applyBtn());
        applyBtn.addClickHandler(this);
        applyBtn.setTitle("Apply the selected filters");

        FlowPanel buttonsPanel = new FlowPanel();
        buttonsPanel.setStyleName(RESOURCES.getCSS().buttonsPanel());
        buttonsPanel.add(cancelBtn);
        buttonsPanel.add(removeBtn);
        buttonsPanel.add(applyBtn);
        byVariousPanel.add(buttonsPanel);

        return byVariousPanel;
    }

    private void updateFilteringWidgets() {
        diseaseFiltering.updateUI();
        resourceFiltering.updateUI();
        pValueFiltering.updateUI();
        sizeFiltering.updateUI();
        speciesFiltering.updateUI();
    }

    private void updateApplyButton() {
        applyBtn.setEnabled(!newFilter.equals(initialFilter));
    }

    @Override
    public void loadAnalysisData(){
        AnalysisClient.getResult(token, newFilter.getResultFilter(), 0, 0,null, null, new AnalysisHandler.Result() {
            @Override
            public void onAnalysisResult(final AnalysisResult result, long time) {
                Long speciesId = result.getSummary().getSpecies();
                if (speciesId != null) {
                    ContentClient.query(result.getSummary().getSpecies(), new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
                        @Override
                        public void onObjectLoaded(DatabaseObject databaseObject) {
                            result.getSummary().setSpeciesName(databaseObject.getDisplayName());
                            analysisResult = result;
                            updateFilteringWidgets();
                        }

                        @Override
                        public void onContentClientException(Type type, String message) {

                        }

                        @Override
                        public void onContentClientError(ContentClientError error) {

                        }
                    });
                } else {
                    analysisResult = result;
                    updateFilteringWidgets();
                }
            }

            @Override
            public void onAnalysisError(AnalysisError error) {

            }

            @Override
            public void onAnalysisServerException(String message) {

            }
        });
    }

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {

        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("../images/ok.png")
        ImageResource applyIcon();

        @Source("../images/cancel.png")
        ImageResource cancelIcon();

        @Source("../images/remove_filter.png")
        ImageResource removeIcon();

        @Source("../images/select_all.png")
        ImageResource selectAllIcon();

    }

    @CssResource.ImportedWithPrefix("pwp-FilteringPanel")
    public interface ResourceCSS extends CssResource {
        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/filtering/FilteringPanel.css";

        String main();

        String mainTitle();

        String flexContainer();

        String firstColumnPanel();

        String bySpeciesPanel();

        String byVariousPanel();

        String byPValuePanel();

        String innerTitle();

        String subtitle();

        String compactInnerTitle();

        String compactSubtitle();

        String resourcePanel();

        String sizeFilterLb();

        String pValueFilterLb();

        String listPanel();

        String selectBtn();

        String speciesListBox();

        String listButtonsPanel();

        String checkBox();

        String buttonsPanel();

        String applyBtn();

    }

    private static CellListResource CUSTOM_STYLE;
    static {
        CUSTOM_STYLE = GWT.create(CellListResource.class);
        CUSTOM_STYLE.cellListStyle().ensureInjected();
    }

    public interface CellListResource extends CellList.Resources {

        @CssResource.ImportedWithPrefix("pwp-FilteringPanel-CellListResource")
        interface CustomCellList extends CellList.Style {
            String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/filtering/CustomCellList.css";
        }

        /**
         * The styles used in this widget.
         */
        @Override
        @Source(CustomCellList.CSS)
        CustomCellList cellListStyle();
    }
}
