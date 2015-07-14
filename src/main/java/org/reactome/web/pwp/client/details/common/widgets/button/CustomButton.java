package org.reactome.web.pwp.client.details.common.widgets.button;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CustomButton extends Button {
    private String text;

    public CustomButton(){
        super();
    }

    public CustomButton(ImageResource imageResource, String text){
        super();
        Image csv = new Image(imageResource);
        FlowPanel fp = new FlowPanel();
        InlineLabel label = new InlineLabel(text);
        label.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        label.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        fp.add(csv); fp.add(label);
        fp.getElement().getStyle().setPaddingLeft(3, Style.Unit.PX);
        fp.getElement().getStyle().setPaddingRight(3, Style.Unit.PX);
        getElement().getStyle().setPadding(0, Style.Unit.PX);
        getElement().getStyle().setPaddingBottom(0, Style.Unit.PX);
        getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        setHTML(fp.toString());
    }

    public void setResource(ImageResource imageResource){
        Image img = new Image(imageResource);
        String definedStyles = img.getElement().getAttribute("style");
        img.getElement().setAttribute("style", definedStyles + "; vertical-align:middle; margin-bottom:2px; margin-top:2px");
        DOM.insertBefore(getElement(), img.getElement(), DOM.getFirstChild(getElement()));
    }

    public void setResourceNoBorder(ImageResource imageResource){
        Image img = new Image(imageResource);
        getElement().getStyle().setPadding(1, Style.Unit.PX);
        getElement().getStyle().setPaddingBottom(0, Style.Unit.PX);
        DOM.appendChild(getElement(), img.getElement());
    }

    @Override
    public void setText(String text) {
        this.text = text;
        Element div = DOM.createElement("div");
        div.setInnerText(text);
        div.setAttribute("style", "padding-left:3px; vertical-align:middle; margin-bottom:2px; margin-top:2px");

        DOM.insertChild(getElement(), div, 0);
    }

    @Override
    public String getText() {
        return this.text;
    }
}
