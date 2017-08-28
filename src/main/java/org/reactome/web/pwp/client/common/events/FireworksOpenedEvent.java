package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.FireworksOpenedHandler;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksOpenedEvent extends GwtEvent<FireworksOpenedHandler> {
    public static final Type<FireworksOpenedHandler> TYPE = new Type<>();

    private Pathway pathway;

    public FireworksOpenedEvent(Pathway pathway) {
        this.pathway = pathway;
    }

    @Override
    public Type<FireworksOpenedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FireworksOpenedHandler handler) {
        handler.onFireworksOpened(this);
    }


    public Pathway getPathway() {
        return pathway;
    }

    @Override
    public String toString() {
        return "FireworksOpenedEvent{}";
    }
}
