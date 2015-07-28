package org.reactome.web.pwp.client.manager.state;

import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.client.RESTFulClient;
import org.reactome.web.pwp.model.client.handlers.AncestorsCreatedHandler;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;
import org.reactome.web.pwp.model.util.Ancestors;
import org.reactome.web.pwp.model.util.Path;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class StateHelper {

    public static List<Event> getEvents(String[] identifiers, Map<String, DatabaseObject> map) {
        List<Event> rtn = new LinkedList<>();
        for (String identifier : identifiers) {
            DatabaseObject databaseObject = map.get(identifier);
            if (databaseObject instanceof Event) {
                rtn.add((Event) databaseObject);
            }
        }
        return rtn;
    }

    public interface PathwayWithDiagramHandler {
        void setPathwayWithDiagram(Pathway pathway, Path path);
        void onPathwayWithDiagramRetrievalError(Throwable throwable);
    }

    public static void getPathwayWithDiagram(Pathway pathway, Path path, final PathwayWithDiagramHandler handler){
        //Trying first to figure out the diagram from the provided path (if there is any)
        if(path!=null && !path.isEmpty()){
            Pathway diagram = path.getLastPathwayWithDiagram();
            if(diagram!=null){
                handler.setPathwayWithDiagram(diagram, path);
                return;
            }
        }

        RESTFulClient.getAncestors(pathway, new AncestorsCreatedHandler() {
            @Override
            public void onAncestorsLoaded(Ancestors ancestors) {
                for (final Path ancestor : ancestors) {
                    Pathway diagram = ancestor.getLastPathwayWithDiagram();
                    if (diagram != null) { //The pathway with diagram object needs to be filled before sending it back
                        diagram.load(new DatabaseObjectLoadedHandler() {
                            @Override
                            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                                handler.setPathwayWithDiagram((Pathway) databaseObject, ancestor);
                            }

                            @Override
                            public void onDatabaseObjectError(Throwable trThrowable) {
                                handler.onPathwayWithDiagramRetrievalError(trThrowable);
                            }
                        });
                        return;
                    }
                }
                handler.setPathwayWithDiagram(null, new Path());
            }

            @Override
            public void onAncestorsError(Throwable exception) {
                handler.onPathwayWithDiagramRetrievalError(exception);
            }
        });
    }
}
