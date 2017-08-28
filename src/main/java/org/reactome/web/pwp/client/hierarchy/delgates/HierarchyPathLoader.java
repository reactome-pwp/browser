package org.reactome.web.pwp.client.hierarchy.delgates;

import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.web.pwp.model.client.util.Ancestors;
import org.reactome.web.pwp.model.client.util.Path;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyPathLoader {

    public interface HierarchyPathLoaderHandler {
        void expandPathway(Path path, Pathway pathway);

        void onPathLoaded(Path path);
    }

    private Path pathToExpand;
    private int openingPath;

    private HierarchyPathLoaderHandler handler;

    public HierarchyPathLoader(HierarchyPathLoaderHandler handler) {
        this.handler = handler;
    }

    public void loadHierarchyEvent(final Path path, final Event event) {
        if (event == null){
            Console.error("Event cannot be null here", this);
        }else {
            ContentClient.getAncestors(event, new ContentClientHandler.AncestorsLoaded() {
                @Override
                public void onAncestorsLoaded(Ancestors ancestors) {
                    setAncestorsListToExpand(ancestors, path, event);
                }

                @Override
                public void onContentClientException(Type type, String message) {
                    Console.error(message, HierarchyPathLoader.this);
                }

                @Override
                public void onContentClientError(ContentClientError error) {
                    Console.error(error.getReason(), HierarchyPathLoader.this);
                }
            });
        }
    }

    public void expandPath() {
        Path path;
        if (this.pathToExpand != null) {
            path = this.pathToExpand.getSubPath(this.openingPath);
            if (this.openingPath < this.pathToExpand.size() - 1) {
                DatabaseObject next = this.pathToExpand.get(this.openingPath);
                this.openingPath += 1;
                if (next instanceof Pathway) {
                    this.handler.expandPathway(path, (Pathway) next);
                    return;
                }
            }
        }else{
            path = null;
        }
        this.handler.onPathLoaded(path);
    }

    private void setAncestorsListToExpand(Ancestors ancestors, Path path, Event event) {
        this.pathToExpand = null;

        List<Path> candidatePaths;
        if (path==null || path.isEmpty()) {
            candidatePaths = ancestors.getPathsContaining(event);
        } else {
            candidatePaths = ancestors.getPathsContaining(path.asList());
        }

        if (!candidatePaths.isEmpty()) {
            this.pathToExpand = candidatePaths.get(0);
        } else {
            this.pathToExpand = new Path();
        }

        if (this.pathToExpand != null && !this.pathToExpand.isEmpty()) {
            this.openingPath = 0;
            this.expandPath();
        } else {
            this.handler.onPathLoaded(path);
        }
    }
}
