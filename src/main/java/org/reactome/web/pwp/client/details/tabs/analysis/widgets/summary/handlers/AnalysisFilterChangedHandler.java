package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.AnalysisFilterChangedEvent;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public interface AnalysisFilterChangedHandler extends EventHandler {

    void onAnalysisFilterChanged(AnalysisFilterChangedEvent event);

}
