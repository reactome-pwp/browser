package org.reactome.web.pwp.client.tools.analysis.wizard.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.TextArea;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.GoHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GoEvent extends GwtEvent<GoHandler> {
    public static final Type<GoHandler> TYPE = new Type<>();

    private FormPanel form;
    private TextArea textArea;

    public GoEvent(FormPanel form) {
        this.form = form;
    }

    public GoEvent(TextArea textArea) {
        this.textArea = textArea;
    }

    public Type<GoHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GoHandler handler) {
        handler.onGoSelected(this);
    }

    public FormPanel getForm() {
        return form;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    @Override
    public String toString() {
        return "GoEvent{" +
                "fileUpload=" + (form != null) +
                ", textArea=" + (textArea != null) +
                '}';
    }
}
