package org.reactome.web.pwp.client.hierarchy.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemDoubleClickedHandler;
import org.reactome.web.pwp.client.hierarchy.widget.HierarchyItem;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyItemDoubleClickedEvent extends GwtEvent<HierarchyItemDoubleClickedHandler> {
    public static Type<HierarchyItemDoubleClickedHandler> TYPE = new Type<>();

    private HierarchyItem item;

    public HierarchyItemDoubleClickedEvent(HierarchyItem item) {
        this.item = item;
    }

    @Override
    public Type<HierarchyItemDoubleClickedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HierarchyItemDoubleClickedHandler handler) {
        handler.onHierarchyItemDoubleClicked(this);
    }

    public HierarchyItem getItem() {
        return item;
    }
}
