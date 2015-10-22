package org.reactome.web.pwp.client.tools.analysis.event;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisError;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisErrorEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisErrorEvent extends GwtEvent<AnalysisErrorEventHandler> {
    public static Type<AnalysisErrorEventHandler> TYPE = new GwtEvent.Type<AnalysisErrorEventHandler>();

    private AnalysisError analysisError;

    public AnalysisErrorEvent(AnalysisError analysisError) {
        this.analysisError = analysisError;
    }

    @Override
    public Type<AnalysisErrorEventHandler> getAssociatedType() {
        return TYPE;
    }

    public AnalysisError getAnalysisError() {
        return analysisError;
    }

    @Override
    protected void dispatch(AnalysisErrorEventHandler handler) {
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
