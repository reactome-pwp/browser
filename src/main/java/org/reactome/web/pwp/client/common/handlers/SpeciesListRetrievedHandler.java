package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.SpeciesListRetrievedEvent;

public interface SpeciesListRetrievedHandler extends EventHandler {
    void onSpeciesListRetrieved(SpeciesListRetrievedEvent event);
}
