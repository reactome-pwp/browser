package org.reactome.web.pwp.client.hierarchy.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOutHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyItemMouseOutEvent extends GwtEvent<HierarchyItemMouseOutHandler> {
    public static Type<HierarchyItemMouseOutHandler> TYPE = new Type<>();

    @Override
    public Type<HierarchyItemMouseOutHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HierarchyItemMouseOutHandler handler) {
        handler.onHierarchyItemHoveredReset();
    }
}
