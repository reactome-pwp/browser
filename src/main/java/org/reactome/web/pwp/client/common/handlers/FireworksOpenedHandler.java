package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.FireworksOpenedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface FireworksOpenedHandler extends EventHandler {
    void onFireworksOpened(FireworksOpenedEvent event);
}
