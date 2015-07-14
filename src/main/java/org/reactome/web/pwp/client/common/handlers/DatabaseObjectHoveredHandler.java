package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.DatabaseObjectHoveredEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DatabaseObjectHoveredHandler extends EventHandler {
    void onDatabaseObjectHovered(DatabaseObjectHoveredEvent event);
}
