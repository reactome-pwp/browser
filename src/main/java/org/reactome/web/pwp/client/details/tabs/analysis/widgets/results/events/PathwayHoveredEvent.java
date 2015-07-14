package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwayHoveredHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PathwayHoveredEvent extends GwtEvent<PathwayHoveredHandler> {
    public static Type<PathwayHoveredHandler> TYPE = new Type<PathwayHoveredHandler>();

    private Long dbId;

    public PathwayHoveredEvent(Long dbId) {
        this.dbId = dbId;
    }

    public Long getIdentifier() {
        return dbId;
    }

    @Override
    public Type<PathwayHoveredHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PathwayHoveredHandler handler) {
        handler.onPathwayHovered(this);
    }
}
