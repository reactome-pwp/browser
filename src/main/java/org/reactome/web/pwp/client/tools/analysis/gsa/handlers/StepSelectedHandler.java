package org.reactome.web.pwp.client.tools.analysis.gsa.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface StepSelectedHandler extends EventHandler {
    void onStepSelected(StepSelectedEvent event);
}
