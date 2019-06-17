package org.reactome.web.pwp.client.details.common.widgets.button;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class IconButton extends Button {
    private FlowPanel fp;
    private Image image;
    private Image overlay;
    private InlineLabel label;

    public IconButton(String text, ImageResource imageResource) {
        image = new Image(imageResource);

        fp = new FlowPanel();
        fp.add(image);
        if(text!=null && !text.isEmpty()) {
            label = new InlineLabel(text);
            fp.add(label);
        }

        updateHTML();
    }

    public IconButton(String text, ImageResource imageResource, ClickHandler clickHandler) {
        this(text, imageResource);
        addClickHandler(clickHandler);
    }

    public IconButton(ImageResource imageResource, String style, String tooltip, ClickHandler clickHandler) {
        this(null, imageResource);
        this.setStyleName(style);
        this.setTitle(tooltip);
        addClickHandler(clickHandler);
    }

    public IconButton(String text, ImageResource imageResource, String style, String tooltip, ClickHandler clickHandler) {
        this(text, imageResource);
        this.setStyleName(style);
        this.setTitle(tooltip);
        addClickHandler(clickHandler);
    }

    public void setText(String text) {
        if(label!=null) {
            label.setText(text);
            updateHTML();
        }
    }

    public void setImage(ImageResource imageResource) {
        image.setResource(imageResource);
        updateHTML();
    }

    public void setOverlayIcon(ImageResource overlayIcon) {
        clearOverlayIcon();

        overlay = new Image(overlayIcon);
        overlay.setHeight("15px");
        overlay.setWidth("auto");

        Style style = overlay.getElement().getStyle();
        style.setPosition(Style.Position.ABSOLUTE);
        style.setRight(-4, Style.Unit.PX);
        style.setBottom(0, Style.Unit.PX);

        fp.add(overlay);
        updateHTML();
    }

    public void clearOverlayIcon() {
        if(overlay!=null) {
            overlay.removeFromParent();
            updateHTML();
        }
    }

    private void updateHTML() {
        SafeHtml safeHtml = SafeHtmlUtils.fromSafeConstant(fp.toString());
        this.setHTML(safeHtml);
    }
}
