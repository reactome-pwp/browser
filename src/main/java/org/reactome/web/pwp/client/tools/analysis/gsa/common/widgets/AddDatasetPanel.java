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

    private static final String EXAMPLE_UPLOADING_MSG = "Please wait while uploading example ...";
    private static final String EXAMPLE_UPLOAD_SUCCESS_MSG = "Example uploaded successfully";
    private static final String EXAMPLE_UPLOAD_FAILED_MSG = "Example upload failed. Please try again.";

    private static final String REMOTE_DATASET_UPLOADING_MSG = "Please wait while querying remote dataset ...";
    private static final String REMOTE_DATASET_SUCCESS_MSG = "Example uploaded successfully";
    private static final String REMOTE_DATASET_FAILED_MSG = "Example upload failed. Please try again.";

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

    private FlowPanel remoteDatasetPanel;

    private Widget remoteDatasetInfoPanel;
    private Image remoteDatasetInfoIcon;
    private Label remoteDatasetInfoMessage;

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
        Console.info("AddDatasetPanel > onOptionExpanded");
        remoteDatasetPanel.forEach(widget -> ((ExternalDatasourceItem)widget).collapse());
    }

    @Override
    public void onOptionSubmitted(ExternalDatasourceItem source) {
        source.validateAllParameters();
        Console.info("AddDatasetPanel > onOptionSubmitted");
        Console.info("AddDatasetPanel > onOptionSubmitted > VALUE: " + source.getParameterValues().get("dataset_id"));
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

    public void showRemoteDatasetInfoPanel(boolean visible) {
        remoteDatasetInfoPanel.setVisible(visible);
    }

    private void init() {
        setStyleName(RESOURCES.getCSS().main());
        add(getLocalCategoryPanel());
        add(orLabel = new Label("OR"));
        add(getExamplesPanel());
        add(new Label("OR"));
        add(getRemoteDatasetPanel());

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

    private void updateRemoteDatasetInfo(ImageResource icon, String message) {
        remoteDatasetInfoIcon.setResource(icon);
        remoteDatasetInfoMessage.setText(message);
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
        remoteDatasetPanel.clear();
        remoteDatasetPanel.getParent().setVisible(!externalDatasources.isEmpty());
        orLabel.setVisible(!externalDatasources.isEmpty());

        for (ExternalDatasource externalDatasource : externalDatasources) {
            remoteDatasetPanel.add(new ExternalDatasourceItem(externalDatasource, this));
        }

//            Label name = new Label(externalDatasource.getName());
//            name.setStyleName(RESOURCES.getCSS().typeName());
//
//            HTMLPanel description = new HTMLPanel(externalDatasource.getName());
//            description.setStyleName(RESOURCES.getCSS().typeDescription());
//
//            FlowPanel content = new FlowPanel();
//            content.setStyleName(RESOURCES.getCSS().externalTypeContent());
//            content.add(name);
//            content.add(description);
//
//            FocusPanel aPanel = new FocusPanel();
//            aPanel.getElement().setId(externalDatasource.getId());
//            aPanel.getElement().getStyle().setBackgroundColor(externalDatasource.getColour());
//            aPanel.addStyleName(RESOURCES.getCSS().externalTypePanel());
//
//            FocusPanel paramPanel = new FocusPanel();
//            ParametersPanel parameters = new ParametersPanel(externalDatasource);
//            parameters.setStyleName(RESOURCES.getCSS().externalParametersPanel());
//            paramPanel.add(parameters);
//            content.add(paramPanel);
//
//            aPanel.addClickHandler(event -> {
//                event.preventDefault();
//                event.stopPropagation();
//                ((FocusPanel)event.getSource()).addStyleName(RESOURCES.getCSS().externalTypePanelExpanded());
//            });
//            aPanel.add(content);

//            remoteDatasetPanel.add(aPanel);
//        }
    }

    private Widget getRemoteDatasetPanel() {
        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().remoteDatasetPanel());

        Image icon = new Image(RESOURCES.folderIcon());
        icon.setStyleName(RESOURCES.getCSS().icon());
        Label title = new Label("Import a dataset");
        title.setStyleName(RESOURCES.getCSS().title());
        FlowPanel header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().header());
        header.add(icon);
        header.add(title);

        remoteDatasetPanel = new FlowPanel();
//        remoteDatasetPanel.setStyleName(RESOURCES.getCSS().itemsPanel());

        rtn.add(header);
        rtn.add(remoteDatasetPanel);

//        rtn.add(remoteDatasetInfoPanel = getRemoteDatasetInfoPanel());
//        remoteDatasetInfoPanel.setVisible(false);

//        getRemoteDatasetWidgets();

        return rtn;
    }

