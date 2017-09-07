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
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.web.pwp.model.client.util.Ancestors;
import org.reactome.web.pwp.model.client.util.Path;

import java.util.List;
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
        if (timer != null && timer.isRunning()) timer.cancel();
        if (dbId != null) {
            ContentClient.query(dbId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
                @Override
                public void onObjectLoaded(DatabaseObject databaseObject) {
                    Selection selection = new Selection(pathway, databaseObject, path);
                    eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), DiagramPresenter.this);
                }

                @Override
                public void onContentClientException(Type type, String message) {
                    String errorMsg = "An error has occurred while retrieving data for " + dbId;
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DiagramPresenter.this);
                }

                @Override
                public void onContentClientError(ContentClientError error) {
                    String errorMsg = "An error has occurred while retrieving data for " + dbId;
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DiagramPresenter.this);
                }
            });
        } else {
            if (selected != null) {
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
        if (Objects.equals(this.hovered, dbId)) return;
        this.hovered = dbId;
        if (timer != null && timer.isRunning()) timer.cancel();
        if (dbId != null) {
            timer = new Timer() {
                @Override
                public void run() {
                    ContentClient.query(dbId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
                        @Override
                        public void onObjectLoaded(DatabaseObject databaseObject) {
                            if(!Objects.equals(selected, databaseObject)) {
                                eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(databaseObject), DiagramPresenter.this);
                            }
                        }

                        @Override
                        public void onContentClientException(Type type, String message) {
                            String errorMsg = "An error has occurred while retrieving data for " + dbId;
                            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DiagramPresenter.this);
                        }

                        @Override
                        public void onContentClientError(ContentClientError error) {
                            String errorMsg = "An error has occurred while retrieving data for " + dbId;
                            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DiagramPresenter.this);
                        }
                    });
                }
            };
            timer.schedule(HOVER_DELAY);
        } else {
            this.eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(), DiagramPresenter.this);
        }
    }

    @Override
    public void fireworksOpened(final Long dbId) {
        ContentClient.query(dbId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                Pathway pathway = (Pathway) databaseObject;
                eventBus.fireEventFromSource(new FireworksOpenedEvent(pathway), DiagramPresenter.this);
            }

            @Override
            public void onContentClientException(Type type, String message) {
                //TODO
//                String errorMsg = "An error has occurred while retrieving data for " + dbId;
//                eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), DiagramPresenter.this);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                //TODO
//                String errorMsg = "An error has occurred while retrieving data for " + dbId;
//                eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, error), DiagramPresenter.this);
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
        ContentClient.query(dbId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                displayedPathway = (Pathway) databaseObject;
                if (Objects.equals(pathway, displayedPathway)) {
                    updateView();
                } else {
                    ContentClient.getAncestors(pathway, new AncestorsLoaded() {
                        @Override
                        public void onAncestorsLoaded(Ancestors ancestors) {
                            List<Path> paths = ancestors.getPathsContaining(pathway);
                            Path path = !paths.isEmpty() ? paths.get(0) : new Path();
                            pathway = displayedPathway;
                            Selection selection = new Selection(pathway, path);
                            eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), DiagramPresenter.this);
                        }

                        @Override
                        public void onContentClientException(Type type, String message) {
                            eventBus.fireEventFromSource(new ErrorMessageEvent(message), DiagramPresenter.this);
                        }

                        @Override
                        public void onContentClientError(ContentClientError error) {
                            eventBus.fireEventFromSource(new ErrorMessageEvent(error.getMessage()), DiagramPresenter.this);
                        }
                    });
                }
            }

            @Override
            public void onContentClientException(Type type, String message) {
                displayedPathway = null;
            }

            @Override
            public void onContentClientError(ContentClientError error) {
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
