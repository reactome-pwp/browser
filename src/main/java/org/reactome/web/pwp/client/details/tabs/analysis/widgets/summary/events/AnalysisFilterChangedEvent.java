package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.analysis.client.filter.ResultFilter;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.AnalysisFilterChangedHandler;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public class AnalysisFilterChangedEvent extends GwtEvent<AnalysisFilterChangedHandler> {
    public static Type<AnalysisFilterChangedHandler> TYPE = new Type<>();

    private ResultFilter filter;

    public AnalysisFilterChangedEvent(ResultFilter filter) {
        this.filter = filter;
    }

    @Override
    public Type<AnalysisFilterChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnalysisFilterChangedHandler handler) {
        handler.onAnalysisFilterChanged(this);
    }

    public ResultFilter getFilter() {
        return filter;
    }
}
