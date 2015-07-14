package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.BrowserReadyHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class BrowserReadyEvent extends GwtEvent<BrowserReadyHandler> {
    public static final Type<BrowserReadyHandler> TYPE = new Type<>();

    @Override
    public Type<BrowserReadyHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BrowserReadyHandler handler) {
        handler.onBrowserReady(this);
    }

    @Override
    public String toString() {
        return "BrowserReadyEvent{}";
    }
}
