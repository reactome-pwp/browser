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
    boolean gsaIncludeDisease;

    public AnalysisCompletedEvent(AnalysisResult analysisResult) {
        this(analysisResult, true, true);
    }

    public AnalysisCompletedEvent(AnalysisResult analysisResult, boolean gsaIncludeDisease) {
        this(analysisResult, true, gsaIncludeDisease);
    }

    public AnalysisCompletedEvent(AnalysisResult analysisResult, boolean hideSubmissionDialog, boolean gsaIncludeDisease) {
        this.analysisResult = analysisResult;
        this.hideSubmissionDialog = hideSubmissionDialog;
        this.gsaIncludeDisease = gsaIncludeDisease;
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

    public boolean isGsaIncludeDisease() {
        return gsaIncludeDisease;
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
