package org.reactome.web.pwp.client.tools.analysis.event;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.tools.analysis.handler.FileNotSelectedEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FileNotSelectedEvent extends GwtEvent<FileNotSelectedEventHandler> {
    public static final Type<FileNotSelectedEventHandler> TYPE = new Type<>();

    @Override
    public Type<FileNotSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FileNotSelectedEventHandler handler) {
        handler.onFileNotSelectedEvent(this);
    }

    @Override
    public String toString() {
        return "FileNotSelectedEvent{}";
    }
}
