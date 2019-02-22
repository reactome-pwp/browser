package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;


import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.Bin;
import org.reactome.web.analysis.client.model.SpeciesSummary;
import org.reactome.web.diagram.common.IconButton;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.FilterAppliedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.FilterAppliedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.pvalue.SimpleSlider;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.RangeSlider;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.RangeValueChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.RangeValueChangedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species.Species;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species.SpeciesCell;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ActionSelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ActionSelectedHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A panel containing all the different
 * filtering options (by size, species etc)
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FilteringPanel extends LayoutPanel implements RangeValueChangedHandler, ClickHandler,
        ValueUpdater<Species>, ValueChangeHandler<Boolean> {
    private static NumberFormat formatter = NumberFormat.getFormat("0.00");

    private FlowPanel bySizePanel;
    private FlowPanel bySpeciesPanel;
    private FlowPanel byVariousPanel;

    private RangeSlider sizeSlider;
    private SimpleSlider pValueSlider;

    private Label sizeFilterLb;
    private Label pValueFilterLb;
    private Button cancelBtn;
    private Button removeBtn;
    private Button applyBtn;

    private final static int binSize = 200;
    private final static int histWidth = 300;
    private final static int histHeight = 90;
    private double filterMin;
    private double filterMax;
    private int min;
    private int max;
    private double pValue;
    private List<Integer> histogram;

    private CellList<Species> speciesListBox;
    private List<Species> speciesList;

    private CheckBox includeDiseaseCB;
    private boolean includeDisease;

    private String token;
    private String resource;
    private Filter filter;

    public FilteringPanel() {
        filter = new Filter();
    }

    public HandlerRegistration addActionSelectedHandler(ActionSelectedHandler handler) {
        return this.addHandler(handler, ActionSelectedEvent.TYPE);
    }

    public HandlerRegistration addFilterAppliedHandler(FilterAppliedHandler handler) {
        return this.addHandler(handler, FilterAppliedEvent.TYPE);
    }

    public void setup(AnalysisResult analysisResult, String resource) {
        this.token = analysisResult.getSummary().getToken();
        this.resource = resource;
        this.includeDisease = true;
        this.pValue = 1d;

        retrieveSpecies(analysisResult);
        initUI();

        retrieveHistogram();
    }

    @Override
    public void onRangeValueChanged(RangeValueChangedEvent event) {
        if (event.getSource() instanceof RangeSlider) { // Size Filter
            filterMin = event.getMin();
            filterMax = event.getMax();
            sizeFilterLb.setText((int) filterMin + " - " + (int) filterMax + " entities");

            if (filterMin != min || filterMax != max) {
                filter.setSize((int) filterMin, (int) filterMax);
            } else {
                filter.removeFilter(Filter.Type.BY_SIZE);
            }

        } else {    // p-Value filter
            pValue = event.getMax();
            String rounded = formatter.format(pValue);
            pValueFilterLb.setText("p ≤ " + rounded);
            if (pValue != 1d) {
                filter.setPValue(Double.parseDouble(rounded));
            } else {
                filter.removeFilter(Filter.Type.BY_PVALUE);
            }
        }
        updateApplyButton();
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        CheckBox cBox = (CheckBox) event.getSource();
        if (cBox.equals(includeDiseaseCB)) {
            includeDisease = includeDiseaseCB.getValue();
            if (!includeDisease) {
                filter.setDisease(includeDisease);
            } else {
                filter.removeFilter(Filter.Type.BY_DISEASE);
            }
        }
        updateApplyButton();
    }

    @Override
    public void onClick(ClickEvent event) {
        Button btn = (Button) event.getSource();
        if (btn.equals(applyBtn)) {
            fireEvent(new FilterAppliedEvent(filter));
        } else if (btn.equals(cancelBtn)) {
            fireEvent(new ActionSelectedEvent(ActionSelectedEvent.Action.FILTERING_OFF));
        } else if(btn.equals(removeBtn)) {
            filter.removeAll();
            fireEvent(new FilterAppliedEvent(filter));
        }
    }

    @Override
    public void update(Species value) {
        List<Species> previouslySelectedSpecies = speciesList.stream().filter(Species::isChecked).collect(Collectors.toList());
        if(value != null) {
            value.setChecked(!value.isChecked());
        }
        List<Species> selectedSpecies = speciesList.stream().filter(Species::isChecked).collect(Collectors.toList());

        if (previouslySelectedSpecies.size() == speciesList.size() && selectedSpecies.size() == previouslySelectedSpecies.size() - 1) {
            //  Deselect everything else but the one clicked
            speciesList.forEach(species -> {
                if(!species.getId().equals(value.getId())) {
                    species.setChecked(false);
                } else {
                    species.setChecked(true);
                }
            });
            selectedSpecies = speciesList.stream().filter(Species::isChecked).collect(Collectors.toList());
            speciesListBox.setRowData(speciesList);
            filter.setSpecies(selectedSpecies);
        } else if (selectedSpecies.size() == speciesList.size()) {
            //  All of the species are selected so the filter should be removed
            filter.removeFilter(Filter.Type.BY_SPECIES);
        } else if (selectedSpecies.size() == 0) {
            //  At least one of the species has to be selected, thus if nothing is selected we select all of them and
            // remove
            speciesList.forEach(species -> species.setChecked(true));
            speciesListBox.setRowData(speciesList);
            filter.removeFilter(Filter.Type.BY_SPECIES);
        } else {
            filter.setSpecies(selectedSpecies);
        }

        retrieveHistogram();
        updateApplyButton();
    }

    private void initUI() {
        clear();
        FlowPanel main = new FlowPanel();
        main.setStyleName(RESOURCES.getCSS().main());

        Label mainTitle = new Label("Filter your analysis results");
        mainTitle.setStyleName(RESOURCES.getCSS().mainTitle());

        FlowPanel flexContainer = new FlowPanel();
        flexContainer.setStyleName(RESOURCES.getCSS().flexContainer());
        flexContainer.add(getBySize());
        flexContainer.add(getBySpecies());
        flexContainer.add(getByVarious());

        main.add(mainTitle);
        main.add(flexContainer);
        add(main);

        updateApplyButton();
    }

    private Widget getBySize() {
        bySizePanel = new FlowPanel();
        bySizePanel.setStyleName(RESOURCES.getCSS().bySizePanel());

        sizeFilterLb = new Label();
        sizeFilterLb.setStyleName(RESOURCES.getCSS().sizeFilterLb());
        bySizePanel.add(sizeFilterLb);

        Label title = new Label("\u2022 By pathway size");
        title.setStyleName(RESOURCES.getCSS().innerTitle());
        bySizePanel.add(title);

        Label subtitle = new Label("Show pathways with size:");
        subtitle.setStyleName(RESOURCES.getCSS().subtitle());
        bySizePanel.add(subtitle);

        return bySizePanel;
    }

    private void addSizeWidget() {
        if (sizeSlider != null) sizeSlider.removeFromParent();

        min = 0;
        max = histogram.size() * binSize;
        if (!filter.getAppliedFilters().contains(Filter.Type.BY_SIZE)) {
            filterMin = min;
            filterMax = max;
        } else {
            filterMin = filter.getSizeMin() < min || filter.getSizeMin() > max ? min : filter.getSizeMin();
            filterMax = filter.getSizeMax() > max ? max : filter.getSizeMax();

            //Remove filter if necessary or update the filter with the new values
            if (filterMin == min && filterMax == max) {
                filter.removeFilter(Filter.Type.BY_SIZE);
            } else {
                filter.setSize((int) filterMin, (int) filterMax);
            }
        }

        sizeSlider = new RangeSlider(histWidth, histHeight, min, max, filterMin, filterMax, histogram);
        sizeSlider.addRangeValueChangedHandler(this);
        bySizePanel.add(sizeSlider);
        sizeFilterLb.setText(filterMin == min && filterMax == max ? "All entities" : (int) filterMin + " - " + (int) filterMax + " entities");
    }

    private Widget getBySpecies() {
        bySpeciesPanel = new FlowPanel();
        bySpeciesPanel.setStyleName(RESOURCES.getCSS().bySpeciesPanel());

        Label title = new Label("\u2022 By species");
        title.setStyleName(RESOURCES.getCSS().innerTitle());
        bySpeciesPanel.add(title);

        Label subtitle = new Label("Show pathways belonging to he following species:");
        subtitle.setStyleName(RESOURCES.getCSS().subtitle());
        bySpeciesPanel.add(subtitle);

        SpeciesCell speciesCell = new SpeciesCell();
        speciesListBox = new CellList<>(speciesCell);
        speciesListBox.setStyleName(RESOURCES.getCSS().speciesListBox());
        speciesListBox.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);
        speciesListBox.setRowCount(speciesList.size(), true);
        speciesListBox.setRowData(speciesList);
        speciesListBox.setValueUpdater(this);

        Button selectAllBtn = new IconButton("all", RESOURCES.selectAllIcon(), event -> {
            speciesList.forEach(s -> s.setChecked(true));
            speciesListBox.setRowData(speciesList);
            update(null);
        });
        selectAllBtn.setStyleName(RESOURCES.getCSS().selectBtn());
        selectAllBtn.setTitle("Select all species");

        FlowPanel listButtonsPanel = new FlowPanel();
        listButtonsPanel.setStyleName(RESOURCES.getCSS().listButtonsPanel());
        listButtonsPanel.add(selectAllBtn);
        FlowPanel listPanel = new FlowPanel();
        listPanel.setStyleName(RESOURCES.getCSS().listPanel());
        listPanel.add(speciesListBox);
        listPanel.add(listButtonsPanel);
        bySpeciesPanel.add(listPanel);

        return bySpeciesPanel;
    }

    private Widget getByVarious() {
        byVariousPanel = new FlowPanel();
        byVariousPanel.setStyleName(RESOURCES.getCSS().byVariousPanel());

        byVariousPanel.add(getByPValue());
        byVariousPanel.add(getByDisease());

        cancelBtn = new IconButton("Cancel", RESOURCES.cancelIcon());
        cancelBtn.setStyleName(RESOURCES.getCSS().applyBtn());
        cancelBtn.addClickHandler(this);
        cancelBtn.setTitle("Cancel filtering");

        removeBtn = new IconButton("Remove", RESOURCES.removeIcon());
        removeBtn.setStyleName(RESOURCES.getCSS().applyBtn());
        removeBtn.addClickHandler(this);
        removeBtn.setTitle("Remove all filters");
        removeBtn.setVisible(false);

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

    private Widget getByDisease() {
        FlowPanel byDiseasePanel = new FlowPanel();

        Label title = new Label("\u2022 By disease");
        title.setStyleName(RESOURCES.getCSS().compactInnerTitle());
        byDiseasePanel.add(title);

        Label subtitle = new Label("Show/hide disease pathways");
        subtitle.setStyleName(RESOURCES.getCSS().compactSubtitle());
        byDiseasePanel.add(subtitle);

        includeDiseaseCB = new CheckBox("Include disease pathway in the results");
        includeDiseaseCB.setStyleName(RESOURCES.getCSS().checkBox());
        includeDiseaseCB.setValue(Boolean.TRUE);
        includeDiseaseCB.addValueChangeHandler(this);
        byDiseasePanel.add(includeDiseaseCB);

        return byDiseasePanel;
    }

    private Widget getByPValue() {
        FlowPanel byPValuePanel = new FlowPanel();
        byPValuePanel.setStyleName(RESOURCES.getCSS().byPValuePanel());

        pValueFilterLb = new Label("p ≤ 1.00");
        pValueFilterLb.setStyleName(RESOURCES.getCSS().pValueFilterLb());
        byPValuePanel.add(pValueFilterLb);

        Label title = new Label("\u2022 By p-value");
        title.setStyleName(RESOURCES.getCSS().innerTitle());
        byPValuePanel.add(title);

        Label subtitle = new Label("Filter by statistical significance:");
        subtitle.setStyleName(RESOURCES.getCSS().compactSubtitle());
        byPValuePanel.add(subtitle);

        pValueSlider = new SimpleSlider(280, 27, 0, 1, 1);
        pValueSlider.addRangeValueChangedHandler(this);
        byPValuePanel.add(pValueSlider);

        return byPValuePanel;
    }

    private void updateApplyButton() {
        applyBtn.setEnabled(filter.isActive());
    }

    public void refresh() {
        List<Species> filterSpecies = filter.getSpecies();
        if (filterSpecies.size() == 0) {
            speciesList.forEach(s -> s.setChecked(true));
            speciesListBox.setRowData(speciesList);
            update(null);
        } else {
            speciesList.forEach(s -> s.setChecked(filterSpecies.contains(s)));
            speciesListBox.setRowData(speciesList);
            update(null);
        }

        includeDiseaseCB.setValue(filter.isIncludeDisease());

        if (filter.getAppliedFilters().contains(Filter.Type.BY_PVALUE)) {
            pValueSlider.setValue(filter.getpValue());
        } else {
            pValueSlider.setValue(1d);
        }

        removeBtn.setVisible(filter.isActive());

    }

    private void retrieveHistogram() {
        this.histogram = new ArrayList<>();

        AnalysisClient.getPathwaysBinnedBySize(token, resource, binSize, 1d, filter != null && filter.getSpecies().size() > 0? filter.getSpecies().stream().map(s -> s.getSpeciesSummary().getTaxId()).collect(Collectors.toList()) : null, new AnalysisHandler.PathwaysBinned() {
            @Override
            public void onPathwaysBinnedLoaded(List<Bin> pathwaysBinned) {
                if (pathwaysBinned != null && !pathwaysBinned.isEmpty()) {
                    Bin lastBin = pathwaysBinned.get(pathwaysBinned.size() - 1);
                    Integer[] binValues = new Integer[lastBin.getKey() + 1];
                    for (Bin bin : pathwaysBinned) {
                        binValues[bin.getKey()] =  bin.getValue();
                    }
                    histogram = Arrays.stream(binValues).map(i -> i == null ? 0 : i).collect(Collectors.toList());

                    addSizeWidget();
                }
            }

            @Override
            public void onPathwaysBinnedError(AnalysisError error) {
                Console.error(error.getReason());
            }

            @Override
            public void onAnalysisServerException(String message) {
                Console.error(message);
            }
        });
    }

    private void retrieveSpecies(AnalysisResult analysisResult) {
        this.speciesList = new ArrayList<>();
        if (analysisResult.getSpeciesSummary() != null) {
            for (SpeciesSummary speciesSummary : analysisResult.getSpeciesSummary()) {
                this.speciesList.add(new Species(speciesSummary));
            }
        }
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

        String bySizePanel();

        String bySpeciesPanel();

        String byVariousPanel();

        String byPValuePanel();

        String innerTitle();

        String subtitle();

        String compactInnerTitle();

        String compactSubtitle();

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
