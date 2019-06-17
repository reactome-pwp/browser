package org.reactome.web.pwp.client.tools.analysis.gsa;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.DatasetType;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Method;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.handlers.StepSelectedHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.steps.AnnotateDataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.steps.DatasetsOverview;
import org.reactome.web.pwp.client.tools.analysis.gsa.steps.GSAStep;
import org.reactome.web.pwp.client.tools.analysis.gsa.steps.SelectMethod;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAWizard extends DockLayoutPanel implements StepSelectedHandler {
    private GSAWizardEventBus eventBus = new GSAWizardEventBus();
    private GSAWizardContext context = new GSAWizardContext();

    private SimplePanel top;
    private TabLayoutPanel panels;

//    private List<Method> availableMethods = new ArrayList<>();
    private AnalysisCompletedHandler handler;

    private SelectMethod selectMethod;
    private DatasetsOverview datasetsOverview;
    private AnnotateDataset annotateDataset;

    public GSAWizard(AnalysisCompletedHandler handler) {
        super(Style.Unit.PX);
        this.handler = handler;
        initHandlers();

        initUI();
    }

    public void setAvailableMethods(List<Method> methods) {
        selectMethod.setAvailableMethods(methods);
    }

    public void setAvailableDatasetTypes(List<DatasetType> types) {
        datasetsOverview.setAvailableDatasetTypes(types);
    }

    @Override
    public void onStepSelected(StepSelectedEvent event) {
        Console.info("=============>");
        Console.info(event.toString());
        Console.info(context.toString());
        Console.info("<=============");
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
            case OVERVIEW:
                index = 3;
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
        this.panels.add(selectMethod = new SelectMethod(eventBus, context));
        this.panels.add(datasetsOverview = new DatasetsOverview(eventBus, context));
        this.panels.add(annotateDataset = new AnnotateDataset(eventBus, context));
        this.panels.add(new Label("OVERVIEW..."));
        this.add(panels);
    }

    private void initHandlers(){
//        eventBus.addHandler(AnalysisCompletedEvent.TYPE, this);
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
