package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class NotificationPopup extends PopupPanel implements ClickHandler {
    private Label messageLabel;

    public NotificationPopup(){
        this.addStyleName(RESOURCES.getCSS().popup());
        this.setModal(false);
        this.setAutoHideEnabled(true);
        messageLabel = new Label();
        messageLabel.setStyleName(RESOURCES.getCSS().messages());
        add(messageLabel);

    }

    public void setMessage(List<String> messages){
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append("-").append(message).append("\n");
        }
        messageLabel.setText(sb.toString());
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        hide();
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
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-NotificationPopup")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/summary/NotificationPopup.css";

        String popup();

        String messages();

    }
}
