package org.reactome.web.pwp.client.tools.analysis.handler;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.tools.analysis.event.AnalysisCompletedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisCompletedHandler extends EventHandler {

    void onAnalysisCompleted(AnalysisCompletedEvent event);
}
