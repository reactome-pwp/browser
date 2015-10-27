package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisCompletedHandler extends EventHandler {

    void onAnalysisCompleted(AnalysisCompletedEvent event);
}
