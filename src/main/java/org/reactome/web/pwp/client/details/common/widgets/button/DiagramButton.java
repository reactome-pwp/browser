package org.reactome.web.pwp.client.details.common.widgets.button;

import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import org.reactome.web.pwp.model.client.classes.Figure;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramButton extends Button {
    private Pathway pathway;
    private Figure figure;

    public DiagramButton(ImageResource imageResource, Figure figure) {
        super(DiagramButton.getHTML(imageResource, "Illustration"));
        setWidth("95px");
        this.figure = figure;
    }

    public DiagramButton(ImageResource imageResource, Pathway pathway) {
        super(DiagramButton.getHTML(imageResource, "Overview"));
        setWidth("95px");
        this.pathway = pathway;
    }

    public Figure getFigure() {
        return figure;
    }

    public Pathway getPathway() {
        return pathway;
    }

    private static String getHTML(ImageResource imageResource, String text){
        FlowPanel panel = new FlowPanel();

        Image image = new Image(imageResource.getSafeUri());
        image.setSize("16px","16px");
        image.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
        panel.add(image);

        InlineLabel label = new InlineLabel(text);
        label.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        panel.add(label);

        return panel.toString();
    }
}
