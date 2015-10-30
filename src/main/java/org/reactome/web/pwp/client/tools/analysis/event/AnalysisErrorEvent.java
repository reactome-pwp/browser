package org.reactome.web.pwp.client.tools.analysis.event;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisError;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisErrorHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisErrorEvent extends GwtEvent<AnalysisErrorHandler> {
    public static Type<AnalysisErrorHandler> TYPE = new GwtEvent.Type<AnalysisErrorHandler>();

    private AnalysisError analysisError;

    public AnalysisErrorEvent(AnalysisError analysisError) {
        this.analysisError = analysisError;
    }

    @Override
    public Type<AnalysisErrorHandler> getAssociatedType() {
        return TYPE;
    }

    public AnalysisError getAnalysisError() {
        return analysisError;
    }

    @Override
    protected void dispatch(AnalysisErrorHandler handler) {
        handler.onAnalysisError(this);
    }

    @Override
    public String toString() {
        if(analysisError==null) return "AnalysisErrorEvent{}";
        return "AnalysisErrorEvent{" +
                "code=" + analysisError.getCode() +
                ", reason=" + analysisError.getReason() +
                ", messages=" + analysisError.getMessages() +
                '}';
    }
}
