package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwayHoveredResetHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PathwayHoveredResetEvent extends GwtEvent<PathwayHoveredResetHandler> {
    public static Type<PathwayHoveredResetHandler> TYPE = new Type<PathwayHoveredResetHandler>();

    @Override
    public Type<PathwayHoveredResetHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PathwayHoveredResetHandler handler) {
        handler.onPathwayHoveredReset(this);
    }
}
