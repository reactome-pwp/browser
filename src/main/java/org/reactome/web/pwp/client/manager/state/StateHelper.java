package org.reactome.web.pwp.client.manager.state;

import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class StateHelper {

    public static DatabaseObject getDatabaseObject(String identifier, List<DatabaseObject> list) {
        if (identifier == null || identifier.isEmpty()) return null;

        for (DatabaseObject databaseObject : list) {
            if (databaseObject.getIdentifier().equals(identifier) ||
                    databaseObject.getDbId().toString().equals(identifier)) {
                return databaseObject;
            }
        }
        return null;
    }

    public static List<Event> getEvents(String[] identifiers, List<DatabaseObject> list) {
        List<Event> rtn = new LinkedList<>();
        for (String identifier : identifiers) {
            DatabaseObject databaseObject = getDatabaseObject(identifier, list);
            if (databaseObject instanceof Event) {
                rtn.add((Event) databaseObject);
            }
        }
        return rtn;
    }
}
