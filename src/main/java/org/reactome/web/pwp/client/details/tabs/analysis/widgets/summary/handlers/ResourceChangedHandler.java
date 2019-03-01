package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ResourceChangedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@Deprecated
public interface ResourceChangedHandler extends EventHandler {

    void onResourceChanged(ResourceChangedEvent event);

}
