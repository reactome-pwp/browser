package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.FilterAppliedEvent;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface FilterAppliedHandler extends EventHandler {

    void onFilterApplied(FilterAppliedEvent event);

}
