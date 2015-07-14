package org.reactome.web.pwp.client.toppanel.species;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.SpeciesSelectedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.model.classes.Species;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;

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
        if(species!=null && !species.equals(this.currentSpecies)){
            this.currentSpecies = species;
            switchDisplayToSpecies(species);
        }
    }

    @Override
    public void setSpeciesSelected(String dbId) {
        for (Species species : speciesList) {
            if(species.getDbId().toString().equals(dbId)){
                this.currentSpecies = species;
                this.eventBus.fireEventFromSource(new SpeciesSelectedEvent(species), this);
                break;
            }
        }
    }

    private void switchDisplayToSpecies(Species species){
        if(species!=null) {
            int index = speciesList.indexOf(species);
            if (loaded) this.display.setSelectedIndex(index);
        }
    }

    private void retrieveSpeciesList() {
        this.speciesList = new LinkedList<>();
        String url = "/ReactomeRESTfulAPI/RESTfulWS/speciesList/";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");

        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                            for (int i = 0; i < list.size(); ++i) {
                                JSONObject object = list.get(i).isObject();
                                Species species = (Species) DatabaseObjectFactory.create(object);
                                //Sometimes the RESTFul services retrieves several times the same speciesList
                                //Note a Set does not help because order is important here
                                if (!speciesList.contains(species)) {
                                    speciesList.add(species);
                                }
                            }
                            display.setData(speciesList);
                            loaded = true;
                            switchDisplayToSpecies(currentSpecies);
                            break;
                        default:
                            display.setData(speciesList);
                            eventBus.fireEventFromSource(new ErrorMessageEvent("There was an error while retrieving species list: " + response.getStatusText()), this);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    display.setData(speciesList);
                    eventBus.fireEventFromSource(new ErrorMessageEvent("Species list not available", exception), this);
                }
            });
        } catch (RequestException e) {
            display.setData(speciesList);
            eventBus.fireEventFromSource(new ErrorMessageEvent("Species list can not be retrieved", e), this);
        }
    }
}
