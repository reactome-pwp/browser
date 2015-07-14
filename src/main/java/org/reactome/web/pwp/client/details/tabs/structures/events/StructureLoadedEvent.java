package org.reactome.web.pwp.client.details.tabs.structures.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.structures.handlers.StructureLoadedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StructureLoadedEvent extends GwtEvent<StructureLoadedHandler> {
    public static Type<StructureLoadedHandler> TYPE = new Type<>();

    @Override
    public Type<StructureLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StructureLoadedHandler handler) {
        handler.onStructureLoaded(this);
    }
}
