package org.reactome.web.pwp.client.tools.analysis.wizard.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.AnalysisErrorHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisErrorEvent extends GwtEvent<AnalysisErrorHandler> {
    public static Type<AnalysisErrorHandler> TYPE = new Type<>();

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
