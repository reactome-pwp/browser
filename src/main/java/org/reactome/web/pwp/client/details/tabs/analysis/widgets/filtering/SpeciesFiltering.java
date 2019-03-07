package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.SpeciesSummary;
import org.reactome.web.diagram.common.IconButton;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species.Species;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species.SpeciesCell;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class SpeciesFiltering extends FlowPanel implements FilteringWidget, ValueUpdater<Species> {
    private Handler handler;
    private FilteringPanel.Resources style;

    private CellList<Species> speciesListBox;
    private List<Species> speciesList;

    public SpeciesFiltering(Handler handler) {
        this.handler = handler;
        style = FilteringPanel.RESOURCES;
    }

    @Override
    public Widget initUI() {
        clear();
        setStyleName(style.getCSS().bySpeciesPanel());

        Label title = new Label("\u2022 By species");
        title.setStyleName(style.getCSS().innerTitle());
        add(title);

        Label subtitle = new Label("Show pathways belonging to he following species:");
        subtitle.setStyleName(style.getCSS().subtitle());
        add(subtitle);

        populateSpeciesList();

        SpeciesCell speciesCell = new SpeciesCell();
        speciesListBox = new CellList<>(speciesCell);
        speciesListBox.setStyleName(style.getCSS().speciesListBox());
        speciesListBox.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);
        speciesListBox.setRowCount(speciesList.size(), true);
        speciesListBox.setRowData(speciesList);
        speciesListBox.setValueUpdater(this);

        Button selectAllBtn = new IconButton("all", style.selectAllIcon(), event -> {
            speciesList.forEach(s -> s.setChecked(true));
            speciesListBox.setRowData(speciesList);
            update(null);
        });
        selectAllBtn.setStyleName(style.getCSS().selectBtn());
        selectAllBtn.setTitle("Select all species");

        FlowPanel listButtonsPanel = new FlowPanel();
        listButtonsPanel.setStyleName(style.getCSS().listButtonsPanel());
        listButtonsPanel.add(selectAllBtn);
        FlowPanel listPanel = new FlowPanel();
        listPanel.setStyleName(style.getCSS().listPanel());
        listPanel.add(speciesListBox);
        listPanel.add(listButtonsPanel);
        add(listPanel);
        return this;
    }

    @Override
    public void updateUI() {
        populateSpeciesList();
        speciesListBox.setRowData(speciesList);
    }

    @Override
    public void update(Species value) {
        List<Species> previouslySelectedSpecies = speciesList.stream().filter(Species::isChecked).collect(Collectors.toList());
        if(value != null) {
            value.setChecked(!value.isChecked());
        }
        List<Species> selectedSpecies = speciesList.stream().filter(Species::isChecked).collect(Collectors.toList());

//        Console.info("Before: " +  previouslySelectedSpecies.size() + " after: " + selectedSpecies.size());

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
            handler.onSpeciesChanged(selectedSpecies);
            handler.loadAnalysisData();

        } else if (selectedSpecies.size() == speciesList.size()) {
            //  All of the species are selected so the filter should be removed
            handler.onSpeciesChanged(null);
            handler.loadAnalysisData();

        } else if (selectedSpecies.size() == 0) {
            //  At least one of the species has to be selected, thus if nothing is selected we select all of them and
            // remove the filter
            speciesList.forEach(species -> species.setChecked(true));
            speciesListBox.setRowData(speciesList);
            handler.onSpeciesChanged(null);
            handler.loadAnalysisData();

        } else {
            handler.onSpeciesChanged(selectedSpecies);
            handler.loadAnalysisData();
        }
    }

    private void populateSpeciesList() {
        this.speciesList = new ArrayList<>();
        AnalysisResult analysisResult = handler.getAnalysisResult();
        if (analysisResult.getSpeciesSummary() != null) {
            for (SpeciesSummary speciesSummary : analysisResult.getSpeciesSummary()) {
                this.speciesList.add(new Species(speciesSummary));
            }
        }

        List<String> filterSpecies = handler.getFilter().getSpeciesList();
        if (filterSpecies.size() == 0) {
            speciesList.forEach(s -> s.setChecked(true));
        } else {
            speciesList.forEach(s -> s.setChecked(filterSpecies.contains(s.getId().toString())));
        }
    }
}
