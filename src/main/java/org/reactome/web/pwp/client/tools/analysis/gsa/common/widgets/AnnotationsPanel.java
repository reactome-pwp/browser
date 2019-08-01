package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.AnnotationProperty;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnnotationsPanel extends FlowPanel {
    private static RegExp regExp = RegExp.compile("^[a-zA-Z\\d_]+$");

    private GSADataset dataset;

    private FlowPanel samplesPanel;
    private FlowPanel propertiesPanel;
    private Button addBtn;

    public AnnotationsPanel(GSADataset dataset) {
        this.dataset = dataset;
        init();
    }

    private void init() {
        setStyleName(RESOURCES.getCSS().main());

        addBtn = new IconButton(RESOURCES.addIcon(), RESOURCES.getCSS().addNewPropertyBtn(), "Add new annotation property", event -> {
            AnnotationProperty property = dataset.getAnnotations().createAnnotationProperty("Annotation");
            if (property != null) {
                propertiesPanel.add(createNewAnnotation(property));
            }
        });

        add(samplesPanel = getSamplesPanel());
        add(propertiesPanel = getPropertiesPanel());
        add(addBtn);

    }

    private FlowPanel getSamplesPanel() {
        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().samplesPanel());

        for (String sample : dataset.getSampleNames()) {
            Label sampleLB = new Label(sample);
            sampleLB.setStyleName(RESOURCES.getCSS().sampleItem());
            rtn.add(sampleLB);
        }

        return rtn;
    }

    private FlowPanel getPropertiesPanel() {
        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().propertiesPanel());

        for (AnnotationProperty annotationProperty : dataset.getAnnotations().getAllAnnotations()) {
            rtn.add(createNewAnnotation(annotationProperty));
        }

        return rtn;
    }

    private FlowPanel createNewAnnotation(AnnotationProperty annotationProperty) {
        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().propertiesGroup());

        FlowPanel header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().propertiesItem());
        header.addStyleName(RESOURCES.getCSS().propertiesItemTitle());

        ExtendedTextBox title = new ExtendedTextBox();
        title.setText(annotationProperty.getName());
        title.getElement().setPropertyString("placeholder", "Enter a name");
        title.addValueChangeHandler(event -> updatePropertyName(title, annotationProperty, title.getText()));
        header.add(title);

        Button deleteBtn = new IconButton(RESOURCES.deleteIcon(), RESOURCES.getCSS().deletePropertyBtn(), "Delete annotation property", event -> {
            Console.info("Deleting ... " + annotationProperty.getName() );
            rtn.removeFromParent();
            dataset.getAnnotations().deleteAnnotationProperty(annotationProperty);
        });
        header.add(deleteBtn);
        rtn.add(header);

        String[] values = annotationProperty.getValues();
        for (int i = 0; i < values.length; i++) {
            ExtendedTextBox sampleTB = new ExtendedTextBox();
            sampleTB.setText(values[i]);
            sampleTB.setStyleName(RESOURCES.getCSS().propertiesItem());
            sampleTB.addStyleName(RESOURCES.getCSS().propertiesItemInput());
            sampleTB.getElement().setPropertyString("placeholder", "Click to edit");
            final int k = i;
            sampleTB.addValueChangeHandler(event -> {
                if (regExp.exec(sampleTB.getText()) != null) {
                    values[k] = sampleTB.getText();
                } else {
                    sampleTB.setText(values[k]);
                }
            });
            rtn.add(sampleTB);
        }

        return rtn;
    }

    private void updatePropertyName(ExtendedTextBox textbox, AnnotationProperty annotationProperty, String newName) {
        if (!newName.isEmpty() && dataset.getAnnotations().isAnnotationNameValid(annotationProperty, newName)) {
            annotationProperty.setName(newName);
        } else {
            Console.info("Invalid or duplicate name detected: " + newName);
            textbox.setText(annotationProperty.getName());

        }
    }

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("../../images/addNewItem.png")
        ImageResource addIcon();

        @Source("../../images/bin.png")
        ImageResource deleteIcon();

    }

    @CssResource.ImportedWithPrefix("pwp-AnnotationsPanel")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/tools/analysis/gsa/common/widgets/AnnotationsPanel.css";

        String main();

        String samplesPanel();

        String sampleItem();

        String propertiesPanel();

        String propertiesGroup();

        String propertiesItem();

        String propertiesItemInput();

        String propertiesItemTitle();

        String addNewPropertyBtn();

        String deletePropertyBtn();

        String unselectable();

        String undraggable();

    }
}
