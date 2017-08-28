package org.reactome.web.pwp.client.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.diagram.common.PwpButton;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MessagesDisplay extends PopupPanel implements Messages.Display {

    private Messages.Presenter presenter;

    public MessagesDisplay() {
        super(true, true);
        this.setAnimationEnabled(true);
        this.setGlassEnabled(true);
        this.setAutoHideOnHistoryEventsEnabled(false);
        this.addStyleName(RESOURCES.getCSS().popupPanel());
    }

    @Override
    public void showErrorMessage(String errorMsg) {
        this.clear();
        this.add(getErrorMessagePanel(errorMsg));
        this.show();
        this.center();
    }

    @Override
    public void setPresenter(Messages.Presenter presenter) {
        this.presenter = presenter;
    }

    private Widget getErrorMessagePanel(String errorMsg){
        FlowPanel message = new FlowPanel();
        message.setStyleName(RESOURCES.getCSS().message());

        InlineLabel header = new InlineLabel("OOPS! Something unexpected happened");
        header.setStyleName(RESOURCES.getCSS().messageHeader());
//        message.add(header);

        Button closeBtn = new PwpButton("Close this message", RESOURCES.getCSS().close(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                MessagesDisplay.this.hide();
            }
        });

        FlowPanel headerFp = new FlowPanel();
        headerFp.add(header);
        headerFp.add(closeBtn);
        message.add(headerFp);

        Label content = new Label(errorMsg);
        content.setStyleName(RESOURCES.getCSS().messageContent());
        message.add(content);

        FlowPanel panel = new FlowPanel();
        Image error = new Image(RESOURCES.errorIcon());
        panel.add(error);
        panel.add(message);

        return panel;
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

        @Source("images/close_clicked.png")
        ImageResource closeClicked();

        @Source("images/close_hovered.png")
        ImageResource closeHovered();

        @Source("images/close_normal.png")
        ImageResource closeNormal();

        @Source("images/error_icon.png")
        ImageResource errorIcon();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-InfoMessages")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/messages/Messages.css";

        String popupPanel();

        String message();

        String messageHeader();

        String messageContent();

        String close();
    }
}
