package org.reactome.web.pwp.client.tools.analysis.handler;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.tools.analysis.event.AnalysisErrorEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisErrorHandler extends EventHandler {

    void onAnalysisError(AnalysisErrorEvent event);

}
