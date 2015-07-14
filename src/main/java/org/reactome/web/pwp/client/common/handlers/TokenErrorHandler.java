package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.TokenErrorEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface TokenErrorHandler extends EventHandler {
    void onTokenError(TokenErrorEvent event);
}
