package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.OptionSelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface OptionSelectedHandler extends EventHandler {

    public void onOptionSelected(OptionSelectedEvent event);

}
