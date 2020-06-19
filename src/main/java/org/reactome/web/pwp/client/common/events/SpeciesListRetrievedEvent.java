package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.SpeciesListRetrievedHandler;
import org.reactome.web.pwp.model.client.classes.Species;

import java.util.List;

public class SpeciesListRetrievedEvent extends GwtEvent<SpeciesListRetrievedHandler> {
    public static Type<SpeciesListRetrievedHandler> TYPE = new Type<>();
    private List<Species> speciesList;

    public SpeciesListRetrievedEvent(List<Species> speciesList) {
        this.speciesList = speciesList;
    }

    @Override
    public Type<SpeciesListRetrievedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SpeciesListRetrievedHandler handler) {
        handler.onSpeciesListRetrieved(this);
    }

    public List<Species> getSpeciesList() {
        return speciesList;
    }

}
