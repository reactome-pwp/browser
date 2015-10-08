package org.reactome.web.pwp.client.viewport.diagram;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import org.reactome.web.diagram.events.DiagramObjectsFlagResetEvent;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.*;
import org.reactome.web.pwp.client.common.handlers.DatabaseObjectHoveredHandler;
import org.reactome.web.pwp.client.common.handlers.ViewportChangedHandler;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.client.viewport.ViewportToolType;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectCreatedHandler;
import org.reactome.web.pwp.model.util.Path;

import java.util.Objects;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramPresenter extends AbstractPresenter implements Diagram.Presenter, ViewportChangedHandler,
        DatabaseObjectHoveredHandler {

    private Diagram.Display display;

    private Pathway displayedPathway;

    private Pathway pathway;
    private DatabaseObject selected;
    private Path path;
    private AnalysisStatus analysisStatus = new AnalysisStatus();
    private Long hovered;
    private String flag;

    public DiagramPresenter(EventBus eventBus, Diagram.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
        this.path = new Path();

        this.eventBus.addHandler(ViewportChangedEvent.TYPE, this);
        this.eventBus.addHandler(DatabaseObjectHoveredEvent.TYPE, this);
    }

    @Override
    public void analysisReset() {
        eventBus.fireEventFromSource(new AnalysisResetEvent(), this);
    }

    @Override
    public void databaseObjectSelected(final Long dbId) {
        if(dbId!=null){
            DatabaseObjectFactory.get(dbId, new DatabaseObjectCreatedHandler() {
                @Override
                public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                    Selection selection = new Selection(pathway, databaseObject, path);
                    eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), DiagramPresenter.this);
                }

                @Override
                public void onDatabaseObjectError(Throwable exception) {
                    String errorMsg = "An error has occurred while retrieving data for " + dbId;
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DiagramPresenter.this);
                }
            });
        }else{
            if(selected!=null) {
                this.selected = null;
                Selection selection = new Selection(this.pathway, this.path);
                this.eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), DiagramPresenter.this);
            }
        }
    }

    @Override
    public void onDatabaseObjectHovered(DatabaseObjectHoveredEvent event) {
        Long hovered = event.getDatabaseObject() != null ? event.getDatabaseObject().getDbId() : null;
        if(!Objects.equals(hovered, this.hovered)) {
            this.hovered = hovered;
            this.display.highlight(event.getDatabaseObject());
        }
    }

    private static final int HOVER_DELAY = 500;
    private Timer timer ;

    @Override
    public void databaseObjectHovered(final Long dbId) {
        this.hovered = dbId;
        if(timer!=null && timer.isRunning()) timer.cancel();
        if(dbId!=null){
            timer = new Timer() {
                @Override
                public void run() {
                    DatabaseObjectFactory.get(dbId, new DatabaseObjectCreatedHandler() {
                        @Override
                        public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                            eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(databaseObject), DiagramPresenter.this);
                        }

                        @Override
                        public void onDatabaseObjectError(Throwable exception) {
                            String errorMsg = "An error has occurred while retrieving data for " + dbId;
                            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DiagramPresenter.this);
                        }
                    });
                }
            };
            timer.schedule(HOVER_DELAY);
        }else{
            this.eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(), DiagramPresenter.this);
        }
    }

    @Override
    public void fireworksOpened(final Long dbId) {
        DatabaseObjectFactory.get(dbId, new DatabaseObjectCreatedHandler() {
            @Override
            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                Pathway pathway = (Pathway) databaseObject;
                eventBus.fireEventFromSource(new FireworksOpenedEvent(pathway), DiagramPresenter.this);
            }

            @Override
            public void onDatabaseObjectError(Throwable exception) {
                String errorMsg = "An error has occurred while retrieving data for " + dbId;
                eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), DiagramPresenter.this);
            }
        });

    }

    @Override
    public void resetFlag(DiagramObjectsFlagResetEvent event) {
        this.eventBus.fireEventFromSource(event, this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();
        boolean isNewDiagram = !Objects.equals(this.pathway, state.getPathway());
        this.pathway = state.getPathway();
        this.selected = state.getSelected();
        this.path = state.getPath();
        this.analysisStatus = state.getAnalysisStatus();
        this.flag = state.getFlag();
        if(this.display.isVisible()) {
            if (isNewDiagram) {
                this.loadCurrentPathway();
            } else {
                updateView();
            }
        }
    }

    @Override
    public void onViewportChanged(ViewportChangedEvent event) {
        if(event.getViewportTool().equals(ViewportToolType.DIAGRAM)) {
            this.loadCurrentPathway();
        }
    }

    @Override
    public void diagramLoaded(final Long dbId) {
        DatabaseObjectFactory.get(dbId, new DatabaseObjectCreatedHandler() {
            @Override
            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                displayedPathway = (Pathway) databaseObject;
                if (Objects.equals(DiagramPresenter.this.pathway, displayedPathway)) {
                    updateView();
                } else {
                    DiagramPresenter.this.pathway = displayedPathway;
                    Selection selection = new Selection(pathway, new Path());
                    eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), DiagramPresenter.this);
                }
            }

            @Override
            public void onDatabaseObjectError(Throwable exception) {
                displayedPathway = null;
            }
        });
    }

    private void loadCurrentPathway(){
        if (this.pathway == null) {
            Console.warn("Undetermined pathway...", this);
        } else if (!Objects.equals(pathway, displayedPathway)) {
            this.display.loadPathway(this.pathway);
        } else {
            updateView();
        }
    }

    private void updateView(){
        this.display.select(this.selected);
        display.setAnalysisToken(analysisStatus);
        display.flag(this.flag);
    }
}
