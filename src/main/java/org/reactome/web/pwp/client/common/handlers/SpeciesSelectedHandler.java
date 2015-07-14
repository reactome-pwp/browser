package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.SpeciesSelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SpeciesSelectedHandler extends EventHandler {
    void onSpeciesSelected(SpeciesSelectedEvent event);
}
