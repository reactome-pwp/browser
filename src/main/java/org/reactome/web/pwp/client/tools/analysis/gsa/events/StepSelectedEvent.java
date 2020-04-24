package org.reactome.web.pwp.client.tools.analysis.gsa.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.handlers.StepSelectedHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.steps.GSAStep;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class StepSelectedEvent extends GwtEvent<StepSelectedHandler> {
    public static final Type<StepSelectedHandler> TYPE = new Type<>();

    private GSAStep step;

    public StepSelectedEvent(GSAStep step) {
        this.step = step;
    }

    @Override
    protected void dispatch(StepSelectedHandler handler) {
        handler.onStepSelected(this);
    }

    @Override
    public Type<StepSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    public GSAStep getStep() {
        return step;
    }

    @Override
    public String toString() {
        return "StepSelectedEvent{" +
                "step=" + step +
                '}';
    }
}
