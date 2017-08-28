package org.reactome.web.pwp.client.details.delegates;


import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.util.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InstanceSelectedDelegate {

    private static InstanceSelectedDelegate INSTANCE;
    private InstanceSelectedHandler handler;

    protected InstanceSelectedDelegate() {
    }

    public static InstanceSelectedDelegate get() {
        if (INSTANCE == null)
            INSTANCE = new InstanceSelectedDelegate();
        return INSTANCE;
    }

    public void eventSelected(Path path, Pathway pathway, Event event) {
        this.handler.eventSelected(path, pathway, event);
    }

    public void instanceSelected(DatabaseObject databaseObject) {
        this.handler.instanceSelected(databaseObject);
    }

    public void setInstanceSelectedHandler(InstanceSelectedHandler handler) {
        this.handler = handler;
    }
}