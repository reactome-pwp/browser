package org.reactome.web.pwp.client.details.tabs.analysis;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.pwp.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisResult;
import org.reactome.web.pwp.client.common.events.DatabaseObjectHoveredEvent;
import org.reactome.web.pwp.client.common.events.DatabaseObjectSelectedEvent;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectCreatedHandler;
import org.reactome.web.pwp.model.util.Path;

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

        AnalysisStatus analysisStatus = state.getAnalysisStatus();
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
            this.analysisStatus = analysisStatus;
            //Load analysis and select the pathway!!
            display.showLoadingMessage();
            this.loadAnalysisData();
        }
    }

    @Override
    public void onPathwayHovered(final Long dbId) {
        DatabaseObjectFactory.get(dbId, new DatabaseObjectCreatedHandler() {
            @Override
            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                Pathway pathway = (Pathway) databaseObject;
                if(!Objects.equals(pathway, AnalysisTabPresenter.this.selected)) {
                    eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(pathway), AnalysisTabPresenter.this);
                }
            }

            @Override
            public void onDatabaseObjectError(Throwable exception) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("Error retrieving data for " + dbId, exception), AnalysisTabPresenter.this);
            }
        });
    }

    @Override
    public void onPathwayHoveredReset() {
        this.eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(), this);
    }

    @Override
    public void onPathwaySelected(final Long dbId) {
        DatabaseObjectFactory.get(dbId, new DatabaseObjectCreatedHandler() {
            @Override
            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                Pathway pathway = (Pathway) databaseObject;
                if(!Objects.equals(pathway, AnalysisTabPresenter.this.selected)) {
                    Selection selection = new Selection(pathway, new Path());
                    eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), AnalysisTabPresenter.this);
                }
            }

            @Override
            public void onDatabaseObjectError(Throwable exception) {
                eventBus.fireEventFromSource(new ErrorMessageEvent("Error retrieving data for " + dbId, exception), AnalysisTabPresenter.this);
            }
        });
    }

    @Override
    public void onResourceSelected(String resource) {
        this.analysisStatus.setResource(resource);
        System.out.println("AnalysisTabPresenter (116) >> resource: " + resource);
        //TODO: Notify the change
        this.loadAnalysisData();
    }

    private void loadAnalysisData(){
        final String token = analysisStatus.getToken();
        final String resource = analysisStatus.getResource();
        String url = AnalysisHelper.URL_PREFIX + "/token/" + token + "?page=1&resource=" + resource;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    final AnalysisResult result;
                    try {
                        result = AnalysisModelFactory.getModelObject(AnalysisResult.class, response.getText());
                    } catch (AnalysisModelException e) {
                        String errorMsg = "The received object for '" + resource + "' is empty or faulty and could not be parsed. ERROR: " + e.getMessage();
                        eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, e), AnalysisTabPresenter.this);
                        display.setInitialState();
                        return;
                    }
                    Long speciesId = result.getSummary().getSpecies();
                    if(speciesId!=null) {
                        DatabaseObjectFactory.get(result.getSummary().getSpecies(), new DatabaseObjectCreatedHandler() {
                            @Override
                            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                                result.getSummary().setSpeciesName(databaseObject.getDisplayName());
                                display.clearSelection();
                                display.showResult(result, resource);
                                display.selectPathway(selected);
                            }

                            @Override
                            public void onDatabaseObjectError(Throwable exception) {
                                String errorMsg = "No species information available for '" + resource + "'. ERROR: " + exception.getMessage();
                                eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), AnalysisTabPresenter.this);
                                display.setInitialState();
                            }
                        });
                    }else{
                        display.clearSelection();
                        display.showResult(result, resource);
                        display.selectPathway(selected);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String errorMsg = "The request for '" + resource + "' received an error instead of a valid response. ERROR: " + exception.getMessage();
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), AnalysisTabPresenter.this);
                    display.setInitialState();
                }
            });
        }catch (RequestException ex) {
            String errorMsg = "The requested detailed data for '" + resource + "' in the Analysis could not be received. ERROR: " + ex.getMessage();
            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, ex), AnalysisTabPresenter.this);
            display.setInitialState();
        }
    }
}
