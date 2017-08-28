package org.reactome.web.pwp.client.viewport.fireworks;

import com.google.gwt.core.client.Duration;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import org.reactome.web.diagram.events.DiagramObjectsFlagResetEvent;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.*;
import org.reactome.web.pwp.client.common.handlers.DatabaseObjectHoveredHandler;
import org.reactome.web.pwp.client.common.handlers.FireworksOpenedHandler;
import org.reactome.web.pwp.client.common.handlers.PathwayDiagramOpenRequestHandler;
import org.reactome.web.pwp.client.common.handlers.ViewportChangedHandler;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.viewport.ViewportToolType;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.classes.ReactionLikeEvent;
import org.reactome.web.pwp.model.client.classes.Species;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.web.pwp.model.client.util.Path;

import java.util.Objects;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksPresenter extends AbstractPresenter implements Fireworks.Presenter, DatabaseObjectHoveredHandler,
        FireworksOpenedHandler, ViewportChangedHandler, PathwayDiagramOpenRequestHandler {

    /**
     * Static files are cached by the browser, but we want to force download at least once per session
     * to avoid old fireworks layout views
     */
    private final double SESSION_STATIC_VERSION = Duration.currentTimeMillis();

    private boolean firstLoad = true;

    private Species currentSpecies;
    private Pathway selected;
    private AnalysisStatus analysisStatus;
    private String flag;

    private Fireworks.Display display;

    public FireworksPresenter(EventBus eventBus, Fireworks.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);

        this.eventBus.addHandler(DatabaseObjectHoveredEvent.TYPE, this);
        this.eventBus.addHandler(FireworksOpenedEvent.TYPE, this);
        this.eventBus.addHandler(PathwayDiagramOpenRequestEvent.TYPE, this);
        this.eventBus.addHandler(ViewportChangedEvent.TYPE, this);
    }

    @Override
    public void onDatabaseObjectHovered(DatabaseObjectHoveredEvent event) {
        if (event.getSource().equals(this)) return;
        Pathway toHighlight = null;
        if (event.getDatabaseObject() instanceof Pathway) {
            toHighlight = (Pathway) event.getDatabaseObject();
        } else {
            Path path = event.getPath();
            if (path != null && !path.isEmpty()) {
                toHighlight = (Pathway) path.get(path.size() - 1);
            }
        }
        if (this.display.isVisible()) {
            this.display.highlightPathway(toHighlight);
        }
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        this.analysisStatus = event.getState().getAnalysisStatus();

        Species toSpecies = event.getState().getSpecies();
        if (!toSpecies.equals(this.currentSpecies)) {
            this.currentSpecies = toSpecies;
            String speciesName = toSpecies.getDisplayName().replaceAll(" ", "_");
            loadSpeciesFireworks(speciesName);
        }

        DatabaseObject databaseObject = event.getState().getSelected();
        Pathway toSelect;
        if (databaseObject instanceof Pathway) {
            toSelect = (Pathway) databaseObject;
        } else {
            toSelect = event.getState().getPathway();
        }

        String flag = event.getState().getFlag();
        if(!Objects.equals(flag, this.flag)) {
            this.flag = flag;
            this.display.flag(this.flag);
        }

        if (this.display.isVisible()) {
            if (toSelect == null) {
                this.selected = null;
                this.display.resetSelection();
            } else if (firstLoad || (databaseObject != null && databaseObject instanceof ReactionLikeEvent)) {
                this.selected = toSelect;
                this.display.openPathway(toSelect);
            } else if (!toSelect.equals(this.selected)) {
                this.selected = toSelect;
                this.display.selectPathway(toSelect);
            }
            this.display.setAnalysisToken(this.analysisStatus);
        }else{
            this.selected = toSelect;
        }

        firstLoad = false;
    }

    @Override
    public void onViewportChanged(ViewportChangedEvent event) {
        if (event.getViewportTool().equals(ViewportToolType.FIREWORKS)) {
            this.display.selectPathway(this.selected);
            this.display.setAnalysisToken(this.analysisStatus);
        }
    }

    @Override
    public void selectPathway(final Long dbId) {
        if (this.selected != null && dbId.equals(this.selected.getDbId())) return;
        ContentClient.query(dbId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                selected = (Pathway) databaseObject;
                Selection selection = new Selection(selected, new Path());
                eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), FireworksPresenter.this);
            }

            @Override
            public void onContentClientException(Type type, String message) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("There was a problem loading the pathway with dbId:" + dbId), FireworksPresenter.this);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("There was a problem loading the pathway with dbId:" + dbId), FireworksPresenter.this);
            }
        });
    }

    @Override
    public void resetPathwaySelection() {
        if(this.selected!=null) {
            this.selected = null;
            Selection selection = new Selection(); //Empty selection is reset
            eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), this);
        }
    }

    @Override
    public void highlightPathway(final Long dbId) {
        ContentClient.query(dbId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(databaseObject), FireworksPresenter.this);
            }

            @Override
            public void onContentClientException(Type type, String message) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("There was a problem loading the pathway with dbId:" + dbId), FireworksPresenter.this);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("There was a problem loading the pathway with dbId:" + dbId), FireworksPresenter.this);
            }
        });
    }

    @Override
    public void profileChanged(String profileName) {
        //TODO
    }

    @Override
    public void resetAnalysis() {
        eventBus.fireEventFromSource(new AnalysisResetEvent(), this);
    }

    @Override
    public void resetFlag() {
        if (this.flag != null) {
            this.flag = null;
            //Using the DiagramObjectsFlagResetEvent for convenience
            eventBus.fireEventFromSource(new DiagramObjectsFlagResetEvent(), this);
        }
    }

    @Override
    public void resetPathwayHighlighting() {
        eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(), this);
    }

    @Override
    public void showPathwayDiagram(final Long dbId) {
        ContentClient.query(dbId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                eventBus.fireEventFromSource(new PathwayDiagramOpenedEvent((Pathway) databaseObject), FireworksPresenter.this);
            }

            @Override
            public void onContentClientException(Type type, String message) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("There was a problem loading the pathway with dbId:" + dbId), FireworksPresenter.this);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("There was a problem loading the pathway with dbId:" + dbId), FireworksPresenter.this);
            }
        });
    }

    private void loadSpeciesFireworks(String species) {
        this.display.resetView();
        String url = "/download/current/fireworks/" + species + ".json?t=" + SESSION_STATIC_VERSION;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            String json = response.getText();
                            display.loadSpeciesFireworks(json);
                            break;
                        default:
                            String errorMsg = "A problem has occurred while loading the pathways overview data. " + response.getStatusText();
                            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), FireworksPresenter.this);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String errorMsg = "A problem has occurred while loading the pathways overview data. " + exception.getMessage();
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), FireworksPresenter.this);
                }
            });
        } catch (RequestException ex) {
            String errorMsg = "A problem has occurred while connecting to the server. " + ex.getMessage();
            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), FireworksPresenter.this);
        }
    }

    @Override
    public void onFireworksOpened(FireworksOpenedEvent event) {
        this.selected = event.getPathway();
    }

    @Override
    public void onPathwayDiagramOpenRequest(PathwayDiagramOpenRequestEvent event) {
        this.display.openPathway(event.getPathway());
    }
}
