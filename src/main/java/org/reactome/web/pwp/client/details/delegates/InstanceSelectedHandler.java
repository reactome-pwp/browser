package org.reactome.web.pwp.client.details.delegates;


import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.util.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface InstanceSelectedHandler {

    public void eventSelected(Path path, Pathway pathway, Event event);

    public void instanceSelected(DatabaseObject databaseObject);
}