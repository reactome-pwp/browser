package org.reactome.web.pwp.client.details.common.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class DialogBoxFactory {

    public static DialogBox alert(final String header, final String content) {
        return alert(header, content, null);
    }

    public static DialogBox alert(final String header, final String content, ClickHandler handler) {
        final DialogBox box = new DialogBox();
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().getStyle().setPadding(10, Style.Unit.PX);
        panel.getElement().getStyle().setPaddingBottom(0, Style.Unit.PX);

        box.setText(header);
        for (String s : content.split("\n")) {
            panel.add(new Label(s));
        }
        final Button buttonClose = new Button("Close",new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                box.hide();
            }
        });
        if(handler!=null) buttonClose.addClickHandler(handler);
        buttonClose.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        buttonClose.setWidth("90px");
        panel.add(buttonClose);
        panel.setCellHorizontalAlignment(buttonClose, HasAlignment.ALIGN_RIGHT);
        box.add(panel);
        box.getElement().getStyle().setZIndex(1000);
        box.center();
        return box;
    }

    public static DialogBox alertMsg(final String header, final String content, final Image image) {
        return alertMsg(header, content, null, image);
    }

    public static DialogBox alertMsg(final String header, final String content, ClickHandler handler, Image image) {
        final DialogBox box = new DialogBox();
        FlowPanel panel = new FlowPanel();
        VerticalPanel textPanel = new VerticalPanel();
        textPanel.getElement().getStyle().setPadding(10, Style.Unit.PX);
        textPanel.getElement().getStyle().setPaddingBottom(0, Style.Unit.PX);

        box.setText(header);
        for (String s : content.split("\n")) {
            textPanel.add(new Label(s));
        }
        final Button buttonClose = new Button("Close",new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                box.hide();
            }
        });
        if(handler!=null) buttonClose.addClickHandler(handler);
        buttonClose.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        buttonClose.setWidth("90px");
        textPanel.setCellHorizontalAlignment(buttonClose, HasAlignment.ALIGN_RIGHT);

        panel.add(image);
        panel.add(textPanel);
//        textPanel.getElement().getStyle().setFloat(Style.Float.RIGHT);
        panel.add(buttonClose);
        buttonClose.getElement().getStyle().setFloat(Style.Float.RIGHT);

        box.add(panel);
        box.getElement().getStyle().setZIndex(1000);
        box.center();
        return box;
    }
}
