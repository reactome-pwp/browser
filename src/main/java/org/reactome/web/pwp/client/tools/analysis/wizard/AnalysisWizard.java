package org.reactome.web.pwp.client.tools.analysis.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;
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

    public enum Step {SAMPLE, OPTIONS, ANALYSIS}

    private WizardEventBus wizardEventBus = new WizardEventBus();
    private WizardSelection wizardSelection = new WizardSelection();

    private SimplePanel top;
    private TabLayoutPanel panels;

    private AnalysisCompletedHandler handler;

    public AnalysisWizard(AnalysisCompletedHandler handler) {
        super(Style.Unit.PX);
        this.handler = handler;
        initHandlers();

        top = new SimplePanel();
        top.addStyleName(AnalysisStyleFactory.getAnalysisStyle().wizardTop());
        top.add(new Image(RESOURCES.wizardTopStep01()));
        this.addNorth(top, 55);

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
        int index = 0;
        top.clear();
        switch (step){
            case SAMPLE:
                top.add(new Image(RESOURCES.wizardTopStep01()));
                break;
            case OPTIONS:
                index = 1;
                top.add(new Image(RESOURCES.wizardTopStep02()));
                break;
            case ANALYSIS:
                index = 2;
                top.add(new Image(RESOURCES.wizardTopStep03()));
                break;
        }
        panels.selectTab(index);
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


    public static UserSampleResource RESOURCES = GWT.create(UserSampleResource.class);
    public interface UserSampleResource extends ClientBundle {

        @Source("SpeciesComparisonInfo.html")
        TextResource speciesComparisonInfo();

        @Source("TissueDistributionInfo.html")
        TextResource tissueDistributionInfo();

        @Source("top/wizard_top_step1.png")
        ImageResource wizardTopStep01();

        @Source("top/wizard_top_step2.png")
        ImageResource wizardTopStep02();

        @Source("top/wizard_top_step3.png")
        ImageResource wizardTopStep03();
    }
}
