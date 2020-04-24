package org.reactome.web.pwp.client.tools.analysis.gsa.steps;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.AnnotationProperty;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.cells.CovariatesCell;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.handlers.StepSelectedHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.style.GSAStyleFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Allows the user to select the properties to be compared
 * during the analysis (group 1 vs group 2) and the covariate factors
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@SuppressWarnings("Duplicates")
public class StatisticalDesignStep extends AbstractGSAStep implements StepSelectedHandler  {
    private static final String NO_VALUE = "-1";

    private IconButton nextBtn;
    private IconButton previousBtn;

    private List<AnnotationProperty> allAnnotations;

    private ListBox comparisonBox;
    private String selectedComparisonFactor = null;

    private FlowPanel groupOnePanel;
    private ListBox groupOneBox;
    private String groupOne = null;

    private FlowPanel groupTwoPanel;
    private ListBox groupTwoBox;
    private String groupTwo = null;

    private FlowPanel covariatesPanel;
    private CellList<AnnotationProperty> covariatesListBox;
    private List<AnnotationProperty> selectedCovariates;

    private GSADataset dataset;

    public StatisticalDesignStep(GSAWizardEventBus wizardEventBus, GSAWizardContext wizardContext) {
        super(wizardEventBus, wizardContext);
        init();
        initHandlers();
    }

    @Override
    public void onStepSelected(StepSelectedEvent event) {
        if (event.getSource().equals(this) || event.getStep() != GSAStep.STATISTICAL_DESIGN)  {
            return;
        }
        updateUI();
    }

    private void init() {
        FlowPanel container = new FlowPanel();
        container.setStyleName(GSAStyleFactory.getStyle().container());

        SimplePanel title = new SimplePanel();
        title.setStyleName(GSAStyleFactory.getStyle().title());
        title.getElement().setInnerHTML("Step 2.2: Statistical design");
        container.add(title);

        Label comparisonLB = new Label("Select comparison factor:");
        comparisonLB.setStyleName(GSAStyleFactory.getStyle().comparisonLB());
        container.add(comparisonLB);
        container.add(getComparisonFactorBox());

        container.add(getGroupOnePanel());
        container.add(getGroupTwoPanel());

        container.add(getCovariatesPanel());

        addNavigationButtons();

        add(new ScrollPanel(container));

    }

    private void initHandlers() {
        wizardEventBus.addHandler(StepSelectedEvent.TYPE, this);
    }

    private Widget getComparisonFactorBox(){
        SimplePanel comparisonPanel = new SimplePanel();
        comparisonPanel.addStyleName(GSAStyleFactory.getStyle().comparisonPanel());
        comparisonBox = new ListBox();
        comparisonBox.setMultipleSelect(false);

        populateComparisonFactorBox();

        comparisonPanel.add(comparisonBox);
        comparisonBox.addChangeHandler(event -> {
            String value = comparisonBox.getValue(comparisonBox.getSelectedIndex());
            selectedComparisonFactor = !value.equals(NO_VALUE) ? comparisonBox.getValue(comparisonBox.getSelectedIndex()) : null;

            groupOne = null;
            groupTwo = null;

            populateGroupOneBox();
            populateGroupTwoBox();
            populateCovariatesListBox();

            nextBtn.setEnabled(enableNextButton());
        });
        return comparisonPanel;
    }

    private void populateComparisonFactorBox() {
        if (dataset == null) return;

        if (comparisonBox.getItemCount() > 0) comparisonBox.clear();

        comparisonBox.addItem(" - Select one - ", NO_VALUE);

        if (allAnnotations != null && !allAnnotations.isEmpty()) {
            for (AnnotationProperty property : allAnnotations) {
                String name = property.getName();
                comparisonBox.addItem(name, name);
            }
            setSelection(comparisonBox, selectedComparisonFactor);
        }
    }

