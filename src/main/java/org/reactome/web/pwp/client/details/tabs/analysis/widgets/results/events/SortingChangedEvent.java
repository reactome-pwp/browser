package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.SortingChangedHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class SortingChangedEvent extends GwtEvent<SortingChangedHandler> {
    public static Type<SortingChangedHandler> TYPE = new Type<>();

    private SortingType sortingBy;

    public SortingChangedEvent(SortingType sortingBy) {
        this.sortingBy = sortingBy;
    }

    @Override
    public Type<SortingChangedHandler> getAssociatedType() {
        return TYPE;
    }

    public SortingType getSortingBy() {
        return sortingBy;
    }

    @Override
    protected void dispatch(SortingChangedHandler handler) {
        handler.onSortingChanged(this);
    }
}
