package org.reactome.web.pwp.client.tools.analysis.handler;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.tools.analysis.event.ServiceUnavailableEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ServiceUnavailableEventHandler extends EventHandler {
    void onServiceUnavailableEvent(ServiceUnavailableEvent event);
}
