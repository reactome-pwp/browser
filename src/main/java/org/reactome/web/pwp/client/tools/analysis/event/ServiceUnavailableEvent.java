package org.reactome.web.pwp.client.tools.analysis.event;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.tools.analysis.handler.ServiceUnavailableEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ServiceUnavailableEvent extends GwtEvent<ServiceUnavailableEventHandler> {
    public static final Type<ServiceUnavailableEventHandler> TYPE = new Type<>();

    @Override
    public Type<ServiceUnavailableEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ServiceUnavailableEventHandler handler) {
        handler.onServiceUnavailableEvent(this);
    }

    @Override
    public String toString() {
        return "ServiceUnavailableEvent{}";
    }
}
