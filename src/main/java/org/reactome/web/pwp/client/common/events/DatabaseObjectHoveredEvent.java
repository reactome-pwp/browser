package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.DatabaseObjectHoveredHandler;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.util.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DatabaseObjectHoveredEvent extends GwtEvent<DatabaseObjectHoveredHandler> {
    public static final Type<DatabaseObjectHoveredHandler> TYPE = new Type<>();

    private DatabaseObject databaseObject;
    private Path path;

    public DatabaseObjectHoveredEvent() {
        this.databaseObject = null;
        this.path = new Path();
    }

    public DatabaseObjectHoveredEvent(DatabaseObject databaseObject) {
        this.databaseObject = databaseObject;
        this.path = new Path();
    }

    public DatabaseObjectHoveredEvent(DatabaseObject databaseObject, Path path) {
        this.databaseObject = databaseObject;
        this.path = path;
    }

    @Override
    public Type<DatabaseObjectHoveredHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DatabaseObjectHoveredHandler handler) {
        handler.onDatabaseObjectHovered(this);
    }

    public DatabaseObject getDatabaseObject() {
        return databaseObject;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "DatabaseObjectHoveredEvent{" +
                "HOVERED=" + databaseObject +
                (path != null ? ", path=" + path.size() : "") +
                '}';
    }
}
