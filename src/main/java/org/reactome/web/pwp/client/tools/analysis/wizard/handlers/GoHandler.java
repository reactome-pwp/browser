package org.reactome.web.pwp.client.tools.analysis.wizard.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.GoEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface GoHandler extends EventHandler {
    void onGoSelected(GoEvent event);
}
