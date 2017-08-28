package org.reactome.web.pwp.client.toppanel.species;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.SpeciesSelectedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.model.client.classes.Species;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;

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
        ContentClient.getSpeciesList(new ContentClientHandler.ObjectListLoaded<Species>() {
            @Override
            public void onObjectListLoaded(List<Species> list) {
                speciesList = list;
                display.setData(speciesList);
                loaded = true;
                switchDisplayToSpecies(currentSpecies);
            }

            @Override
            public void onContentClientException(Type type, String message) {
                display.setData(speciesList);
                eventBus.fireEventFromSource(new ErrorMessageEvent(message), this);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                display.setData(speciesList);
                //TODO
                eventBus.fireEventFromSource(new ErrorMessageEvent(error.getMessage().get(0)), this);
            }
        });
    }
}
