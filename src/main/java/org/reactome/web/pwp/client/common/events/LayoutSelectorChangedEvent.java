package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.LayoutSelectorChangedHandler;
import org.reactome.web.pwp.client.toppanel.layout.LayoutSelectorType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class LayoutSelectorChangedEvent extends GwtEvent<LayoutSelectorChangedHandler> {
    public static final Type<LayoutSelectorChangedHandler> TYPE = new Type<>();

    private LayoutSelectorType type;

    public LayoutSelectorChangedEvent(LayoutSelectorType type) {
        this.type = type;
    }

    @Override
    public Type<LayoutSelectorChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LayoutSelectorChangedHandler handler) {
        handler.onLayoutSelectorChanged(this);
    }

    public LayoutSelectorType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "LayoutSelectorChangedEvent{" +
                "type=" + type +
                '}';
    }
}
