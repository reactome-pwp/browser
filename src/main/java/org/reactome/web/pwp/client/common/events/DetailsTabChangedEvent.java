package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.DetailsTabChangedHandler;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DetailsTabChangedEvent extends GwtEvent<DetailsTabChangedHandler> {
    public static final Type<DetailsTabChangedHandler> TYPE = new Type<>();

    private DetailsTabType detailsTab;

    public DetailsTabChangedEvent(DetailsTabType detailsTab) {
        this.detailsTab = detailsTab;
    }

    @Override
    public Type<DetailsTabChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DetailsTabChangedHandler handler) {
        handler.onDetailsTabChanged(this);
    }

    public DetailsTabType getDetailsTab() {
        return detailsTab;
    }

    @Override
    public String toString() {
        return "DetailsTabChangedEvent{" +
                "detailsTab=" + detailsTab.getTitle() +
                '}';
    }
}