//    private void getRemoteDatasetWidgets() {
//        HTMLPanel description = new HTMLPanel("Please specify the id of the dataset you would like to import");
//        description.setStyleName(RESOURCES.getCSS().remoteDatasetDescription());
//
//        Button importBtn = new Button();
//        importBtn.setStyleName(RESOURCES.getCSS().importBtn());
//        importBtn.setText("Import");
//        importBtn.setTitle("Import a dataset from another resource");
//        importBtn.setEnabled(false);
//
//        TextBox remoteDatasetId = new TextBox();
//        remoteDatasetId.getElement().setPropertyString("placeholder", "Enter a dataset ID, e.g. ...");
//        remoteDatasetId.setStyleName(RESOURCES.getCSS().remoteDatasetId());
//        remoteDatasetId.addKeyUpHandler(event -> {
//            importBtn.setEnabled(!remoteDatasetId.getText().isEmpty());
//        });
//
//        importBtn.addClickHandler(event -> {
//            updateRemoteDatasetInfo(RESOURCES.uploadSpinnerIcon(), REMOTE_DATASET_UPLOADING_MSG);
//            showRemoteDatasetInfoPanel(true);
//            submitRemoteDataset(remoteDatasetId.getText());
//        });
//
//
//        remoteDatasetPanel.clear();
//        remoteDatasetPanel.add(description);
//        remoteDatasetPanel.add(remoteDatasetId);
//        remoteDatasetPanel.add(importBtn);
//
//    }

    private Widget getRemoteDatasetInfoPanel() {
        remoteDatasetInfoIcon = new Image(RESOURCES.binIcon());
        remoteDatasetInfoIcon.setStyleName(RESOURCES.getCSS().infoIcon());

        remoteDatasetInfoMessage = new Label();
        remoteDatasetInfoMessage.setStyleName(RESOURCES.getCSS().infoMessage());

        FlowPanel infoPanel = new FlowPanel();
        infoPanel.setStyleName(RESOURCES.getCSS().infoPanel());
        infoPanel.add(remoteDatasetInfoIcon);
        infoPanel.add(remoteDatasetInfoMessage);

        return infoPanel;
    }

    private void submitExample(ExampleDataset example) {
        uploadInProgress = true;
        GSAClient.loadExampleDataset(example.getId(), new GSAClientHandler.GSAExampleDatasetLoadHandler() {
            @Override
            public void onExampleDatasetLoadSuccess(String statusToken) {
                updateExampleInfo(RESOURCES.uploadSpinnerIcon(), EXAMPLE_UPLOADING_MSG);
                showExampleInfoPanel(true);

                checkExampleLoadingStatusUntilCompleted(statusToken, example);
            }

            @Override
            public void onError(GSAError error) {
                uploadInProgress = false;
                updateExampleInfo(RESOURCES.errorIcon(), EXAMPLE_UPLOAD_FAILED_MSG);
                showExampleInfoPanel(true);
            }

            @Override
            public void onException(String msg) {
                uploadInProgress = false;
                updateExampleInfo(RESOURCES.errorIcon(), EXAMPLE_UPLOAD_FAILED_MSG);
                showExampleInfoPanel(true);
            }
        });

    }
    private void submitRemoteDataset(String exampleId) {
        uploadInProgress = true;
        GSAClient.loadExampleDataset(exampleId, new GSAClientHandler.GSAExampleDatasetLoadHandler() {
            @Override
            public void onExampleDatasetLoadSuccess(String statusToken) {
                updateRemoteDatasetInfo(RESOURCES.uploadSpinnerIcon(), REMOTE_DATASET_UPLOADING_MSG);
                showRemoteDatasetInfoPanel(true);

                checkRemoteDatasetLoadingStatusUntilCompleted(statusToken, exampleId);
            }

            @Override
            public void onError(GSAError error) {
                uploadInProgress = false;
                updateRemoteDatasetInfo(RESOURCES.errorIcon(), REMOTE_DATASET_FAILED_MSG);
                showRemoteDatasetInfoPanel(true);
            }

            @Override
            public void onException(String msg) {
                uploadInProgress = false;
                updateRemoteDatasetInfo(RESOURCES.errorIcon(), REMOTE_DATASET_FAILED_MSG);
                showRemoteDatasetInfoPanel(true);
            }
        });
    }

    private void checkRemoteDatasetLoadingStatusUntilCompleted(String statusToken, String exampleId) {
        if (statusToken == null || statusToken.isEmpty()) return;

        Scheduler.get().scheduleFixedPeriod(() -> {
            if (!uploadInProgress) return false;

            GSAClient.getExampleDatasetLoadingStatus(statusToken, new GSAClientHandler.GSAStatusHandler() {
                @Override
                public void onStatusSuccess(Status status) {
                    if (status.getStatus().equalsIgnoreCase("running")) {
                        uploadInProgress = true;
                        updateRemoteDatasetInfo(RESOURCES.uploadSpinnerIcon(), REMOTE_DATASET_UPLOADING_MSG);
                        showRemoteDatasetInfoPanel(true);
                    } else if (status.getStatus().equalsIgnoreCase("complete")) {
                        uploadInProgress = false;
                        getExternalDatasourcesSummary();
                    } else if (status.getStatus().equalsIgnoreCase("failed")) {
                        uploadInProgress = false;
                        updateRemoteDatasetInfo(RESOURCES.errorIcon(), REMOTE_DATASET_FAILED_MSG);
                        showRemoteDatasetInfoPanel(true);
                    }
                }

                @Override
                public void onError(GSAError error) {
                    uploadInProgress = false;
                    Console.info("Error uploading example: " + error.getTitle());
                    updateRemoteDatasetInfo(RESOURCES.errorIcon(), REMOTE_DATASET_FAILED_MSG);
                    showRemoteDatasetInfoPanel(true);
                }

                @Override
                public void onException(String msg) {
                    uploadInProgress = false;
                    Console.info("Error uploading example: " + msg);
                    updateRemoteDatasetInfo(RESOURCES.errorIcon(), REMOTE_DATASET_FAILED_MSG);
                    showRemoteDatasetInfoPanel(true);
                }
            });
            return true;
        }, UPLOAD_POLLING_PERIOD);
    }

    private void getExternalDatasourcesSummary() {
        GSAClient.getExternalDatasources(new GSAClientHandler.GSAExternalDatasourcesHandler() {
            @Override
            public void onExternalDatasourcesSuccess(List<ExternalDatasource> externalDatasources) {
                Console.info("pickaboo");
            }

            @Override
            public void onError(GSAError error) {
                Console.info("pickaboo");
            }

            @Override
            public void onException(String msg) {
                Console.info("pickaboo");
            }
        });
    }

    private void checkExampleLoadingStatusUntilCompleted(String statusToken, ExampleDataset example) {
        if (statusToken == null || statusToken.isEmpty()) return;

        Scheduler.get().scheduleFixedPeriod(() -> {
            if (!uploadInProgress) return false;

            GSAClient.getExampleDatasetLoadingStatus(statusToken, new GSAClientHandler.GSAStatusHandler() {
                @Override
                public void onStatusSuccess(Status status) {
                    if (status.getStatus().equalsIgnoreCase("running")) {
                        uploadInProgress = true;
                        updateExampleInfo(RESOURCES.uploadSpinnerIcon(), EXAMPLE_UPLOADING_MSG);
                        showExampleInfoPanel(true);
                    } else if (status.getStatus().equalsIgnoreCase("complete")) {
                        uploadInProgress = false;
                        getExampleSummary(example);

                    } else if (status.getStatus().equalsIgnoreCase("failed")) {
                        uploadInProgress = false;
                        updateExampleInfo(RESOURCES.errorIcon(), EXAMPLE_UPLOAD_FAILED_MSG);
                        showExampleInfoPanel(true);
                    }
                }

                @Override
                public void onError(GSAError error) {
                    uploadInProgress = false;
                    Console.info("Error uploading example: " + error.getTitle());
                    updateExampleInfo(RESOURCES.errorIcon(), EXAMPLE_UPLOAD_FAILED_MSG);
                    showExampleInfoPanel(true);
                }

                @Override
                public void onException(String msg) {
                    uploadInProgress = false;
                    Console.info("Error uploading example: " + msg);
                    updateExampleInfo(RESOURCES.errorIcon(), EXAMPLE_UPLOAD_FAILED_MSG);
                    showExampleInfoPanel(true);
                }
            });
            return true;
        }, UPLOAD_POLLING_PERIOD);
    }

    private void getExampleSummary(ExampleDataset example) {
        GSAClient.getExampleDatasetSummary(example.getId(), new GSAClientHandler.GSAExampleDatasetSummaryHandler() {
            @Override
            public void onExampleDatasetSummarySuccess(ExampleDatasetSummary summary) {
                uploadInProgress = false;
                updateExampleInfo(RESOURCES.successIcon(), EXAMPLE_UPLOAD_SUCCESS_MSG);
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
                updateExampleInfo(RESOURCES.errorIcon(), EXAMPLE_UPLOAD_FAILED_MSG);
                showExampleInfoPanel(true);
            }

            @Override
            public void onException(String msg) {
                uploadInProgress = false;
                Console.info("Error getting example summary: " + msg);
                updateExampleInfo(RESOURCES.errorIcon(), EXAMPLE_UPLOAD_FAILED_MSG);
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

        String remoteDatasetPanel();

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

//        String remoteDatasetDescription();

//        String remoteDatasetId();

        String importBtn();

        String externalParametersPanel();

        String externalTypePanel();

        String externalTypeContent();

        String externalTypePanelExpanded();
    }
}
