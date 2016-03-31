package org.reactome.web.pwp.client.tools.analysis.wizard.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.AnalysisErrorEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisErrorHandler extends EventHandler {

    void onAnalysisError(AnalysisErrorEvent event);

}
