package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class NotificationPanel extends Anchor implements ClickHandler {

    private List<String> warnings;
    private NotificationPopup popupInstance;

    public NotificationPanel(List<String> warnings){
        this.warnings = warnings;
        this.addStyleName(RESOURCES.getCSS().notificationPanel());

        Image icon = new Image(RESOURCES.warning());
        icon.setStyleName(RESOURCES.getCSS().icon());

        InlineLabel title = new InlineLabel( warnings.size() + " Warning" + (warnings.size()>1?"s":"") );
        title.addClickHandler(this);

        FlowPanel fp = new FlowPanel();
        fp.add(icon);
        fp.add(title);

        this.setHTML(SafeHtmlUtils.fromTrustedString(fp.toString()));
        this.addClickHandler(this);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if(popupInstance==null) {
            popupInstance = new NotificationPopup();
        }

        popupInstance.setMessage(warnings);
        popupInstance.showRelativeTo(this);
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
    @CssResource.ImportedWithPrefix("pwp-NotificationPanel")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/summary/NoticationPanel.css";

        String notificationPanel();

        String icon();
    }
}
