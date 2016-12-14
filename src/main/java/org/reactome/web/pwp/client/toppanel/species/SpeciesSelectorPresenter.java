package org.reactome.web.pwp.client.toppanel.species;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.SpeciesSelectedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.model.classes.Species;
import org.reactome.web.pwp.model.client.RESTFulClient;
import org.reactome.web.pwp.model.handlers.DatabaseObjectsLoadedHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SpeciesSelectorPresenter extends AbstractPresenter implements SpeciesSelector.Presenter {

    private SpeciesSelector.Display display;

    private Species currentSpecies;
    private List<Species> speciesList;
    private boolean loaded = false;

    public SpeciesSelectorPresenter(EventBus eventBus, SpeciesSelector.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
        this.retrieveSpeciesList();
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        Species species = event.getState().getSpecies();
        if (species != null && !species.equals(this.currentSpecies)) {
            this.currentSpecies = species;
            switchDisplayToSpecies(species);
        }
    }

    @Override
    public void setSpeciesSelected(String dbId) {
        for (Species species : speciesList) {
            if (species.getDbId().toString().equals(dbId)) {
                this.currentSpecies = species;
                this.eventBus.fireEventFromSource(new SpeciesSelectedEvent(species), this);
                break;
            }
        }
    }

    private void switchDisplayToSpecies(Species species) {
        if (species != null) {
            int index = speciesList.indexOf(species);
            if (loaded) this.display.setSelectedIndex(index);
        }
    }

    private void retrieveSpeciesList() {
        this.speciesList = new LinkedList<>();
        RESTFulClient.getSpeciesList(new DatabaseObjectsLoadedHandler<Species>() {
            @Override
            public void onDatabaseObjectLoaded(List<Species> species) {
                speciesList = species;
                display.setData(speciesList);
                loaded = true;
                switchDisplayToSpecies(currentSpecies);
            }

            @Override
            public void onDatabaseObjectError(Throwable ex) {
                display.setData(speciesList);
                eventBus.fireEventFromSource(new ErrorMessageEvent(ex.getMessage()), this);
            }
        });
    }
}
