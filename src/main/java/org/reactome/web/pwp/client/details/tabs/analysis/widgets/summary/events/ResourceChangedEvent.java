package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ResourceChangedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ResourceChangedEvent extends GwtEvent<ResourceChangedHandler> {
    public static Type<ResourceChangedHandler> TYPE = new Type<ResourceChangedHandler>();

    private String resource;

    public ResourceChangedEvent(String resource) {
        this.resource = resource;
    }

    @Override
    public Type<ResourceChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ResourceChangedHandler handler) {
        handler.onResourceChanged(this);
    }

    public String getResource() {
        return resource;
    }
}
