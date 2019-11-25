package org.reactome.web.pwp.client.tools.analysis.gsa.steps;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.NoSelectionModel;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.DatasetType;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.ExampleDataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.cells.DatasetCell;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.AddDatasetPanel;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.handlers.StepSelectedHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.style.GSAStyleFactory;

import java.util.List;

/**
 * Allows the users to add new a dataset (from the acceptable dataset type)
 * and presents a list of the previously annotated ones
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DatasetsOverviewStep extends AbstractGSAStep implements StepSelectedHandler {
    private CellList<GSADataset> datasetsList;
    private NoSelectionModel<GSADataset> datasetsListSelectionModel;

    private IconButton nextBtn;
    private IconButton previousBtn;
    private IconButton addNewDatasetBtn;
    private AddDatasetPanel addDatasetPanel;


    public DatasetsOverviewStep(GSAWizardEventBus wizardEventBus, GSAWizardContext wizardContext) {
        super(wizardEventBus, wizardContext);
        init();
        initHandlers();
    }

    public void setAvailableDatasetTypes(List<DatasetType> types) {
        addDatasetPanel.setDatasetTypes(types);
    }

    public void setExampleDatasets(List<ExampleDataset> exampleDatasets) {
        addDatasetPanel.setExampleDatasets(exampleDatasets);
    }

    @Override
    public void onStepSelected(StepSelectedEvent event) {
        if (event.getStep() == GSAStep.ANNOTATE_DATASET) {
            if (addDatasetPanel.isExpanded()) {
                rotateAddButton();
                addDatasetPanel.expandCollapse();
                addDatasetPanel.showFileInfoPanel(false);
                addDatasetPanel.showExampleInfoPanel(false);
                addDatasetPanel.showRemoteDatasetInfoPanel(false);
            }
        } else if (event.getStep() == GSAStep.DATASETS) {
            updateUI();
        }
    }

    private void init() {
        FlowPanel container = new FlowPanel();
        container.setStyleName(GSAStyleFactory.getStyle().container());

        SimplePanel title = new SimplePanel();
        title.setStyleName(GSAStyleFactory.getStyle().title());
        title.getElement().setInnerHTML("Step 2: Add and annotate your datasets");
        container.add(title);

        // Create a cell to render each value.
        DatasetCell datasetCell = new DatasetCell();
        datasetsList = new CellList<>(datasetCell);
        datasetsList.setStyleName(GSAStyleFactory.getStyle().datasetsList());
        datasetsList.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);
        datasetsListSelectionModel = new NoSelectionModel<>();
        datasetsList.setSelectionModel(datasetsListSelectionModel);

        Label emptyListLabel = new Label("No dataset uploaded. Please upload at least one dataset.");
        emptyListLabel.setStyleName(GSAStyleFactory.getStyle().emptyListLabel());
        datasetsList.setEmptyListWidget(emptyListLabel);
        datasetsList.addCellPreviewHandler(event -> {
            if(event.getNativeEvent().getType().equals("click")) {
                Element el = Element.as(event.getNativeEvent().getEventTarget());
                String className = el.getParentElement().getClassName();
                if (className != null) {
                    if (className.equalsIgnoreCase("deleteIcon")) {
                        // Handle the delete icon being clicked
                        wizardContext.removeDatasetByIndex(event.getIndex());
                        updateUI();
                    } else if (className.equalsIgnoreCase("editIcon")) {
                        // Select the clicked item to be edited
                        GSADataset selected = datasetsList.getVisibleItem(event.getIndex());
                        wizardContext.setDatasetToAnnotate(selected);
                        wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.ANNOTATE_DATASET), this);
                    }
                }
            }
        });


        // Set the total row count. This isn't strictly necessary, but it affects
        // paging calculations, so its good habit to keep the row count up to date.
        datasetsList.setRowCount(wizardContext.sizeOfAnnotatedDatasets(), true);
        datasetsList.setRowData(wizardContext.getAnnotatedDatasets());

        ScrollPanel datasetsListContainer = new ScrollPanel();
        datasetsListContainer.setStyleName(GSAStyleFactory.getStyle().datasetsListContainer());
        datasetsListContainer.add(datasetsList);
        container.add(datasetsListContainer);

        addNavigationButtons();

        addDatasetPanel = new AddDatasetPanel(wizardEventBus, wizardContext);
        container.add(addDatasetPanel);

        addNewDatasetBtn = new IconButton(
                "Add Dataset",
                GSAStyleFactory.RESOURCES.addIcon(),
                GSAStyleFactory.getStyle().addNewDatasetBtn(),
                "Add a new dataset",
                event -> {
                    rotateAddButton();
                    addDatasetPanel.expandCollapse();
                });
        container.add(addNewDatasetBtn);

        add(new ScrollPanel(container));

    }

    private void addNavigationButtons() {
        nextBtn = new IconButton(
                "Continue",
                GSAStyleFactory.RESOURCES.nextIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Set the analysis options",
                event -> wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.OPTIONS), this));
        nextBtn.setEnabled(false);
        addRightButton(nextBtn);

        previousBtn = new IconButton(
                "Back",
                GSAStyleFactory.RESOURCES.previousIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Select your preferred analysis method",
                event -> wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.METHODS), this));
        addLeftButton(previousBtn);
    }

    private void initHandlers() {
        wizardEventBus.addHandler(StepSelectedEvent.TYPE, this);
    }

    private void rotateAddButton() {
        if (addDatasetPanel.isExpanded()) {
            addNewDatasetBtn.getImage().removeStyleName(GSAStyleFactory.getStyle().rotate());
            addNewDatasetBtn.setText("Add dataset");
            addNewDatasetBtn.setTitle("Add a new dataset");
        } else {
            //addNewDatasetBtn.addStyleName(GSAStyleFactory.getStyle().rotate());
            addNewDatasetBtn.getImage().setStyleName(GSAStyleFactory.getStyle().rotate());
            addNewDatasetBtn.setText("Cancel");
            addNewDatasetBtn.setTitle("Cancel");
        }
    }

    private void updateUI() {
        // Set the total row count. This isn't strictly necessary, but it affects
        // paging calculations, so its good habit to keep the row count up to date.
        datasetsList.setRowCount(wizardContext.sizeOfAnnotatedDatasets(), true);
        datasetsList.setRowData(wizardContext.getAnnotatedDatasets());

        nextBtn.setEnabled(wizardContext.sizeOfAnnotatedDatasets() > 0);
    }

}
