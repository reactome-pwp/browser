package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.TermFlaggedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface TermFlaggedHandler extends EventHandler {

    void onTermFlagged(TermFlaggedEvent event);
}
