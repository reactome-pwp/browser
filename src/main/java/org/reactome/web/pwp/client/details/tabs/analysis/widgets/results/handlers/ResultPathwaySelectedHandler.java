package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.ResultPathwaySelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ResultPathwaySelectedHandler extends EventHandler {

    void onPathwayFoundEntitiesSelected(ResultPathwaySelectedEvent event);

}
