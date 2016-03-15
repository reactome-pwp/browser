package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.EntitiesPathwaySelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesPathwaySelectedEvent extends GwtEvent<EntitiesPathwaySelectedHandler> {
    public static Type<EntitiesPathwaySelectedHandler> TYPE = new Type<>();

    PathwaySummary pathwaySummary;

    public EntitiesPathwaySelectedEvent(PathwaySummary pathwaySummary) {
        this.pathwaySummary = pathwaySummary;
    }

    @Override
    public Type<EntitiesPathwaySelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EntitiesPathwaySelectedHandler handler) {
        handler.onPathwayFoundEntitiesSelected(this);
    }

    public PathwaySummary getPathwaySummary() {
        return pathwaySummary;
    }
}
