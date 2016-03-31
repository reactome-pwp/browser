package org.reactome.web.pwp.client.tools.analysis.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;
import org.reactome.web.pwp.client.tools.analysis.wizard.common.WizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.wizard.common.WizardSelection;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.NextStepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.NextStepSelectedHandler;
import org.reactome.web.pwp.client.tools.analysis.wizard.steps.Analysis;
import org.reactome.web.pwp.client.tools.analysis.wizard.steps.Options;
import org.reactome.web.pwp.client.tools.analysis.wizard.steps.UserSample;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisWizard extends DockLayoutPanel implements NextStepSelectedHandler, AnalysisCompletedHandler {

    public enum Step {
        SAMPLE, OPTIONS, ANALYSIS;

        public int getIndex() {
            for (int i = 0; i < values().length; i++)
                if(values()[i].equals(this)) return i;
            return 0;
        }

    }

    private WizardEventBus wizardEventBus = new WizardEventBus();
    private WizardSelection wizardSelection = new WizardSelection();
    private TabLayoutPanel panels;

    private AnalysisCompletedHandler handler;

    public AnalysisWizard(AnalysisCompletedHandler handler) {
        super(Style.Unit.PX);
        this.handler = handler;
        initHandlers();

        FlowPanel optionsSelector = new FlowPanel();
        optionsSelector.add(getButton("Your Sample", Step.SAMPLE));
        optionsSelector.add(getButton("Options", Step.OPTIONS));
        optionsSelector.add(getButton("Analysis", Step.ANALYSIS));
        this.addNorth(optionsSelector, 50);

        this.panels = new TabLayoutPanel(0, Style.Unit.PX);
        this.panels.setAnimationDuration(250);
        this.panels.add(new UserSample(wizardEventBus, wizardSelection));
        this.panels.add(new Options(wizardEventBus, wizardSelection));
        this.panels.add(new Analysis(wizardEventBus, wizardSelection));
        this.add(panels);
    }

    @Override
    public void onNextStepSelected(NextStepSelectedEvent event) {
        select(event.getStep());
    }


    @Override
    public void onAnalysisCompleted(AnalysisCompletedEvent event) {
        handler.onAnalysisCompleted(event);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                select(Step.SAMPLE);
            }
        });
    }


    public void select(Step step) {
        panels.selectTab(step.getIndex());
    }

    private void initHandlers(){
        wizardEventBus.addHandler(AnalysisCompletedEvent.TYPE, this);
        wizardEventBus.addHandler(NextStepSelectedEvent.TYPE, this);
    }

    private Button getButton(String text, final Step step) {
        return new Button(text, new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                select(step);
            }
        });
    }


    public interface UserSampleResource extends ClientBundle {

        UserSampleResource INSTANCE = GWT.create(UserSampleResource.class);

        @Source("AnalysisInfo.html")
        TextResource analysisInfo();

        @Source("AnalysisExamples.html")
        TextResource analysisExamples();

        @Source("SpeciesComparisonInfo.html")
        TextResource speciesComparisonInfo();
    }
}
