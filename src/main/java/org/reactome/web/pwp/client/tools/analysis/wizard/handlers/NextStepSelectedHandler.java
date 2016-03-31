package org.reactome.web.pwp.client.tools.analysis.wizard.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.NextStepSelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface NextStepSelectedHandler extends EventHandler {
    void onNextStepSelected(NextStepSelectedEvent event);
}
