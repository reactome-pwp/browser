package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ActionSelectedHandler;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ActionSelectedEvent extends GwtEvent<ActionSelectedHandler> {
    public static Type<ActionSelectedHandler> TYPE = new Type<>();

    private Action action;

    public enum Action {
        FILTERING_ON,
        FILTERING_OFF,
        HELP_ON,
        HELP_OFF,
        CLUSTERING_ON,
        CLUSTERING_OFF,
    }

    public ActionSelectedEvent(Action action) {
        this.action = action;
    }

    @Override
    public Type<ActionSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ActionSelectedHandler handler) {
        handler.onActionSelected(this);
    }

    public Action getAction() {
        return action;
    }
}
