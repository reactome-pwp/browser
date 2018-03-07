package org.reactome.web.pwp.client.details.tabs.downloads.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.button.IconToggleButton;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DownloadGroupPanel extends FlowPanel {

    private FlowPanel optionsInner;
    private FlowPanel infoPanel;
    private boolean isInfoVisible = false;

    public DownloadGroupPanel(String title, String info) {
        super();
        setStyleName(RESOURCES.getCSS().main());

        IconToggleButton infoBtn = new IconToggleButton("", RESOURCES.info(), RESOURCES.cancel());
        infoBtn.setStyleName(RESOURCES.getCSS().infoBtn());
        infoBtn.setVisible(true);
        infoBtn.setTitle("More information");
        infoBtn.addClickHandler(e -> showHideInfo());

        Label titleLabel = new Label(title);
        titleLabel.setStyleName(RESOURCES.getCSS().titleLabel());

        FlowPanel titlePanel = new FlowPanel();
        titlePanel.setStyleName(RESOURCES.getCSS().titlePanel());
        titlePanel.add(infoBtn);
        titlePanel.add(titleLabel);

        optionsInner = new FlowPanel();
        optionsInner.setStyleName(RESOURCES.getCSS().optionsInner());

        SimplePanel optionsParent = new SimplePanel();
        optionsParent.setStyleName(RESOURCES.getCSS().optionsParent());
        optionsParent.add(optionsInner);

        infoPanel = new FlowPanel();
        infoPanel.setStyleName(RESOURCES.getCSS().infoPanel());
        infoPanel.add(new HTMLPanel( "\u24D8 " + info));

        this.add(titlePanel);
        this.add(optionsParent);
        this.add(infoPanel);
    }

    public void insert(Widget w) {
        optionsInner.add(w);
    }

    private void showHideInfo() {

        if (isInfoVisible) {
            infoPanel.removeStyleName(RESOURCES.getCSS().infoPanelExpanded());
            isInfoVisible = false;
        } else {
            infoPanel.addStyleName(RESOURCES.getCSS().infoPanelExpanded());
            isInfoVisible = true;
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

        @Source("../widgets/images/info.png")
        ImageResource info();

        @Source("../widgets/images/cancel.png")
        ImageResource cancel();

    }

    @CssResource.ImportedWithPrefix("diagram-DownloadGroupPanel")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/details/tabs/downloads/DownloadGroupPanel.css";

        String main();

        String titlePanel();

        String titleLabel();

        String infoBtn();

        String optionsParent();

        String optionsInner();

        String infoPanel();

        String infoPanelExpanded();
    }
}
