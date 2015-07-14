package org.reactome.web.pwp.client.tools.analysis.event;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisResult;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisCompletedHandler;

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
