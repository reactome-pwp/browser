package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.LayoutSelectorChangedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface LayoutSelectorChangedHandler extends EventHandler {
    void onLayoutSelectorChanged(LayoutSelectorChangedEvent event);
}
