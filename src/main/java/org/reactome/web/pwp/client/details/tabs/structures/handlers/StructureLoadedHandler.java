package org.reactome.web.pwp.client.details.tabs.structures.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.details.tabs.structures.events.StructureLoadedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface StructureLoadedHandler extends EventHandler {

    void onStructureLoaded(StructureLoadedEvent structureLoadedEvent);

}
