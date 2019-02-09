package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.Filter;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.FilterAppliedHandler;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FilterAppliedEvent extends GwtEvent<FilterAppliedHandler> {
    public static Type<FilterAppliedHandler> TYPE = new Type<>();

    private Filter filter;

    public FilterAppliedEvent(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Type<FilterAppliedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FilterAppliedHandler handler) {
        handler.onFilterApplied(this);
    }

    public Filter getFilter() {
        return filter;
    }
}
