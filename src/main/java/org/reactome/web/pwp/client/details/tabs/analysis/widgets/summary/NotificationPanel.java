package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class NotificationPanel extends FlowPanel implements ClickHandler {

    public NotificationPanel(){
        this.addStyleName(RESOURCES.getCSS().notificationPanel());

        Image icon = new Image(RESOURCES.warning());
        icon.setStyleName(RESOURCES.getCSS().icon());

        InlineLabel title = new InlineLabel("Warnings (5)");
        title.addClickHandler(this);
        add(icon);
        add(title);

        initHandlers();
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        this.addStyleName(RESOURCES.getCSS().notificationPanelExpanded());
    }

    private void initHandlers(){

    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("../images/warning.png")
        ImageResource warning();
//
//        @Source("../images/close_hovered.png")
//        ImageResource closeHovered();
//
//        @Source("../images/close_normal.png")
//        ImageResource closeNormal();

    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-NotificationPanel")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/summary/NoticationPanel.css";

        String notificationPanel();

        String notificationPanelExpanded();

        String icon();
    }
}
