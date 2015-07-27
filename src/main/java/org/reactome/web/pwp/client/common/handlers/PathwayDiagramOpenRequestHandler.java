package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.PathwayDiagramOpenRequestEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface PathwayDiagramOpenRequestHandler extends EventHandler {

    void onPathwayDiagramOpenRequest(PathwayDiagramOpenRequestEvent event);

}
