package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwaySelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PathwaySelectedEvent extends GwtEvent<PathwaySelectedHandler> {
    public static Type<PathwaySelectedHandler> TYPE = new Type<PathwaySelectedHandler>();

    private Long dbId;

    public PathwaySelectedEvent(Long dbId) {
        this.dbId = dbId;
    }

    public Long getIdentifier() {
        return dbId;
    }

    @Override
    public Type<PathwaySelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PathwaySelectedHandler handler) {
        handler.onPathwaySelected(this);
    }
}
