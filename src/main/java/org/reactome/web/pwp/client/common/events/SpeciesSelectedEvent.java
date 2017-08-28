package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.SpeciesSelectedHandler;
import org.reactome.web.pwp.model.client.classes.Species;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SpeciesSelectedEvent extends GwtEvent<SpeciesSelectedHandler> {
    public static final Type<SpeciesSelectedHandler> TYPE = new Type<>();

    private Species species;

    public SpeciesSelectedEvent(Species species) {
        this.species = species;
    }

    @Override
    public Type<SpeciesSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SpeciesSelectedHandler handler) {
        handler.onSpeciesSelected(this);
    }

    public Species getSpecies() {
        return species;
    }

    @Override
    public String toString() {
        return "SpeciesSelectedEvent{" +
                "species=" + species +
                '}';
    }
}
