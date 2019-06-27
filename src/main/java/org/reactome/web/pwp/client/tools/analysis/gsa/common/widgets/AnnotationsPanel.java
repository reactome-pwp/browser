package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnnotationsPanel extends FlowPanel {
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

        addBtn = new IconButton(RESOURCES.addIcon(), RESOURCES.getCSS().parametersBtn(), "Add new annotation property", event -> {
            propertiesPanel.add(createNewPropertyAnnotation());
        });

        add(samplesPanel = getSamplesPanel());
        add(propertiesPanel = getPropertiesPanel());
        add(addBtn);

    }

    private FlowPanel getSamplesPanel() {
        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().samplesPanel());

        for (String sample : dataset.getSampleNames()) {
            Label sampleLB = new Label(sample + " " + sample + " " + sample);
            sampleLB.setStyleName(RESOURCES.getCSS().sampleItem());
            rtn.add(sampleLB);
        }

        return rtn;
    }

    private FlowPanel getPropertiesPanel() {
        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().propertiesPanel());

        return rtn;
    }

    private FlowPanel createNewPropertyAnnotation() {
        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().propertiesGroup());

        FlowPanel header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().propertiesItem());
        header.addStyleName(RESOURCES.getCSS().propertiesItemTitle());
        ExtendedTextBox title = new ExtendedTextBox();
        title.setText("Annotation");
        title.addValueChangeHandler(event -> Console.info(" >>> >>> " + event.getValue()));
        title.addKeyUpHandler(event -> Console.info(" >>> >>> " + title.getText()));
        header.add(title);
        header.add(new Image(RESOURCES.addIcon()));
        rtn.add(header);

        for (String sample : dataset.getSampleNames()) {
            Label sampleLB = new Label("Blank");
            sampleLB.setStyleName(RESOURCES.getCSS().propertiesItem());
            rtn.add(sampleLB);
        }

        return rtn;
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

        String propertiesItemTitle();

        String parametersBtn();

        String unselectable();

        String undraggable();

    }
}
