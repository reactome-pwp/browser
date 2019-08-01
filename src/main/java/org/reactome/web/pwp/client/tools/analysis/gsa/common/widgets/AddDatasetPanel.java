package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAException;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAFactory;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.DatasetType;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.GSAError;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.UploadResult;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.StepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.steps.GSAStep;

import java.util.List;
import java.util.Optional;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AddDatasetPanel extends FlowPanel implements ClickHandler, ChangeHandler,
        FormPanel.SubmitHandler, FormPanel.SubmitCompleteHandler {
    private static final String URL_UPLOAD_FILE = "/gsa/upload";

    private static final String UPLOADING_MSG = "Uploading file ...";
    private static final String UPLOAD_SUCCESS_MSG = "File uploaded successfully";
    private static final String UPLOAD_FAILED_MSG = "File upload failed. Please try again.";

    private GSAWizardEventBus wizardEventBus;
    private GSAWizardContext wizardContext;

    private List<DatasetType> datasetTypes;
    private boolean isExpanded;
    private DatasetType selectedType;

    private FlowPanel itemsPanel;
    private FormPanel form;
    private FileUpload fileUpload;

    private Widget infoPanel;
    private Image infoIcon;
    private Label infoMessage;

    public AddDatasetPanel(GSAWizardEventBus wizardEventBus, GSAWizardContext wizardContext) {
        this.wizardEventBus = wizardEventBus;
        this.wizardContext = wizardContext;
        init();
    }

    public void setDatasetTypes(List<DatasetType> types) {
        this.datasetTypes = types;
        getTypeWidgets();
    }

    @Override
    public void onClick(ClickEvent event) {
        showInfoPanel(false);
        Optional<DatasetType> datasetType = datasetTypes.stream()
                                                        .filter(dt -> dt.getId().equalsIgnoreCase(((FocusPanel)event.getSource()).getElement().getId()))
                                                        .findFirst();
        selectedType = datasetType.orElse(null);
        fileUpload.click();
    }

    @Override
    public void onChange(ChangeEvent event) {
        if (fileUpload.getFilename() != null && !fileUpload.getFilename().isEmpty()) {
            form.submit();
        }
    }

    @Override
    public void onSubmit(FormPanel.SubmitEvent event) {
        updateInfo(RESOURCES.binIcon(), UPLOADING_MSG);
        showInfoPanel(true);
    }

    @Override
    public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
        showInfoPanel(false);
        String results = event.getResults();
        if (results != null) {
            String json = results.replaceAll("\\<[^>]*>", "");
            try {
                if (results.contains("\"status\":")) {
                    // This is an indication that a server error has occurred
                    GSAError error = GSAFactory.getModelObject(GSAError.class, json);
                    Console.info(error.getStatus() + " -> " + error.getTitle());
                    form.reset();
                    updateInfo(RESOURCES.errorIcon(), UPLOAD_FAILED_MSG);
                    showInfoPanel(true);
                } else {
                    // Create a new dataset and store it in the context
                    UploadResult result = GSAFactory.getModelObject(UploadResult.class, json);
                    String defaultName = selectedType.getId() + "_" + (wizardContext.getAnnotatedDatasets().size() + 1);
                    GSADataset dataset = GSADataset.create(selectedType.getId(), selectedType.getName(), fileUpload.getFilename(), result, defaultName.toUpperCase());
                    wizardContext.setDatasetToAnnotate(dataset);
                    updateInfo(RESOURCES.successIcon(), UPLOAD_SUCCESS_MSG);
                    showInfoPanel(true);
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
                form.reset();
            }
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

    public void showInfoPanel(boolean visible) {
        infoPanel.setVisible(visible);
    }

    private void init() {
        setStyleName(RESOURCES.getCSS().main());
        add(getLocalCategoryPanel());

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
        rtn.add(infoPanel = getInfoPanel());
        infoPanel.setVisible(false);

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
            typePanel.getElement().setId(type.getId());
            typePanel.getElement().getStyle().setBackgroundColor(type.getColour());
            typePanel.addStyleName(RESOURCES.getCSS().typePanel());
            typePanel.addClickHandler(this);
            typePanel.add(content);

            itemsPanel.add(typePanel);
        }

    }

    private Widget getInfoPanel() {
        infoIcon = new Image(RESOURCES.binIcon());
        infoIcon.setStyleName(RESOURCES.getCSS().infoIcon());

        infoMessage = new Label();
        infoMessage.setStyleName(RESOURCES.getCSS().infoMessage());

        FlowPanel infoPanel = new FlowPanel();
        infoPanel.setStyleName(RESOURCES.getCSS().infoPanel());
        infoPanel.add(infoIcon);
        infoPanel.add(infoMessage);

        return infoPanel;
    }

    private void updateInfo(ImageResource icon, String message) {
        infoIcon.setResource(icon);
        infoMessage.setText(message);
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

    }

    @CssResource.ImportedWithPrefix("pwp-AddDatasetPanel")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/tools/analysis/gsa/common/widgets/AddDatasetPanel.css";

        String main();

        String expanded();

        String categoryPanel();

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
