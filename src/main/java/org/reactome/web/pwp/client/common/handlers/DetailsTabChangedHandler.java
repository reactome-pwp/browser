package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.DetailsTabChangedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DetailsTabChangedHandler extends EventHandler {
    void onDetailsTabChanged(DetailsTabChangedEvent event);
}
