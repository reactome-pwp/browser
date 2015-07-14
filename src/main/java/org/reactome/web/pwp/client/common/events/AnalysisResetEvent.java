package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.AnalysisResetHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisResetEvent extends GwtEvent<AnalysisResetHandler> {
    public static final Type<AnalysisResetHandler> TYPE = new Type<>();

    @Override
    public Type<AnalysisResetHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnalysisResetHandler handler) {
        handler.onAnalysisReset();
    }

    @Override
    public String toString() {
        return "AnalysisResetEvent{}";
    }
}
