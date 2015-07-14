package org.reactome.web.pwp.client.details.common.help;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HelpPopup extends PopupPanel implements PopupPanel.PositionCallback {

    private MouseEvent event;

    public HelpPopup(String scope, Widget content) {
        addStyleName("elv-HelpPopup");
        setAnimationEnabled(true);

        VerticalPanel verticalPanel = new VerticalPanel();

        HTMLPanel title = new HTMLPanel(scope);
        title.addStyleName("elv-HelpPopup-Title");
        verticalPanel.add(title);

        content.addStyleName("elv-HelpPopup-Content");
        verticalPanel.add(content);
        getElement().getStyle().setZIndex(1);

        setWidget(verticalPanel);
    }

    public void setPositionAndShow(MouseEvent event){
        setEvent(event);
        setPopupPositionAndShow(this);
    }

    private void setEvent(MouseEvent event) {
        this.event = event;
    }

    @Override
    public void setPosition(int offsetWidth, int offsetHeight) {
        int w = this.getOffsetWidth();
        int hSpace = Window.getClientWidth() - event.getClientX();
        int left = hSpace > w ? event.getClientX() : event.getClientX() - w;// (Window.getClientWidth() - offsetWidth) / 3;

        int h = this.getOffsetHeight();
        int vSpace = Window.getClientHeight() - event.getClientY();

        int top = (vSpace > h)? event.getClientY() : event.getClientY() - h; //(Window.getClientHeight() - offsetHeight) / 3;
        setPopupPosition(left, top);
    }
}
