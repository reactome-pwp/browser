package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.PathwayPortalTool;
import org.reactome.web.pwp.client.common.handlers.ToolSelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ToolSelectedEvent extends GwtEvent<ToolSelectedHandler> {
    public static final Type<ToolSelectedHandler> TYPE = new Type<>();

    private PathwayPortalTool tool;

    public ToolSelectedEvent(PathwayPortalTool tool) {
        this.tool = tool;
    }

    @Override
    public Type<ToolSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ToolSelectedHandler handler) {
        handler.onToolSelected(this);
    }

    public PathwayPortalTool getTool() {
        return tool;
    }

    @Override
    public String toString() {
        return "ToolSelectedEvent{" +
                "tool=" + tool +
                '}';
    }
}
