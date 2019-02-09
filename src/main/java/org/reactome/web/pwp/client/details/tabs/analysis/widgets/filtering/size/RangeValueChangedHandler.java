package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface RangeValueChangedHandler extends EventHandler {

    void onRangeValueChanged(RangeValueChangedEvent event);

}
