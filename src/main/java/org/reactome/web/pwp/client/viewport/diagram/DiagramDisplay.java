package org.reactome.web.pwp.client.viewport.diagram;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import org.reactome.web.diagram.client.DiagramFactory;
import org.reactome.web.diagram.client.DiagramViewer;
import org.reactome.web.diagram.events.*;
import org.reactome.web.diagram.handlers.*;
import org.reactome.web.pwp.client.Browser;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.viewport.ViewportDisplay;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramDisplay extends DockLayoutPanel implements Diagram.Display,
        DiagramLoadedHandler, DatabaseObjectSelectedHandler, DatabaseObjectHoveredHandler,
        AnalysisResetHandler, FireworksOpenedHandler {

    private Diagram.Presenter presenter;

    private DiagramViewer diagram;

    public DiagramDisplay() {
        super(Style.Unit.PX);

        DiagramFactory.CONSOLE_VERBOSE = Browser.VERBOSE;
        DiagramFactory.EVENT_BUS_VERBOSE = Browser.VERBOSE;
        DiagramFactory.SHOW_INFO = false;

        this.diagram = DiagramFactory.createDiagramViewer();
        this.diagram.asWidget().setStyleName(ViewportDisplay.RESOURCES.getCSS().viewportPanel());
        add(this.diagram);

        this.diagram.addDatabaseObjectSelectedHandler(this);
        this.diagram.addDatabaseObjectHoveredHandler(this);
        this.diagram.addDiagramLoadedHandler(this);
        this.diagram.addAnalysisResetHandler(this);
        this.diagram.addFireworksOpenedHandler(this);
    }

    @Override
    public void loadPathway(Pathway pathway) {
        this.diagram.loadDiagram(pathway.getIdentifier());
    }

    @Override
    public void highlight(DatabaseObject databaseObject) {
        if (databaseObject != null) {
            this.diagram.highlightItem(databaseObject.getDbId());
        } else {
            this.diagram.resetHighlight();
        }
    }

    @Override
    public void select(DatabaseObject databaseObject) {
        if (databaseObject == null) {
            this.diagram.resetSelection();
        } else {
            this.diagram.selectItem(databaseObject.getDbId());
        }
    }

    @Override
    public void setAnalysisToken(AnalysisStatus analysisStatus) {
        this.diagram.setAnalysisToken(analysisStatus.getToken(), analysisStatus.getResource());
    }

    @Override
    public void onAnalysisReset(AnalysisResetEvent event) {
        this.presenter.analysisReset();
    }

    @Override
    public void onDatabaseObjectSelected(DatabaseObjectSelectedEvent event) {
        if (event.getDatabaseObject() != null) {
            this.presenter.databaseObjectSelected(event.getDatabaseObject().getDbId());
        } else {
            this.presenter.databaseObjectSelected(null);
        }
    }

    @Override
    public void onDatabaseObjectHovered(DatabaseObjectHoveredEvent event) {
        if (event.getDatabaseObject() != null) {
            this.presenter.databaseObjectHovered(event.getDatabaseObject().getDbId());
        } else {
            this.presenter.databaseObjectHovered(null);
        }
    }

    @Override
    public void onDiagramLoaded(DiagramLoadedEvent event) {
        this.presenter.diagramLoaded(event.getContext().getContent().getDbId());
    }

    @Override
    public void onFireworksOpened(FireworksOpenedEvent event) {
        this.presenter.fireworksOpened(event.getPathwayId());
    }

    @Override
    public void setPresenter(Diagram.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            this.diagram.onResize();
        }
    }
}
