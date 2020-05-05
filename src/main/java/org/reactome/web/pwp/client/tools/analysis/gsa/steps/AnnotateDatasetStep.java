package org.reactome.web.pwp.client.tools.analysis.gsa.steps;

import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.AnnotationProperty;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.Annotations;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.AnnotationsPanel;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.handlers.StepSelectedHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.style.GSAStyleFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Allows the user to annotate the selected dataset through a table-like widget
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnnotateDatasetStep extends AbstractGSAStep implements StepSelectedHandler, AnnotationsPanel.Handler {

    private IconButton nextBtn;
    private IconButton previousBtn;
    private TextBox nameTB;
    private SimplePanel annotationsPlaceholder;
    private AnnotationsPanel annotationsPanel;

    private boolean enableNextBtn = false;

    private GSADataset dataset;

    public AnnotateDatasetStep(GSAWizardEventBus wizardEventBus, GSAWizardContext wizardContext) {
        super(wizardEventBus, wizardContext);
        init();
        initHandlers();
    }

    @Override
    public void onStepSelected(StepSelectedEvent event) {
        if (event.getSource().equals(this) || event.getStep() != GSAStep.ANNOTATE_DATASET)  {
            return;
        }
        updateUI();
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

        FlowPanel infoPanel = new FlowPanel();
        infoPanel.setStyleName(GSAStyleFactory.getStyle().annotationInfoPanel());
        infoPanel.add(new HTML(GSAStyleFactory.RESOURCES.annotationInfo().getText()));
        container.add(infoPanel);

        annotationsPlaceholder = new SimplePanel();
        annotationsPlaceholder.setStyleName(GSAStyleFactory.getStyle().annotationsPlaceholder());
        container.add(annotationsPlaceholder);

        addNavigationButtons();

        add(new ScrollPanel(container));

    }

    private void initHandlers() {
        wizardEventBus.addHandler(StepSelectedEvent.TYPE, this);
    }

    @Override
    public void onAnnotationPropertyChanged(Annotations a) {
        Set<Boolean> ret = new HashSet<>(2);
        List<AnnotationProperty> annProps = a.getAllAnnotations();
        for (AnnotationProperty annProp : annProps) {
            Set<String> uniqueValues = new HashSet<>(Arrays.asList(annProp.getValues()));
            uniqueValues.removeAll(Arrays.asList("", null));
            ret.add(uniqueValues.size() >= 2);
        }

        enableNextBtn = ret.contains(true);
        nextBtn.setEnabled(enableNextBtn);
    }

    private void addNavigationButtons() {
        nextBtn = new IconButton(
                "Continue",
                GSAStyleFactory.RESOURCES.nextIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Set statistical comparison details",
                event -> {

                    //TODO update the dataset/name
                    if (!nameTB.getText().isEmpty()) {
                        dataset.setName(nameTB.getText());
                    }

                    wizardContext.setDatasetToAnnotate(dataset);
                    wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.STATISTICAL_DESIGN), this);
                });
        nextBtn.setEnabled(enableNextBtn);
        addRightButton(nextBtn);

        previousBtn = new IconButton(
                "Cancel",
                GSAStyleFactory.RESOURCES.previousIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Abort dataset annotation and go back",
                event -> wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.DATASETS), this));
        addLeftButton(previousBtn);
    }

    private void updateUI() {
        dataset = wizardContext.getDatasetToAnnotate();
        nameTB.setText(dataset.getName());
        annotationsPanel = new AnnotationsPanel(dataset, this);
        annotationsPlaceholder.clear();
        annotationsPlaceholder.add(annotationsPanel);
    }

}
