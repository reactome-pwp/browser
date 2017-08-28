package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.PathwayDiagramOpenedHandler;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PathwayDiagramOpenedEvent extends GwtEvent<PathwayDiagramOpenedHandler> {
    public static final Type<PathwayDiagramOpenedHandler> TYPE = new Type<>();

    private Pathway pathway;

    public PathwayDiagramOpenedEvent(Pathway pathway) {
        this.pathway = pathway;
    }

    @Override
    public Type<PathwayDiagramOpenedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PathwayDiagramOpenedHandler handler) {
        handler.onPathwayDiagramOpened(this);
    }

    public Pathway getPathway() {
        return pathway;
    }

    @Override
    public String toString() {
        return "PathwayDiagramOpenedEvent{" +
                "pathway=" + pathway +
                '}';
    }
}
