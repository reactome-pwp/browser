package org.reactome.web.pwp.client.tools.analysis.gsa.steps;

import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.AnnotationsPanel;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.handlers.StepSelectedHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.style.GSAStyleFactory;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnnotateDataset extends AbstractGSAStep implements StepSelectedHandler {

    private IconButton nextBtn;
    private IconButton previousBtn;
    private TextBox nameTB;
    private SimplePanel annotationsPlaceholder;
    private AnnotationsPanel annotationsPanel;

    private GSADataset dataset;

    public AnnotateDataset(GSAWizardEventBus wizardEventBus, GSAWizardContext wizardContext) {
        super(wizardEventBus, wizardContext);
        init();
        initHandlers();
    }

    @Override
    public void onStepSelected(StepSelectedEvent event) {
        if (event.getSource().equals(this) || event.getStep() != GSAStep.ANNOTATE_DATASET)  {
            return;
        }
        update();
    }

    private void init() {
        FlowPanel container = new FlowPanel();
        container.setStyleName(GSAStyleFactory.getStyle().container());

        SimplePanel title = new SimplePanel();
        title.setStyleName(GSAStyleFactory.getStyle().title());
        title.getElement().setInnerHTML("Step 2.1: Annotate your dataset");
        container.add(title);

        Label nameLB = new Label("Dataset name:");
        nameLB.setStyleName(GSAStyleFactory.getStyle().nameLabel());
        container.add(nameLB);

        nameTB = new TextBox();
        nameTB.getElement().setPropertyString("placeholder", "Enter a unique name");
        nameTB.setStyleName(GSAStyleFactory.getStyle().nameTextBox());
        container.add(nameTB);

        annotationsPlaceholder = new SimplePanel();
        annotationsPlaceholder.setStyleName(GSAStyleFactory.getStyle().annotationsPlaceholder());
        container.add(annotationsPlaceholder);

        addNavigationButtons();

        add(new ScrollPanel(container));

    }

    private void initHandlers() {
        wizardEventBus.addHandler(StepSelectedEvent.TYPE, this);
    }

    private void addNavigationButtons() {
        nextBtn = new IconButton(
                "Continue",
                GSAStyleFactory.RESOURCES.nextIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Save annotated dataset",
                event -> {
                    wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.DATASETS), this);
//                    }
                });
        nextBtn.setEnabled(false);
        addRightButton(nextBtn);

        previousBtn = new IconButton(
                "Cancel",
                GSAStyleFactory.RESOURCES.nextIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Abort dataset annotation and go back",
                event -> wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.DATASETS), this));
        addLeftButton(previousBtn);
    }

    private void update() {
        dataset = wizardContext.getDatasetToAnnotate();
        Console.info("Updating for ...." + dataset);
        annotationsPanel = new AnnotationsPanel(dataset);
        annotationsPlaceholder.clear();
        annotationsPlaceholder.add(annotationsPanel);
    }

}
