package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.StateChangedHandler;
import org.reactome.web.pwp.client.manager.state.State;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StateChangedEvent extends GwtEvent<StateChangedHandler> {
    public static Type<StateChangedHandler> TYPE = new Type<>();

    private State state;

    public StateChangedEvent(State state) {
        this.state = state;
    }

    @Override
    public Type<StateChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StateChangedHandler handler) {
        handler.onStateChanged(this);
    }

    public State getState() {
        return state;
    }

    @Override
    public String toString() {
        return "StateChangedEvent{" +
                "state=" + state +
                '}';
    }
}
