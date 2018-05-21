package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.TermFlaggedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TermFlaggedEvent extends GwtEvent<TermFlaggedHandler> {
    public static final Type<TermFlaggedHandler> TYPE = new Type<>();

    private String term;

    public TermFlaggedEvent(String term) {
        this.term = term;
    }

    @Override
    public Type<TermFlaggedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TermFlaggedHandler handler) {
        handler.onTermFlagged(this);
    }

    public String getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return "TermFlaggedEvent{" +
                "term='" + term + '\'' +
                '}';
    }
}