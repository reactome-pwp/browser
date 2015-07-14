package org.reactome.web.pwp.client.tools.analysis.event;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisErrorEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisErrorEvent extends GwtEvent<AnalysisErrorEventHandler> {
    public static Type<AnalysisErrorEventHandler> TYPE = new GwtEvent.Type<AnalysisErrorEventHandler>();

    private AnalysisErrorType errorType;

    public AnalysisErrorEvent(AnalysisErrorType errorType) {
        this.errorType = errorType;
    }

    @Override
    public Type<AnalysisErrorEventHandler> getAssociatedType() {
        return TYPE;
    }

    public AnalysisErrorType getErrorType() {
        return errorType;
    }

    @Override
    protected void dispatch(AnalysisErrorEventHandler handler) {
        handler.onAnalysisError(this);
    }

    @Override
    public String toString() {
        return "AnalysisErrorEvent{" +
                "errorType=" + errorType +
                '}';
    }
}
