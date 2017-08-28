package org.reactome.web.pwp.client.toppanel.species;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import org.reactome.web.pwp.model.client.classes.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SpeciesSelectorDisplay extends FlowPanel implements SpeciesSelector.Display, ChangeHandler {

    private SpeciesSelector.Presenter presenter;
    private ListBox species;

    public SpeciesSelectorDisplay() {
        Styles css = RESOURCES.getCSS();
        setStyleName(css.speciesPanel());

        this.add(new InlineLabel("Pathways for:"));

        this.species = new ListBox();
        this.species.insertItem("Loading species list...", 0);
        this.species.setMultipleSelect(false);
        this.species.addChangeHandler(this);
        this.add(this.species);
    }

    @Override
    public void setPresenter(SpeciesSelector.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setData(List<Species> speciesList) {
        this.species.clear(); //Not really needed, but just in case :)
        for (Species species : speciesList) {
            this.species.addItem(species.getDisplayName(), species.getDbId().toString());
        }
    }

    @Override
    public void setSelectedIndex(int index) {
        this.species.setSelectedIndex(index);
        setStyleBasedOnSelection(index);
    }

    @Override
    public void onChange(ChangeEvent event) {
        setStyleBasedOnSelection(species.getSelectedIndex());
        this.presenter.setSpeciesSelected(species.getSelectedValue());
    }

    private void setStyleBasedOnSelection(int index) {
        if (index == 0) {
            setStyleName(RESOURCES.getCSS().speciesPanel());
        } else {
            addStyleName(RESOURCES.getCSS().otherSpecies());
        }
    }

    public static final Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(Styles.CSS)
        Styles getCSS();
    }

    @CssResource.ImportedWithPrefix("pwp-SpeciesDisplay")
    public interface Styles extends CssResource {
        String CSS = "org/reactome/web/pwp/client/toppanel/species/SpeciesPanel.css";

        String speciesPanel();

        String otherSpecies();
    }
}
