package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisCompletedEvent extends GwtEvent<AnalysisCompletedHandler> {
    public static Type<AnalysisCompletedHandler> TYPE = new GwtEvent.Type<AnalysisCompletedHandler>();

    AnalysisResult analysisResult;

    public AnalysisCompletedEvent(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    @Override
    public Type<AnalysisCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    @Override
    protected void dispatch(AnalysisCompletedHandler handler) {
        handler.onAnalysisCompleted(this);
    }

    @Override
    public String toString() {
        return "AnalysisCompletedEvent{" +
                "analysis=" + analysisResult.getSummary().getToken() +
                '}';
    }
}
