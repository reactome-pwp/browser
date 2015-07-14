package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.ViewportChangedHandler;
import org.reactome.web.pwp.client.viewport.ViewportToolType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ViewportChangedEvent extends GwtEvent<ViewportChangedHandler> {
    public static final Type<ViewportChangedHandler> TYPE = new Type<>();

    private ViewportToolType viewportTool;

    public ViewportChangedEvent(ViewportToolType viewportTool) {
        this.viewportTool = viewportTool;
    }

    @Override
    public Type<ViewportChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ViewportChangedHandler handler) {
        handler.onViewportChanged(this);
    }

    public ViewportToolType getViewportTool() {
        return viewportTool;
    }

    @Override
    public String toString() {
        return "ViewportChangedEvent{" +
                "viewportTool=" + viewportTool +
                '}';
    }
}
