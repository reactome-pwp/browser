package org.reactome.web.pwp.client.tools.analysis.gsa;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.DatasetType;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Method;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.handlers.StepSelectedHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.steps.*;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAWizard extends DockLayoutPanel implements StepSelectedHandler, AnalysisCompletedHandler {
    private GSAWizardEventBus eventBus = new GSAWizardEventBus();
    private GSAWizardContext context = new GSAWizardContext();

    private SimplePanel top;
    private TabLayoutPanel panels;

    private AnalysisCompletedHandler handler;

    private SelectMethodStep selectMethodStep;
    private DatasetsOverviewStep datasetsOverviewStep;
    private AnnotateDatasetStep annotateDatasetStep;
    private StatisticalDesignStep statisticalDesignStep;
    private OptionsStep optionsStep;
    private AnalysisStep analysisStep;

    public GSAWizard(AnalysisCompletedHandler handler) {
        super(Style.Unit.PX);
        this.handler = handler;
        initHandlers();

        initUI();
    }

    @Override
    public void onAnalysisCompleted(AnalysisCompletedEvent event) {
        handler.onAnalysisCompleted(event);
//        Scheduler.get().scheduleDeferred(() -> showStep(GSAStep.METHODS));
    }

    public void setAvailableMethods(List<Method> methods) {
        selectMethodStep.setAvailableMethods(methods);
    }

    public void setAvailableDatasetTypes(List<DatasetType> types) {
        datasetsOverviewStep.setAvailableDatasetTypes(types);
    }

    @Override
    public void onStepSelected(StepSelectedEvent event) {
        showStep(event.getStep());
    }

    public void showStep(GSAStep step) {
        int index = 0;
        top.clear();
        switch (step){
            case METHODS:
                top.add(new Image(RESOURCES.step01()));
                break;
            case DATASETS:
                index = 1;
                top.add(new Image(RESOURCES.step02()));
                break;
            case ANNOTATE_DATASET:
                index = 2;
                top.add(new Image(RESOURCES.step02()));
                break;
            case STATISTICAL_DESIGN:
                index = 3;
                top.add(new Image(RESOURCES.step02()));
                break;
            case OPTIONS:
                index = 4;
                top.add(new Image(RESOURCES.step02()));
                break;
            case ANALYSIS:
                index = 5;
                top.add(new Image(RESOURCES.step02()));
                break;

        }
        panels.selectTab(index);
    }

    private void initUI() {
        top = new SimplePanel();
        top.addStyleName(AnalysisStyleFactory.getAnalysisStyle().wizardTop());
        top.add(new Image(RESOURCES.step01()));
        this.addNorth(top, 55);

        this.panels = new TabLayoutPanel(0, Style.Unit.PX);
        this.panels.setAnimationDuration(250);
        this.panels.add(selectMethodStep = new SelectMethodStep(eventBus, context));
        this.panels.add(datasetsOverviewStep = new DatasetsOverviewStep(eventBus, context));
        this.panels.add(annotateDatasetStep = new AnnotateDatasetStep(eventBus, context));
        this.panels.add(statisticalDesignStep = new StatisticalDesignStep(eventBus, context));
        this.panels.add(optionsStep = new OptionsStep(eventBus, context));
        this.panels.add(analysisStep = new AnalysisStep(eventBus, context));
        this.add(panels);
    }

    private void initHandlers(){
        eventBus.addHandler(AnalysisCompletedEvent.TYPE, this);
        eventBus.addHandler(StepSelectedEvent.TYPE, this);
    }

    public static GSAResource RESOURCES = GWT.create(GSAResource.class);
    public interface GSAResource extends ClientBundle {

        @Source("images/step1.png")
        ImageResource step01();

        @Source("images/step2.png")
        ImageResource step02();

        @Source("images/step1.png")
        ImageResource step03();
    }

}
