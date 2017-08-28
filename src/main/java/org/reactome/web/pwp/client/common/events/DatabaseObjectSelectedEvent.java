package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.handlers.DatabaseObjectSelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DatabaseObjectSelectedEvent extends GwtEvent<DatabaseObjectSelectedHandler> {
    public static final Type<DatabaseObjectSelectedHandler> TYPE = new Type<>();

    private Selection selection;

    public DatabaseObjectSelectedEvent(Selection selection) {
        this.selection = selection;
    }

    @Override
    public Type<DatabaseObjectSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DatabaseObjectSelectedHandler handler) {
        handler.onDatabaseObjectSelected(this);
    }

    public Selection getSelection() {
        return selection;
    }

    @Override
    public String toString() {
        return "DatabaseObjectSelectedEvent{" + selection + '}';
    }
}
