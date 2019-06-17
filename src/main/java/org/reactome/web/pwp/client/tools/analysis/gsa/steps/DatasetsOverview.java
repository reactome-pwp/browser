package org.reactome.web.pwp.client.tools.analysis.gsa.steps;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.SingleSelectionModel;
import org.reactome.web.diagram.search.autocomplete.cells.AutoCompleteCell;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.DatasetType;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.AddDatasetPanel;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.style.GSAStyleFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DatasetsOverview extends AbstractGSAStep {
    private List<DatasetType> availableDatasetTypes;
    private List<String> datasets = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6");

    private CellList<String> datasetsList;
    private SingleSelectionModel<String> datasetsListSelectionModel;

    private IconButton nextBtn;
    private IconButton previousBtn;
    private IconButton addNewDatasetBtn;
    private AddDatasetPanel addDatasetPanel;


    public DatasetsOverview(GSAWizardEventBus wizardEventBus, GSAWizardContext wizardContext) {
        super(wizardEventBus, wizardContext);
        init();
    }

    public void setAvailableDatasetTypes(List<DatasetType> types) {
        this.availableDatasetTypes = types;
        addDatasetPanel.setDatasetTypes(types);
    }


    private void init() {
        FlowPanel container = new FlowPanel();
        container.setStyleName(GSAStyleFactory.getStyle().container());

        SimplePanel title = new SimplePanel();
        title.setStyleName(GSAStyleFactory.getStyle().title());
        title.getElement().setInnerHTML("Step 2: Add and annotate your datasets");
        container.add(title);

        // Create a cell to render each value.
        AutoCompleteCell autoCompleteCell = new AutoCompleteCell();
        datasetsList = new CellList<>(new TextCell());
        datasetsList.setStyleName(GSAStyleFactory.getStyle().datasetsList());
        datasetsList.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);
//        datasetsListSelectionModel = new SingleSelectionModel<>();
//        datasetsList.setSelectionModel(datasetsListSelectionModel);
//        datasetsListSelectionModel.addSelectionChangeHandler(event -> {
//            AutoCompleteResult selected = datasetsListSelectionModel.getSelectedObject();
//            if (selected != null) {
//                fireEvent(new AutoCompleteSelectedEvent(selected.getResult()));
//                makeVisible(false);
//            }
//        });


        // Set the total row count. This isn't strictly necessary, but it affects
        // paging calculations, so its good habit to keep the row count up to date.
        datasetsList.setRowCount(datasets.size(), true);
        datasetsList.setRowData(datasets);

        ScrollPanel datasetsListContainer = new ScrollPanel();
        datasetsListContainer.setStyleName(GSAStyleFactory.getStyle().datasetsListContainer());
        datasetsListContainer.add(datasetsList);
        container.add(datasetsListContainer);

        addNavigationButtons();

        addDatasetPanel = new AddDatasetPanel(wizardEventBus, wizardContext);
        container.add(addDatasetPanel);

        addNewDatasetBtn = new IconButton(
                null,
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
                "Review your submission",
                event -> {
//                    if(selectedMethodItem.validateAllParameters()) {
//                        wizardContext.setMethod(selectedMethodItem.getMethod().getName());
//                        wizardContext.setParameters(selectedMethodItem.getParameterValues());
                    wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.OVERVIEW), this);
//                    }
                });
        nextBtn.setEnabled(false);
        addRightButton(nextBtn);

        previousBtn = new IconButton(
                "Back",
                GSAStyleFactory.RESOURCES.nextIcon(),
                GSAStyleFactory.getStyle().navigationBtn(),
                "Select your preferred analysis method",
                event -> wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.METHODS), this));
        addLeftButton(previousBtn);
    }

    private void rotateAddButton() {
        if (addDatasetPanel.isExpanded()) {
            addNewDatasetBtn.removeStyleName(GSAStyleFactory.getStyle().rotate());
        } else {
            addNewDatasetBtn.addStyleName(GSAStyleFactory.getStyle().rotate());
            addNewDatasetBtn.setTitle("Cancel");
        }
    }

}
