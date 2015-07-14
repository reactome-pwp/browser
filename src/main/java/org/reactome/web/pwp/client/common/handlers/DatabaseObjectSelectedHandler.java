package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.DatabaseObjectSelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DatabaseObjectSelectedHandler extends EventHandler {
    void onDatabaseObjectSelected(DatabaseObjectSelectedEvent event);
}
