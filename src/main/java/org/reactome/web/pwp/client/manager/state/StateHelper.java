package org.reactome.web.pwp.client.manager.state;

import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.web.pwp.model.client.util.Ancestors;
import org.reactome.web.pwp.model.client.util.Path;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class StateHelper {

    public static List<Event> getEvents(String[] identifiers, Map<String, ? extends DatabaseObject> map) {
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
        void onPathwayWithDiagramRetrievalError(String errorMessage);
    }

    public static void getPathwayWithDiagram(Event event, Path path, final PathwayWithDiagramHandler handler){
        //Trying first to figure out the diagram from the provided path (if there is any)
        if(path!=null && !path.isEmpty()){
            Pathway diagram = path.getLastPathwayWithDiagram();
            if(diagram!=null){
                handler.setPathwayWithDiagram(diagram, path);
                return;
            }
        }

        ContentClient.getAncestors(event, new ContentClientHandler.AncestorsLoaded() {
            @Override
            public void onAncestorsLoaded(Ancestors ancestors) {
                for (final Path ancestor : ancestors) {
                    Pathway diagram = ancestor.getLastPathwayWithDiagram();
                    if (diagram != null) { //The pathway with diagram object needs to be filled before sending it back
                        diagram.load(new ObjectLoaded() {
                            @Override
                            public void onObjectLoaded(DatabaseObject databaseObject) {
                                handler.setPathwayWithDiagram((Pathway) databaseObject, ancestor);
                            }

                            @Override
                            public void onContentClientException(Type type, String message) {
                                handler.onPathwayWithDiagramRetrievalError(message);
                            }

                            @Override
                            public void onContentClientError(ContentClientError error) {
                                handler.onPathwayWithDiagramRetrievalError(error.getReason());

                            }
                        });
                        return;
                    }
                }
                handler.setPathwayWithDiagram(null, new Path());
            }

            @Override
            public void onContentClientException(Type type, String message) {
                //TODO
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                //TODO
            }

//            @Override
//            public void onAncestorsError(Throwable exception) {
//                handler.onPathwayWithDiagramRetrievalError(error);
//            }
        });
    }
}
