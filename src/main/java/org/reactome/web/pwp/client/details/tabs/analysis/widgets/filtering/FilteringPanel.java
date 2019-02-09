package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;


import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.diagram.common.IconButton;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.FilterAppliedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.FilterAppliedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.RangeSlider;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.RangeValueChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.RangeValueChangedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species.Species;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species.SpeciesCell;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ActionSelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ActionSelectedHandler;

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

    private FlowPanel bySizePanel;
    private FlowPanel bySpeciesPanel;
    private FlowPanel byDiseasePanel;

    private Label sizeFilterLb;
    private Button cancelBtn;
    private Button applyBtn;

    private final static int histWidth = 300;
    private final static int histHeight = 90;
    private double filterMin;
    private double filterMax;
    private int min;
    private int max;
    private List<Integer> histogram;

    private CellList<Species> speciesListBox;
    private List<Species> speciesList;

    private CheckBox includeDisease;
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

    public void setup() {
        min = 1;
        max = 200;
        filterMin = min;
        filterMax = max;

        retrieveHistogram();
        retrieveSpecies();
        initUI();
    }

    @Override
    public void onRangeValueChanged(RangeValueChangedEvent event) {
        filterMin = event.getMin();
        filterMax = event.getMax();
        sizeFilterLb.setText("" + (int) filterMin + " - " + (int) filterMax + " entities");

        if (filterMin != min || filterMax != max) {
            filter.setSize((int) filterMin, (int) filterMax);
        } else {
            filter.removeFilter(Filter.Type.BY_SIZE);
        }
        updateButtons();
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        CheckBox cBox = (CheckBox) event.getSource();
        if (cBox.equals(includeDisease)) {
            if (!includeDisease.getValue()) {
                filter.setDisease(includeDisease.getValue());
            } else {
                filter.removeFilter(Filter.Type.BY_DISEASE);
            }
        }
        updateButtons();
    }

    @Override
    public void onClick(ClickEvent event) {
        Button btn = (Button) event.getSource();
        if (btn.equals(applyBtn)) {
            fireEvent(new FilterAppliedEvent(filter));
        } else if (btn.equals(cancelBtn)) {
            fireEvent(new ActionSelectedEvent(ActionSelectedEvent.Action.FILTERING_OFF));
        }
    }

    @Override
    public void update(Species value) {
        List<Species> selectedSpecies = speciesList.stream().filter(Species::isChecked).collect(Collectors.toList());

        if (selectedSpecies.size() != speciesList.size()) {
            filter.setSpecies(selectedSpecies);
        } else {
            filter.removeFilter(Filter.Type.BY_SPECIES);
        }
        updateButtons();
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
        flexContainer.add(getByDisease());

        main.add(mainTitle);
        main.add(flexContainer);
        add(main);

        updateButtons();
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

        RangeSlider rs = new RangeSlider(histWidth, histHeight, min, max, filterMin, filterMax, histogram);
        rs.addRangeValueChangedHandler(this);
        bySizePanel.add(rs);
        sizeFilterLb.setText("All entities");

        return bySizePanel;
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

        Button selectNoneBtn = new IconButton("none", RESOURCES.selectNoneIcon(), event -> {
            speciesList.forEach(s -> s.setChecked(false));
            speciesListBox.setRowData(speciesList);
            update(null);
        });
        selectNoneBtn.setStyleName(RESOURCES.getCSS().selectBtn());
        selectNoneBtn.setTitle("Select none");

        FlowPanel listButtonsPanel = new FlowPanel();
        listButtonsPanel.setStyleName(RESOURCES.getCSS().listButtonsPanel());
        listButtonsPanel.add(selectAllBtn);
        listButtonsPanel.add(selectNoneBtn);
        FlowPanel listPanel = new FlowPanel();
        listPanel.setStyleName(RESOURCES.getCSS().listPanel());
        listPanel.add(speciesListBox);
        listPanel.add(listButtonsPanel);
        bySpeciesPanel.add(listPanel);

        return bySpeciesPanel;
    }

    private Widget getByDisease() {
        byDiseasePanel = new FlowPanel();
        byDiseasePanel.setStyleName(RESOURCES.getCSS().byDiseasePanel());

        Label title = new Label("\u2022 By disease");
        title.setStyleName(RESOURCES.getCSS().innerTitle());
        byDiseasePanel.add(title);

        Label subtitle = new Label("Show/hide disease pathways");
        subtitle.setStyleName(RESOURCES.getCSS().subtitle());
        byDiseasePanel.add(subtitle);

        includeDisease = new CheckBox("Include disease pathway in the results");
        includeDisease.setStyleName(RESOURCES.getCSS().checkBox());
        includeDisease.setValue(Boolean.TRUE);
        includeDisease.addValueChangeHandler(this);
        byDiseasePanel.add(includeDisease);

        cancelBtn = new IconButton("Cancel", RESOURCES.cancelIcon());
        cancelBtn.setStyleName(RESOURCES.getCSS().applyBtn());
        cancelBtn.addClickHandler(this);
        cancelBtn.setTitle("Cancel filtering");

        applyBtn = new IconButton("Apply", RESOURCES.applyIcon());
        applyBtn.setStyleName(RESOURCES.getCSS().applyBtn());
        applyBtn.addClickHandler(this);
        applyBtn.setTitle("Apply the selected filters");

        FlowPanel buttonsPanel = new FlowPanel();
        buttonsPanel.setStyleName(RESOURCES.getCSS().buttonsPanel());
        buttonsPanel.add(cancelBtn);
        buttonsPanel.add(applyBtn);
        byDiseasePanel.add(buttonsPanel);

        return byDiseasePanel;
    }

    private void updateButtons() {
        applyBtn.setEnabled(filter.isActive());
    }

    private void retrieveHistogram() {
        //TODO remove mockup
        this.histogram = Arrays.asList(20, 15, 10, 15, 20, 20, 15, 10, 15, 20, 20, 15, 10, 15, 20, 20, 15, 10, 15, 20, 20, 15, 10, 15, 20, 20, 15, 10, 15, 20, 20, 15, 10, 15, 20, 20, 15, 10, 15, 20);
    }

    private void retrieveSpecies() {
        //TODO remove mockup
        this.speciesList = Arrays.asList(
                new Species(0L, "Species 1"),
                new Species(1L, "Species 2"),
                new Species(2L, "Species 3"),
                new Species(3L, "Species 4"),
                new Species(4L, "Species 5"),
                new Species(5L, "Species 6"),
                new Species(6L, "Species 7"),
                new Species(7L, "Species 8"),
                new Species(8L, "Species 9"),
                new Species(9L, "Species 10"),
                new Species(10L, "Species 11"),
                new Species(12L, "Species 12"),
                new Species(13L, "Species 13"),
                new Species(14L, "Species 14"));
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

        @Source("../images/select_all.png")
        ImageResource selectAllIcon();

        @Source("../images/select_none.png")
        ImageResource selectNoneIcon();
    }

    @CssResource.ImportedWithPrefix("pwp-FilteringPanel")
    public interface ResourceCSS extends CssResource {
        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/filtering/FilteringPanel.css";

        String main();

        String mainTitle();

        String flexContainer();

        String bySizePanel();

        String bySpeciesPanel();

        String byDiseasePanel();

        String innerTitle();

        String subtitle();

        String sizeFilterLb();

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
