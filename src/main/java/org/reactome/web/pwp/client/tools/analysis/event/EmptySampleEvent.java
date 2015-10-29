package org.reactome.web.pwp.client.tools.analysis.event;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisError;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisErrorHandler;
import org.reactome.web.pwp.client.tools.analysis.handler.EmptySampleHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EmptySampleEvent extends GwtEvent<EmptySampleHandler> {
    public static Type<EmptySampleHandler> TYPE = new Type<>();

    @Override
    public Type<EmptySampleHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EmptySampleHandler handler) {
        handler.onEmptySample(this);
    }

    @Override
    public String toString() {
        return "EmptySampleEvent{}";
    }
}
