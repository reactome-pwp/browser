package org.reactome.web.pwp.client.tools.analysis.wizard.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.AnalysisWizard;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.NextStepSelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NextStepSelectedEvent extends GwtEvent<NextStepSelectedHandler> {
    public static final Type<NextStepSelectedHandler> TYPE = new Type<>();

    private AnalysisWizard.Step step;

    public NextStepSelectedEvent(AnalysisWizard.Step step) {
        this.step = step;
    }

    @Override
    protected void dispatch(NextStepSelectedHandler handler) {
        handler.onNextStepSelected(this);
    }

    @Override
    public Type<NextStepSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    public AnalysisWizard.Step getStep() {
        return step;
    }

    @Override
    public String toString() {
        return "NextStepSelectedEvent{" +
                "step=" + step +
                '}';
    }
}
