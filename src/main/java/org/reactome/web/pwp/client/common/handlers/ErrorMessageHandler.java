package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ErrorMessageHandler extends EventHandler {
    void onInternalMessage(ErrorMessageEvent event);
}
