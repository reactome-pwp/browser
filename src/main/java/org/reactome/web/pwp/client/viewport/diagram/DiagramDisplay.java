package org.reactome.web.pwp.client.viewport.diagram;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.diagram.client.DiagramFactory;
import org.reactome.web.diagram.client.DiagramViewer;
import org.reactome.web.diagram.client.OptionalWidget;
import org.reactome.web.diagram.client.ViewerContainer;
import org.reactome.web.diagram.data.loader.GraphLoader;
import org.reactome.web.diagram.data.loader.LayoutLoader;
import org.reactome.web.diagram.events.*;
import org.reactome.web.diagram.handlers.*;
import org.reactome.web.pwp.client.AppConfig;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.viewport.ViewportDisplay;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramDisplay extends DockLayoutPanel implements Diagram.Display,
        ContentLoadedHandler, GraphObjectSelectedHandler, GraphObjectHoveredHandler,
        DiagramObjectsFlaggedHandler, DiagramObjectsFlagResetHandler,
        AnalysisResetHandler, FireworksOpenedHandler, DiagramProfileChangedHandler, AnalysisProfileChangedHandler {

    private Diagram.Presenter presenter;

    private DiagramViewer diagram;

    public DiagramDisplay() {
        super(Style.Unit.PX);

        DiagramFactory.CONSOLE_VERBOSE = true;
        DiagramFactory.EVENT_BUS_VERBOSE = false;
        DiagramFactory.WATERMARK = false;
        OptionalWidget.INFO.setVisible(false);

        this.diagram = DiagramFactory.createDiagramViewer();
        this.diagram.asWidget().setStyleName(ViewportDisplay.RESOURCES.getCSS().viewportPanel());

        // adjust PharmGKB position in PWB
        findFirstChildElementByClassName(this.diagram.asWidget(), "pharmGKB").addClassName(ViewerContainer.RESOURCES.getCSS().pharmGKBAdjustPosition());

        //When the pathway browser is configured to run on the Curator site, the Fireworks
        //is not available and the diagram data needs to be retrieved from the ContentService
        if (AppConfig.getIsCurator()) {
            OptionalWidget.FIREWORKS.setVisible(false);
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
        this.diagram.addDiagramObjectsFlaggedHandler(this);

        this.diagram.addDiagramProfileChangedHandler(this);
        this.diagram.addAnalysisProfileChangedHandler(this);
    }


    public static Element findFirstChildElementByClassName(Widget w, String className) {
        return findFirstChildElementByClassName(w.getElement(), className);
    }

    private static Element findFirstChildElementByClassName(Element e, String className) {
        for (int i = 0; i != e.getChildCount(); ++i) {
            Node childNode = e.getChild(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                if (childElement.getClassName().contains(className))
                    return childElement;
                else if (childElement.hasChildNodes()) {
                    Element grandChildElement =
                            findFirstChildElementByClassName(
                                    childElement, className);
                    if (grandChildElement != null) return grandChildElement;
                }
            }
        }
        return null;
    }

    @Override
    public void loadPathway(Pathway pathway) {
        this.diagram.loadDiagram(pathway.getReactomeIdentifier());
    }

    @Override
    public void flag(String flag, Boolean includeInteractors) {
        if (flag == null) {
            this.diagram.resetFlaggedItems();
        } else {
            this.diagram.flagItems(flag, includeInteractors);
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
        this.diagram.setAnalysisToken(analysisStatus.getToken(), analysisStatus.getResultFilter());
    }

    @Override
    public void onAnalysisReset(AnalysisResetEvent event) {
        this.presenter.analysisReset();
    }

    @Override
    public void onDiagramObjectsFlagged(DiagramObjectsFlaggedEvent event) {
        this.presenter.diagramFlagPerformed(event.getTerm(), event.getIncludeInteractors());
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
    public void onContentLoaded(ContentLoadedEvent event) {
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

    @Override
    public void onAnalysisProfileChanged(AnalysisProfileChangedEvent event) {
        presenter.analysisProfileChanged(event);
    }

    @Override
    public void onDiagramProfileChanged(DiagramProfileChangedEvent event) {
        presenter.diagramProfileChanged(event);
    }
}
