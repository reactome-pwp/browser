package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.GSAClient;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.GSAClientHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.ExternalDataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAException;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAFactory;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.*;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.steps.GSAStep;

import java.util.List;

/**
 * Uses a FormUpload widget to upload the dataset to the gsa server.
 *
 * NOTE: Please keep in mind that to avoid Cross-Origin restrictions applied by most browsers
 * we proxy the gsa backend (gsa.reactome.org) through our servers at Apache level.
 *
 * Examples and External Datasources come from the different endpoint, however, when posting data
 * they are using the same endpoint.
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@SuppressWarnings("Duplicates")
public class AddDatasetPanel extends FlowPanel implements ChangeHandler,
        FormPanel.SubmitHandler, FormPanel.SubmitCompleteHandler, ExternalDatasourceItem.Handler {
    private static final String URL_UPLOAD_FILE = "/GSAServer/upload";
    private static final int UPLOAD_POLLING_PERIOD = 2000;

    private static final String UPLOADING_MSG = "Please wait while uploading file ...";
    private static final String UPLOAD_SUCCESS_MSG = "File uploaded successfully";
    private static final String UPLOAD_FAILED_MSG = "File upload failed. Please try again.";

    private static final String DATASET_QUERYING_MSG = "Please wait while querying dataset ...";
    private static final String DATASET_SUCCESS_MSG = "Dataset queried successfully";
    private static final String DATASET_FAILED_MSG = "Dataset query failed. Please try again.";

    private static final String EXAMPLE_DATASET_RESOURCE_ID = "example_datasets";

    private GSAWizardEventBus wizardEventBus;
    private GSAWizardContext wizardContext;

    private List<DatasetType> datasetTypes;
    private List<ExampleDataset> exampleDatasets;
    private List<ExternalDatasource> externalDatasources;

    private boolean isExpanded;
    private boolean uploadInProgress;
    private DatasetType selectedType;

    private FlowPanel itemsPanel;
    private FormPanel form;
    private FileUpload fileUpload;

    private Label orLabel;

    private Widget fileInfoPanel;
    private Image fileInfoIcon;
    private Label fileInfoMessage;

    private FlowPanel examplesPanel;

    private Widget exampleInfoPanel;
    private Image exampleInfoIcon;
    private Label exampleInfoMessage;

    private FlowPanel datasetPanel;

    private Widget datasetInfoPanel;
    private Image datasetInfoIcon;
    private Label datasetInfoMessage;

    public AddDatasetPanel(GSAWizardEventBus wizardEventBus, GSAWizardContext wizardContext) {
        this.wizardEventBus = wizardEventBus;
        this.wizardContext = wizardContext;
        init();
    }

    public void setDatasetTypes(List<DatasetType> types) {
        this.datasetTypes = types;
        getTypeWidgets();
    }

    public void setExampleDatasets(List<ExampleDataset> exampleDatasets) {
        this.exampleDatasets = exampleDatasets;
        getExampleWidgets();
    }

    public void setExternalDatasources(List<ExternalDatasource> externalDatasources) {
        this.externalDatasources = externalDatasources;
        getExternalDatasourcesWidget();
    }

    @Override
    public void onOptionsExpanded(ExternalDatasourceItem source) {
        //Collapse all of them first
        datasetPanel.forEach(widget -> ((ExternalDatasourceItem)widget).collapse());
    }

    @Override
    public void onOptionSubmitted(ExternalDatasourceItem externalDatasourceItem) {
        if(externalDatasourceItem.validateAllParameters()) {
            updateDatasetInfo(RESOURCES.uploadSpinnerIcon(), DATASET_QUERYING_MSG);
            showDatasetInfoPanel(true);
            submitDataset(externalDatasourceItem);
        }
    }

    @Override
    public void onChange(ChangeEvent event) {
        if (fileUpload.getFilename() != null && !fileUpload.getFilename().isEmpty()) {
            form.submit();
        }
    }

    @Override
    public void onSubmit(FormPanel.SubmitEvent event) {
        uploadInProgress = true;
        updateFileInfo(RESOURCES.uploadSpinnerIcon(), UPLOADING_MSG);
        showFileInfoPanel(true);
    }

    @Override
    public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
        uploadInProgress = false;
        showFileInfoPanel(false);
        String results = event.getResults();
        if (results != null) {
            String json = results.replaceAll("\\<[^>]*>", "");
            try {
                if (results.contains("\"status\":")) {
                    // This is an indication that a server error has occurred
                    GSAError error = GSAFactory.getModelObject(GSAError.class, json);
                    Console.info(error.getStatus() + " -> " + error.getTitle());
                    form.reset();
                    updateFileInfo(RESOURCES.errorIcon(), UPLOAD_FAILED_MSG);
                    showFileInfoPanel(true);
                } else {
                    // Create a new dataset and store it in the context
                    UploadResult result = GSAFactory.getModelObject(UploadResult.class, json);
                    String defaultName = selectedType.getType() + "_" + (wizardContext.sizeOfAnnotatedDatasets() + 1);
                    GSADataset dataset = GSADataset.create(selectedType.getType(), selectedType.getName(), fileUpload.getFilename(), result, defaultName.toUpperCase());
                    wizardContext.setDatasetToAnnotate(dataset);
                    updateFileInfo(RESOURCES.successIcon(), UPLOAD_SUCCESS_MSG);
                    showFileInfoPanel(true);
                    Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
                        @Override
                        public boolean execute() {
                            wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.ANNOTATE_DATASET), this);
                            form.reset();
                            return false;
                        }
                    }, 700);

                }
            } catch (GSAException ex) {
                Console.info("Exception: " + ex.getMessage());
                form.reset();
            }
        } else {
            updateFileInfo(RESOURCES.errorIcon(), "No response received from server after file upload");
            Console.info("Error: No response received from server after file upload");
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void expandCollapse() {
        if (isExpanded) {
            removeStyleName(RESOURCES.getCSS().expanded());
        } else {
            addStyleName(RESOURCES.getCSS().expanded());
        }
        isExpanded = !isExpanded;
    }

    public void showFileInfoPanel(boolean visible) {
        fileInfoPanel.setVisible(visible);
    }

    public void showExampleInfoPanel(boolean visible) {
        exampleInfoPanel.setVisible(visible);
    }

    public void showDatasetInfoPanel(boolean visible) {
        datasetInfoPanel.setVisible(visible);
    }

    private void init() {
        setStyleName(RESOURCES.getCSS().main());
        add(getLocalCategoryPanel());
        add(orLabel = new Label("OR"));
        add(getExamplesPanel());
        add(new Label("OR"));
        add(getDatasetPanel());

        //initialise the file submission form
        form = new FormPanel();
        form.setVisible(false);
        form.setMethod(FormPanel.METHOD_POST);
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setAction(URL_UPLOAD_FILE);
        form.addSubmitHandler(this);
        form.addSubmitCompleteHandler(this);

        fileUpload = new FileUpload();
        fileUpload.setVisible(false);
        fileUpload.setName("file");
        fileUpload.setTitle("Select your dataset");
        fileUpload.addChangeHandler(this);
        form.add(fileUpload);
        add(form);
    }

    private Widget getLocalCategoryPanel() {
        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().categoryPanel());

        Image icon = new Image(RESOURCES.folderIcon());
        icon.setStyleName(RESOURCES.getCSS().icon());
        Label title = new Label("Select a file from a local folder");
        title.setStyleName(RESOURCES.getCSS().title());
        FlowPanel header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().header());
        header.add(icon);
        header.add(title);

        itemsPanel = new FlowPanel();
        itemsPanel.setStyleName(RESOURCES.getCSS().itemsPanel());

        rtn.add(header);
        rtn.add(itemsPanel);
        rtn.add(fileInfoPanel = getFileInfoPanel());
        fileInfoPanel.setVisible(false);

        return rtn;
    }

    private void getTypeWidgets() {
        itemsPanel.clear();

        for (DatasetType type : datasetTypes) {
            Label name = new Label(type.getName());
            name.setStyleName(RESOURCES.getCSS().typeName());

            HTMLPanel description = new HTMLPanel(type.getDescription());
            description.setStyleName(RESOURCES.getCSS().typeDescription());

            FlowPanel content = new FlowPanel();
            content.setStyleName(RESOURCES.getCSS().typeContent());
            content.add(name);
            content.add(description);

            FocusPanel typePanel = new FocusPanel();
            typePanel.getElement().setId(type.getType());
            typePanel.getElement().getStyle().setBackgroundColor(type.getColour());
            typePanel.addStyleName(RESOURCES.getCSS().typePanel());
            typePanel.addClickHandler(event -> {
                if (uploadInProgress) return;

                showFileInfoPanel(false);
                selectedType = type;
                fileUpload.click();
            });
            typePanel.add(content);

            itemsPanel.add(typePanel);
        }
    }

    private Widget getFileInfoPanel() {
        fileInfoIcon = new Image(RESOURCES.binIcon());
        fileInfoIcon.setStyleName(RESOURCES.getCSS().infoIcon());

        fileInfoMessage = new Label();
        fileInfoMessage.setStyleName(RESOURCES.getCSS().infoMessage());

        FlowPanel infoPanel = new FlowPanel();
        infoPanel.setStyleName(RESOURCES.getCSS().infoPanel());
        infoPanel.add(fileInfoIcon);
        infoPanel.add(fileInfoMessage);

        return infoPanel;
    }

    private void updateFileInfo(ImageResource icon, String message) {
        fileInfoIcon.setResource(icon);
        fileInfoMessage.setText(message);
    }

    private void updateExampleInfo(ImageResource icon, String message) {
        exampleInfoIcon.setResource(icon);
        exampleInfoMessage.setText(message);
    }

    private void updateDatasetInfo(ImageResource icon, String message) {
        datasetInfoIcon.setResource(icon);
        datasetInfoMessage.setText(message);
    }

    private Widget getExamplesPanel() {
        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().categoryPanel());

        Image icon = new Image(RESOURCES.folderIcon());
        icon.setStyleName(RESOURCES.getCSS().icon());
        Label title = new Label("Select one example dataset");
        title.setStyleName(RESOURCES.getCSS().title());
        FlowPanel header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().header());
        header.add(icon);
        header.add(title);

        examplesPanel = new FlowPanel();
        examplesPanel.setStyleName(RESOURCES.getCSS().itemsPanel());

        rtn.add(header);
        rtn.add(examplesPanel);
        rtn.add(exampleInfoPanel = getExampleInfoPanel());
        exampleInfoPanel.setVisible(false);

        rtn.setVisible(true);

        return rtn;
    }

    private Widget getExampleInfoPanel() {
        exampleInfoIcon = new Image(RESOURCES.binIcon());
        exampleInfoIcon.setStyleName(RESOURCES.getCSS().infoIcon());

        exampleInfoMessage = new Label();
        exampleInfoMessage.setStyleName(RESOURCES.getCSS().infoMessage());

        FlowPanel infoPanel = new FlowPanel();
        infoPanel.setStyleName(RESOURCES.getCSS().infoPanel());
        infoPanel.add(exampleInfoIcon);
        infoPanel.add(exampleInfoMessage);

        return infoPanel;
    }

    private void getExampleWidgets() {
        examplesPanel.clear();

        examplesPanel.getParent().setVisible(!exampleDatasets.isEmpty());
        orLabel.setVisible(!exampleDatasets.isEmpty());

        for (ExampleDataset example : exampleDatasets) {
            Label name = new Label(example.getTitle());
            name.setStyleName(RESOURCES.getCSS().typeName());

            HTMLPanel description = new HTMLPanel(example.getDescription());
            description.setStyleName(RESOURCES.getCSS().typeDescription());

            FlowPanel content = new FlowPanel();
            content.setStyleName(RESOURCES.getCSS().typeContent());
            content.add(name);
            content.add(description);

            FocusPanel aPanel = new FocusPanel();
            aPanel.getElement().setId(example.getType());
            aPanel.getElement().getStyle().setBackgroundColor(example.getColour());
            aPanel.addStyleName(RESOURCES.getCSS().typePanel());
            aPanel.addClickHandler(event -> {
                if (uploadInProgress) return;
                showExampleInfoPanel(false);
                submitExample(example);
            });
            aPanel.add(content);

            examplesPanel.add(aPanel);
        }
    }

    private void getExternalDatasourcesWidget() {
        datasetPanel.clear();
        datasetPanel.getParent().setVisible(!externalDatasources.isEmpty());
        orLabel.setVisible(!externalDatasources.isEmpty());

        for (ExternalDatasource externalDatasource : externalDatasources) {
            if (!externalDatasource.getId().contains(EXAMPLE_DATASET_RESOURCE_ID)) { // examples are loaded in getExamples()
                datasetPanel.add(new ExternalDatasourceItem(externalDatasource, this));
            }
        }
    }

    private Widget getDatasetPanel() {
        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().datasetPanel());

        Image icon = new Image(RESOURCES.folderIcon());
        icon.setStyleName(RESOURCES.getCSS().icon());
        Label title = new Label("Import a dataset");
        title.setStyleName(RESOURCES.getCSS().title());
        FlowPanel header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().header());
        header.add(icon);
        header.add(title);

        rtn.add(header);

        datasetPanel = new FlowPanel();
        rtn.add(datasetPanel);

        rtn.add(datasetInfoPanel = getDatasetInfoPanel());
        datasetInfoPanel.setVisible(false);

        return rtn;
    }

    private Widget getDatasetInfoPanel() {
        datasetInfoIcon = new Image(RESOURCES.binIcon());
        datasetInfoIcon.setStyleName(RESOURCES.getCSS().infoIcon());

        datasetInfoMessage = new Label();
        datasetInfoMessage.setStyleName(RESOURCES.getCSS().infoMessage());

        FlowPanel infoPanel = new FlowPanel();
        infoPanel.setStyleName(RESOURCES.getCSS().infoPanel());
        infoPanel.add(datasetInfoIcon);
        infoPanel.add(datasetInfoMessage);

        return infoPanel;
    }

    private void submitExample(ExampleDataset example) {
        ExternalDataset ee = new ExternalDataset(EXAMPLE_DATASET_RESOURCE_ID);
        ee.addDatasetId(example.getId());

        uploadInProgress = true;
        GSAClient.loadDataset(ee, new GSAClientHandler.GSADatasetLoadHandler() {
            @Override
            public void onDatasetLoadSuccess(String statusToken) {
                updateExampleInfo(RESOURCES.uploadSpinnerIcon(), DATASET_QUERYING_MSG);
                showExampleInfoPanel(true);

                checkExampleLoadingStatusUntilCompleted(statusToken, example);
            }

            @Override
            public void onError(GSAError error) {
                uploadInProgress = false;
                updateExampleInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                showExampleInfoPanel(true);
            }

            @Override
            public void onException(String msg) {
                uploadInProgress = false;
                updateExampleInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                showExampleInfoPanel(true);
            }
        });

    }

    private void submitDataset(ExternalDatasourceItem externalDatasourceItem) {
        ExternalDataset externalDataset = new ExternalDataset(externalDatasourceItem.getExternalDatasource().getId(), externalDatasourceItem.getParameterValues());

        uploadInProgress = true;
        GSAClient.loadDataset(externalDataset, new GSAClientHandler.GSADatasetLoadHandler() {
            @Override
            public void onDatasetLoadSuccess(String statusToken) {
                updateDatasetInfo(RESOURCES.uploadSpinnerIcon(), DATASET_QUERYING_MSG);
                showDatasetInfoPanel(true);

                checkDatasetLoadingStatusUntilCompleted(statusToken);
            }
            @Override
            public void onError(GSAError error) {
                uploadInProgress = false;
                updateDatasetInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                showDatasetInfoPanel(true);
            }

            @Override
            public void onException(String msg) {
                uploadInProgress = false;
                updateDatasetInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                showDatasetInfoPanel(true);
            }
        });
    }

    private void checkDatasetLoadingStatusUntilCompleted(String statusToken) {
        if (statusToken == null || statusToken.isEmpty()) return;

        Scheduler.get().scheduleFixedPeriod(() -> {
            if (!uploadInProgress) return false;

            GSAClient.getDatasetLoadingStatus(statusToken, new GSAClientHandler.GSAStatusHandler() {
                @Override
                public void onStatusSuccess(Status status) {
                    if (status.getStatus().equalsIgnoreCase("running")) {
                        uploadInProgress = true;
                        updateDatasetInfo(RESOURCES.uploadSpinnerIcon(), DATASET_QUERYING_MSG);
                        showDatasetInfoPanel(true);
                    } else if (status.getStatus().equalsIgnoreCase("complete")) {
                        uploadInProgress = false;
                        getExternalDatasourcesSummary(status.getDatasetId());
                    } else if (status.getStatus().equalsIgnoreCase("failed")) {
                        uploadInProgress = false;
                        updateDatasetInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                        showDatasetInfoPanel(true);
                    }
                }

                @Override
                public void onError(GSAError error) {
                    uploadInProgress = false;
                    Console.info("Error uploading example: " + error.getTitle());
                    updateDatasetInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                    showDatasetInfoPanel(true);
                }

                @Override
                public void onException(String msg) {
                    uploadInProgress = false;
                    Console.info("Error uploading example: " + msg);
                    updateDatasetInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                    showDatasetInfoPanel(true);
                }
            });
            return true;
        }, UPLOAD_POLLING_PERIOD);
    }

    private void checkExampleLoadingStatusUntilCompleted(String statusToken, ExampleDataset example) {
        if (statusToken == null || statusToken.isEmpty()) return;

        Scheduler.get().scheduleFixedPeriod(() -> {
            if (!uploadInProgress) return false;

            GSAClient.getDatasetLoadingStatus(statusToken, new GSAClientHandler.GSAStatusHandler() {
                @Override
                public void onStatusSuccess(Status status) {
                    if (status.getStatus().equalsIgnoreCase("running")) {
                        uploadInProgress = true;
                        updateExampleInfo(RESOURCES.uploadSpinnerIcon(), DATASET_QUERYING_MSG);
                        showExampleInfoPanel(true);
                    } else if (status.getStatus().equalsIgnoreCase("complete")) {
                        uploadInProgress = false;
                        getExampleSummary(example);
                    } else if (status.getStatus().equalsIgnoreCase("failed")) {
                        uploadInProgress = false;
                        updateExampleInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                        showExampleInfoPanel(true);
                    }
                }

                @Override
                public void onError(GSAError error) {
                    uploadInProgress = false;
                    Console.info("Error uploading example: " + error.getTitle());
                    updateExampleInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                    showExampleInfoPanel(true);
                }

                @Override
                public void onException(String msg) {
                    uploadInProgress = false;
                    Console.info("Error uploading example: " + msg);
                    updateExampleInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                    showExampleInfoPanel(true);
                }
            });
            return true;
        }, UPLOAD_POLLING_PERIOD);
    }

    private void getExternalDatasourcesSummary(String datasetId) {

        GSAClient.getDatasetSummary(datasetId, new GSAClientHandler.GSADatasetSummaryHandler() {
            @Override
            public void onDatasetSummarySuccess(ExampleDatasetSummary summary) {
                uploadInProgress = false;
                updateDatasetInfo(RESOURCES.successIcon(), DATASET_SUCCESS_MSG);
                showDatasetInfoPanel(true);

                GSADataset dataset = GSADataset.create(summary);

                wizardContext.setDatasetToAnnotate(dataset);
                Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
                    @Override
                    public boolean execute() {
                        wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.ANNOTATE_DATASET), this);
                        return false;
                    }
                }, 700);
            }

            @Override
            public void onError(GSAError error) {
                uploadInProgress = false;
                Console.info("Error getting example summary: " + error.getDetail());
                updateDatasetInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                showDatasetInfoPanel(true);
            }

            @Override
            public void onException(String msg) {
                uploadInProgress = false;
                Console.info("Error getting example summary: " + msg);
                updateDatasetInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                showDatasetInfoPanel(true);
            }
        });

    }

    private void getExampleSummary(ExampleDataset example) {
        GSAClient.getDatasetSummary(example.getId(), new GSAClientHandler.GSADatasetSummaryHandler() {
            @Override
            public void onDatasetSummarySuccess(ExampleDatasetSummary summary) {
                uploadInProgress = false;
                updateExampleInfo(RESOURCES.successIcon(), DATASET_SUCCESS_MSG);
                showExampleInfoPanel(true);

                GSADataset dataset = GSADataset.create(summary);

                wizardContext.setDatasetToAnnotate(dataset);
                Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
                    @Override
                    public boolean execute() {
                        wizardEventBus.fireEventFromSource(new StepSelectedEvent(GSAStep.ANNOTATE_DATASET), this);
                        return false;
                    }
                }, 700);
            }

            @Override
            public void onError(GSAError error) {
                uploadInProgress = false;
                Console.info("Error getting example summary: " + error.getDetail());
                updateExampleInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                showExampleInfoPanel(true);
            }

            @Override
            public void onException(String msg) {
                uploadInProgress = false;
                Console.info("Error getting example summary: " + msg);
                updateExampleInfo(RESOURCES.errorIcon(), DATASET_FAILED_MSG);
                showExampleInfoPanel(true);
            }
        });
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("../../images/folder.png")
        ImageResource folderIcon();

        @Source("../../images/pen.png")
        ImageResource penIcon();

        @Source("../../images/bin.png")
        ImageResource binIcon();

        @Source("../../images/success.png")
        ImageResource successIcon();

        @Source("../../images/error.png")
        ImageResource errorIcon();

        @Source("../../images/upload_spinner.gif")
        ImageResource uploadSpinnerIcon();

    }

    @CssResource.ImportedWithPrefix("pwp-AddDatasetPanel")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/tools/analysis/gsa/common/widgets/AddDatasetPanel.css";

        String main();

        String expanded();

        String categoryPanel();

        String datasetPanel();

        String header();

        String title();

        String icon();

        String itemsPanel();

        String typePanel();

        String typeContent();

        String typeName();

        String typeDescription();

        String infoPanel();

        String infoIcon();

        String infoMessage();

    }
}
