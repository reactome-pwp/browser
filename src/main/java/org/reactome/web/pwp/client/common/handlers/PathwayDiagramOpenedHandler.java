package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.PathwayDiagramOpenedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface PathwayDiagramOpenedHandler extends EventHandler {
    void onPathwayDiagramOpened(PathwayDiagramOpenedEvent event);
}
