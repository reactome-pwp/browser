package org.reactome.web.pwp.client.manager.state;

import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.AncestorsCreatedHandler;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;
import org.reactome.web.pwp.model.util.Ancestors;
import org.reactome.web.pwp.model.util.Path;

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

    public interface PathwayWithDiagramHandler {
        void setPathwayWithDiagram(Pathway pathway);
        void onPathwayWithDiagramRetrievalError(Throwable throwable);
    }

    //TODO: Use Path in first instance before call StateHelper to get the PathwayDiagram
    public static void getPathwayWithDiagram(Pathway pathway, Path path, final PathwayWithDiagramHandler handler){
        DatabaseObjectFactory.getAncestors(pathway, new AncestorsCreatedHandler() {
            @Override
            public void onAncestorsLoaded(Ancestors ancestors) {
                for (Path ancestor : ancestors) {
                    Pathway diagram = ancestor.getLastPathwayWithDiagram();
                    if (diagram != null) { //The pathway with diagram object needs to be filled before sending it back
                        diagram.load(new DatabaseObjectLoadedHandler() {
                            @Override
                            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                                handler.setPathwayWithDiagram((Pathway) databaseObject);
                            }

                            @Override
                            public void onDatabaseObjectError(Throwable trThrowable) {
                                handler.onPathwayWithDiagramRetrievalError(trThrowable);
                            }
                        });
                        return;
                    }
                }
                handler.setPathwayWithDiagram(null);
            }

            @Override
            public void onAncestorsError(Throwable exception) {
                handler.onPathwayWithDiagramRetrievalError(exception);
            }
        });
    }
}
