package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnalysisCompletedEvent extends GwtEvent<AnalysisCompletedHandler> {
    public static Type<AnalysisCompletedHandler> TYPE = new GwtEvent.Type<AnalysisCompletedHandler>();

    AnalysisResult analysisResult;
    boolean hideSubmissionDialog;

    public AnalysisCompletedEvent(AnalysisResult analysisResult) {
        this(analysisResult, true);
    }

    public AnalysisCompletedEvent(AnalysisResult analysisResult, boolean hideSubmissionDialog ) {
        this.analysisResult = analysisResult;
        this.hideSubmissionDialog = hideSubmissionDialog;
    }

    @Override
    public Type<AnalysisCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    public boolean getHideSubmissionDialog() {
        return hideSubmissionDialog;
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