    private void setSelection(ListBox listBox, String selection){
        for (int i = 0; i < listBox.getItemCount(); i++) {
            String value = listBox.getValue(i);
            if (value.equals(selection)){
                listBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private Widget getGroupOnePanel() {
        groupOnePanel = new FlowPanel();
        groupOnePanel.addStyleName(GSAStyleFactory.getStyle().groupOnePanel());

        Label groupOneLB = new Label("Set 1st group:");
        groupOneLB.setStyleName(GSAStyleFactory.getStyle().groupOneLB());
        groupOnePanel.add(groupOneLB);

        groupOneBox = new ListBox();
        groupOneBox.setMultipleSelect(false);

        populateGroupOneBox();

        groupOnePanel.add(groupOneBox);
        groupOneBox.addChangeHandler(event -> {
            String value = groupOneBox.getValue(groupOneBox.getSelectedIndex());
            groupOne = !value.equals(NO_VALUE) ? groupOneBox.getValue(groupOneBox.getSelectedIndex()) : null;
            nextBtn.setEnabled(enableNextButton());
        });
        return groupOnePanel;
    }

    private void populateGroupOneBox() {
        if (dataset == null || selectedComparisonFactor == null) {
            groupOnePanel.setVisible(false);
            groupOneBox.clear();
            groupOne = null;
            return;
        }

        if (groupOneBox.getItemCount() > 0) groupOneBox.clear();

        groupOneBox.addItem(" - Select one - ", NO_VALUE);

        AnnotationProperty annotationProperty = dataset.getAnnotations().findAnnotationPropertyByName(selectedComparisonFactor);
        if (annotationProperty != null) {
            Set<String> values = Arrays.stream(annotationProperty.getValues()).collect(Collectors.toSet());
            for (String annotation : values) {
                if (!annotation.isEmpty()) {
                    groupOneBox.addItem(annotation, annotation);
                }
            }
            groupOnePanel.setVisible(groupOneBox.getItemCount() > 2);
            setSelection(groupOneBox, groupOne);
        }
    }

    private Widget getGroupTwoPanel() {
        groupTwoPanel = new FlowPanel();
        groupTwoPanel.addStyleName(GSAStyleFactory.getStyle().groupTwoPanel());

        Label groupTwoLB = new Label("Set 2nd group:");
        groupTwoLB.setStyleName(GSAStyleFactory.getStyle().groupTwoLB());
        groupTwoPanel.add(groupTwoLB);

        groupTwoBox = new ListBox();
        groupTwoBox.setMultipleSelect(false);

        populateGroupTwoBox();

        groupTwoPanel.add(groupTwoBox);
        groupTwoBox.addChangeHandler(event -> {
            String value = groupTwoBox.getValue(groupTwoBox.getSelectedIndex());
            groupTwo = !value.equals(NO_VALUE) ? groupTwoBox.getValue(groupTwoBox.getSelectedIndex()) : null;
            nextBtn.setEnabled(enableNextButton());
        });
        return groupTwoPanel;
    }

    private void populateGroupTwoBox() {
        if (dataset == null || selectedComparisonFactor == null) {
            groupTwoPanel.setVisible(false);
            groupTwoBox.clear();
            groupTwo = null;
            return;
        }

        if (groupTwoBox.getItemCount() > 0) groupTwoBox.clear();

        groupTwoBox.addItem(" - Select one - ", NO_VALUE);

        AnnotationProperty annotationProperty = dataset.getAnnotations().findAnnotationPropertyByName(selectedComparisonFactor);
        if (annotationProperty != null) {
            Set<String> values = Arrays.stream(annotationProperty.getValues()).collect(Collectors.toSet());
            for (String annotation : values) {
                if (!annotation.isEmpty()) {
                    groupTwoBox.addItem(annotation, annotation);
                }
            }
            groupTwoPanel.setVisible(groupTwoBox.getItemCount() > 2);
            setSelection(groupTwoBox, groupTwo);
        }
    }

    private Widget getCovariatesPanel() {
        covariatesPanel = new FlowPanel();

        Label covariatesLB = new Label("Select covariates:");
        covariatesLB.setStyleName(GSAStyleFactory.getStyle().covariatesLB());
        covariatesPanel.add(covariatesLB);

        CovariatesCell covariatesCell = new CovariatesCell();
        covariatesListBox = new CellList<>(covariatesCell);
        covariatesListBox.setStyleName(GSAStyleFactory.getStyle().covariatesListBox());
        covariatesListBox.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);
        covariatesListBox.setValueUpdater(value -> {
            value.setChecked(!value.isChecked());

            selectedCovariates.clear();
            for (int i=0; i< covariatesListBox.getVisibleItemCount(); i++) {
                AnnotationProperty property = covariatesListBox.getVisibleItem(i);
                if (property.isChecked()) {
                    selectedCovariates.add(property);
                }
            }

            nextBtn.setEnabled(enableNextButton());
        });
        covariatesPanel.add(covariatesListBox);

        return covariatesPanel;
    }

    private void populateCovariatesListBox() {
        List<AnnotationProperty> covariates = new ArrayList<>();
        if (dataset == null) {
            covariatesPanel.setVisible(false);
            covariatesListBox.setRowCount(covariates.size(), true);
            covariatesListBox.setRowData(covariates);
            return;
        }

        for (AnnotationProperty property : allAnnotations) {
            if (!property.getName().equals(selectedComparisonFactor)) {
                property.setChecked(selectedCovariates.contains(property));
                covariates.add(property);
            }
        }

        covariatesListBox.setRowCount(covariates.size(), true);
        covariatesListBox.setRowData(covariates);

        covariatesPanel.setVisible(covariates.size() > 0 && !comparisonBox.getSelectedValue().equals(NO_VALUE));
    }

    private boolean checkGroupBoxes() {
        if (!groupOnePanel.isVisible() && !groupTwoPanel.isVisible()) return true;
        return groupOne != null && groupTwo != null;
    }

    private boolean checkCovariatesBox() {
        return true;
    }

    private boolean enableNextButton() {
        return selectedComparisonFactor != null && checkGroupBoxes() && checkCovariatesBox();
    }

    private void addNavigationButtons() {
        nextBtn = new IconButton(
                "Continue",
                GSAStyleFactory.RESOURCES.nextIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Save annotated dataset",
                event -> {
                    dataset.getAnnotations().setSelectedComparisonFactor(selectedComparisonFactor);
                    dataset.getAnnotations().setGroupOne(groupOne);
                    dataset.getAnnotations().setGroupTwo(groupTwo);
                    dataset.getAnnotations().setCovariates(selectedCovariates);

                    wizardContext.updateOrAddDataset(dataset);
                    wizardContext.setDatasetToAnnotate(null);
                    wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.DATASETS), this);

                });
        nextBtn.setEnabled(false);
        addRightButton(nextBtn);

        previousBtn = new IconButton(
                "Back",
                GSAStyleFactory.RESOURCES.previousIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Go back to dataset annotation",
                event -> wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.ANNOTATE_DATASET), this));
        addLeftButton(previousBtn);
    }

    private void updateUI() {
        dataset = wizardContext.getDatasetToAnnotate();
        allAnnotations = dataset.getAnnotations().getAllAnnotations();
        selectedComparisonFactor = dataset.getAnnotations().getSelectedComparisonFactor();
        groupOne = dataset.getAnnotations().getGroupOne();
        groupTwo = dataset.getAnnotations().getGroupTwo();
        selectedCovariates = dataset.getAnnotations().getCovariates();

        populateComparisonFactorBox();
        populateGroupOneBox();
        populateGroupTwoBox();
        populateCovariatesListBox();

        nextBtn.setEnabled(enableNextButton());

    }

}
