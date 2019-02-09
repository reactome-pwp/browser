package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ActionSelectedEvent;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface ActionSelectedHandler extends EventHandler {

    void onActionSelected(ActionSelectedEvent event);

}
