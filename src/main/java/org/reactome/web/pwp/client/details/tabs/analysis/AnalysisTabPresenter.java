package org.reactome.web.pwp.client.details.tabs.analysis;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.DatabaseObjectHoveredEvent;
import org.reactome.web.pwp.client.common.events.DatabaseObjectSelectedEvent;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.AnalysisResultTable;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ResourceChangedEvent;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.web.pwp.model.client.util.Path;

import java.util.Objects;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisTabPresenter extends AbstractPresenter implements AnalysisTab.Presenter {

    private AnalysisTab.Display display;
    private AnalysisStatus analysisStatus = new AnalysisStatus();
    private Pathway selected;

    public AnalysisTabPresenter(EventBus eventBus, AnalysisTab.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();

        //IMPORTANT: Analysis will always load on a performed analysis! It doesn't matter whether is is active or not
        //if(!state.getDetailsTab().equals(display.getDetailTabType())) return;

        AnalysisStatus analysisStatus = state.getAnalysisStatus().clone();
        if(analysisStatus.isEmpty()){
            if(!this.analysisStatus.isEmpty()) {
                this.selected = null;
                this.analysisStatus = analysisStatus;
                display.setInitialState();
            }
            return;
        }

        //Show the data
        DatabaseObject selected = state.getSelected();
        this.selected = (selected instanceof Pathway) ? (Pathway) selected : state.getPathway();

        display.selectPathway(this.selected);

        if(!analysisStatus.equals(this.analysisStatus)){
            if(this.analysisStatus.isEmpty()) display.showLoadingMessage();
            this.analysisStatus = analysisStatus;
            this.loadAnalysisData(analysisStatus.getToken(), analysisStatus.getResource());
        }
    }

    @Override
    public void onPathwayHovered(final Long dbId) {
        ContentClient.query(dbId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                Pathway pathway = (Pathway) databaseObject;
                if(!Objects.equals(pathway, AnalysisTabPresenter.this.selected)) {
                    eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(pathway), AnalysisTabPresenter.this);
                }
            }

            @Override
            public void onContentClientException(Type type, String message) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("Error retrieving data for " + dbId), AnalysisTabPresenter.this);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("Error retrieving data for " + dbId), AnalysisTabPresenter.this);
            }
        });
    }

    @Override
    public void onPathwayHoveredReset() {
        this.eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(), this);
    }

    @Override
    public void onPathwaySelected(final Long dbId) {
        ContentClient.query(dbId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                Pathway pathway = (Pathway) databaseObject;
                if(!Objects.equals(pathway, AnalysisTabPresenter.this.selected)) {
                    Selection selection = new Selection(pathway, new Path());
                    eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), AnalysisTabPresenter.this);
                }
            }

            @Override
            public void onContentClientException(Type type, String message) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("Error retrieving data for " + dbId), AnalysisTabPresenter.this);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("Error retrieving data for " + dbId), AnalysisTabPresenter.this);
            }
        });
    }

    @Override
    public void onResourceSelected(ResourceChangedEvent event) {
        this.eventBus.fireEventFromSource(event, this);
    }

    private void loadAnalysisData(final String token, final String resource){
        AnalysisClient.getResult(token, resource, AnalysisResultTable.PAGE_SIZE, 1, new AnalysisHandler.Result() {
            @Override
            public void onAnalysisResult(final AnalysisResult result, long time) {
                Long speciesId = result.getSummary().getSpecies();
                if (speciesId != null) {
                    ContentClient.query(result.getSummary().getSpecies(), new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
                        @Override
                        public void onObjectLoaded(DatabaseObject databaseObject) {
                            result.getSummary().setSpeciesName(databaseObject.getDisplayName());
                            display.clearSelection();
                            display.showResult(result, resource);
                            display.selectPathway(selected);
                        }

                        @Override
                        public void onContentClientException(Type type, String message) {
                            String errorMsg = "No species information available for '" + resource + "'. ERROR: " + message;
                            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), AnalysisTabPresenter.this);
                            display.setInitialState();
                        }

                        @Override
                        public void onContentClientError(ContentClientError error) {
                            eventBus.fireEventFromSource(new ErrorMessageEvent(error.getMessage()), AnalysisTabPresenter.this);
                            display.setInitialState();
                        }
                    });
                } else {
                    display.clearSelection();
                    display.showResult(result, resource);
                    display.selectPathway(selected);
                }
            }

            @Override
            public void onAnalysisError(AnalysisError error) {
                eventBus.fireEventFromSource(new ErrorMessageEvent(error.getMessages()), AnalysisTabPresenter.this);
                display.setInitialState();
            }

            @Override
            public void onAnalysisServerException(String message) {
                String errorMsg = "The requested detailed data for '" + resource + "' in the Analysis could not be received. ERROR: " + message;
                eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), AnalysisTabPresenter.this);
                display.setInitialState();
            }
        });
    }
}
