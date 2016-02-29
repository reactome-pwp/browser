package org.reactome.web.pwp.client.viewport.diagram;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import org.reactome.web.diagram.client.DiagramFactory;
import org.reactome.web.diagram.client.DiagramViewer;
import org.reactome.web.diagram.data.loader.GraphLoader;
import org.reactome.web.diagram.data.loader.LayoutLoader;
import org.reactome.web.diagram.events.*;
import org.reactome.web.diagram.handlers.*;
import org.reactome.web.pwp.client.AppConfig;
import org.reactome.web.pwp.client.Browser;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.viewport.ViewportDisplay;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramDisplay extends DockLayoutPanel implements Diagram.Display,
        DiagramLoadedHandler, GraphObjectSelectedHandler, GraphObjectHoveredHandler, DiagramObjectsFlagResetHandler,
        AnalysisResetHandler, FireworksOpenedHandler {

    private Diagram.Presenter presenter;

    private DiagramViewer diagram;

    public DiagramDisplay() {
        super(Style.Unit.PX);

        DiagramFactory.CONSOLE_VERBOSE = Browser.VERBOSE;
        DiagramFactory.EVENT_BUS_VERBOSE = Browser.VERBOSE;
        DiagramFactory.SHOW_INFO = false;
        DiagramFactory.WATERMARK = false;

        this.diagram = DiagramFactory.createDiagramViewer();
        this.diagram.asWidget().setStyleName(ViewportDisplay.RESOURCES.getCSS().viewportPanel());

        //When the pathway browser is configured to run on the Curator site, the Fireworks
        //is not available and the diagram data needs to be retrieved from the ContentService
        if (AppConfig.getIsCurator()) {
            DiagramFactory.SHOW_FIREWORKS_BTN = false;
            //Altering the loaders to retrieve data from the ContentService
            LayoutLoader.PREFIX = "/ContentService/diagram/layout/";
            LayoutLoader.SUFFIX = "";
            GraphLoader.PREFIX = "/ContentService/diagram/graph/";
            GraphLoader.SUFFIX = "";

            this.diagram.setVisible(false);
        }

        add(this.diagram);

        this.diagram.addDatabaseObjectSelectedHandler(this);
        this.diagram.addDatabaseObjectHoveredHandler(this);
        this.diagram.addDiagramLoadedHandler(this);
        this.diagram.addAnalysisResetHandler(this);
        this.diagram.addFireworksOpenedHandler(this);
        this.diagram.addDiagramObjectsFlagResetHandler(this);
    }

    @Override
    public void loadPathway(Pathway pathway) {
        this.diagram.loadDiagram(pathway.getIdentifier());
    }

    @Override
    public void flag(String flag) {
        if (flag == null) {
            this.diagram.resetFlaggedItems();
        } else {
            this.diagram.flagItems(flag);
        }
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
    public void onDiagramObjectsFlagReset(DiagramObjectsFlagResetEvent event) {
        this.presenter.resetFlag(event);
    }

    @Override
    public void onGraphObjectSelected(GraphObjectSelectedEvent event) {
        if (event.getGraphObject() != null) {
            this.presenter.databaseObjectSelected(event.getGraphObject().getDbId());
        } else {
            this.presenter.databaseObjectSelected(null);
        }
    }

    @Override
    public void onGraphObjectHovered(GraphObjectHoveredEvent event) {
        if (event.getGraphObject() != null) {
            this.presenter.databaseObjectHovered(event.getGraphObject().getDbId());
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
        this.diagram.setVisible(visible);
    }
}
