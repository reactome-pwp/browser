package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.Filter;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.FilterRemovedHandler;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FilterRemovedEvent extends GwtEvent<FilterRemovedHandler> {
    public static Type<FilterRemovedHandler> TYPE = new Type<>();

    private Filter.Type removed;

    public FilterRemovedEvent(Filter.Type removed) {
        this.removed = removed;
    }

    @Override
    public Type<FilterRemovedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FilterRemovedHandler handler) { handler.onFilterRemoved(this);
    }

    public Filter.Type getRemovedFilter() {
        return removed;
    }
}
