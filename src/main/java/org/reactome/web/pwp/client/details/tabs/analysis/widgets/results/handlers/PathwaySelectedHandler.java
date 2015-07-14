package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwaySelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface PathwaySelectedHandler extends EventHandler {

    void onPathwaySelected(PathwaySelectedEvent event);

}
