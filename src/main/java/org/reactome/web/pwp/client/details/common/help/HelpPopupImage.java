package org.reactome.web.pwp.client.details.common.help;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HelpPopupImage extends Image implements MouseOverHandler, MouseOutHandler, ClickHandler {
    HelpPopup popup;

    public HelpPopupImage(ImageResource resource, String title, Widget content) {
        super(resource);
        popup = new HelpPopup(title, content);
        addMouseOverHandler(this);
        addMouseOutHandler(this);
        addClickHandler(this);
        getElement().getStyle().setProperty("cursor", "help");
    }

    @Override
    public void onMouseOver(MouseOverEvent event) {
        popup.setPositionAndShow(event);
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        popup.hide(true);
    }

    @Override
    public void onClick(ClickEvent event) {
        popup.setPositionAndShow(event);
    }
}
