package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.InteractorsPathwaySelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InteractorsPathwaySelectedEvent extends GwtEvent<InteractorsPathwaySelectedHandler> {
    public static Type<InteractorsPathwaySelectedHandler> TYPE = new Type<>();

    PathwaySummary pathwaySummary;

    public InteractorsPathwaySelectedEvent(PathwaySummary pathwaySummary) {
        this.pathwaySummary = pathwaySummary;
    }

    @Override
    public Type<InteractorsPathwaySelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(InteractorsPathwaySelectedHandler handler) {
        handler.onPathwayFoundInteractorsSelected(this);
    }

    public PathwaySummary getPathwaySummary() {
        return pathwaySummary;
    }
}
