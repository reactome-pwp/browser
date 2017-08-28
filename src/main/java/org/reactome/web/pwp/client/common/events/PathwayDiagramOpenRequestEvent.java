package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.PathwayDiagramOpenRequestHandler;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PathwayDiagramOpenRequestEvent extends GwtEvent<PathwayDiagramOpenRequestHandler> {
    public static final Type<PathwayDiagramOpenRequestHandler> TYPE = new Type<>();

    private Pathway pathway;

    public PathwayDiagramOpenRequestEvent(Pathway pathway) {
        this.pathway = pathway;
    }

    @Override
    public Type<PathwayDiagramOpenRequestHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PathwayDiagramOpenRequestHandler handler) {
        handler.onPathwayDiagramOpenRequest(this);
    }

    public Pathway getPathway() {
        return pathway;
    }

    @Override
    public String toString() {
        return "PathwayDiagramOpenRequestEvent{" +
                "pathway=" + pathway +
                '}';
    }
}
