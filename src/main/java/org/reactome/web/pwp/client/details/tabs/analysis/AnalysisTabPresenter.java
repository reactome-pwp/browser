package org.reactome.web.pwp.client.details.tabs.analysis;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
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
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.Filter;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.FilterAppliedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.AnalysisResultTable;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.AnalysisFilterChangedEvent;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.GSAClient;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAException;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAFactory;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.GSAError;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Status;
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
        display.setSpecies(state.getSpecies().getDisplayName());

        if(!analysisStatus.equals(this.analysisStatus)){
            if(this.analysisStatus.isEmpty()) display.showLoadingMessage();
            this.analysisStatus = analysisStatus.clone();
            Filter filter = Filter.fromAnalysisStatus(this.analysisStatus);
            this.loadAnalysisData(analysisStatus.getToken(), filter);
        }
    }

    private void loadGSAReportLinks(String gsaToken) {
        if (gsaToken == null || gsaToken.isEmpty()) return;

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, GSAClient.URL_REPORTS_STATUS + "/" + gsaToken);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) {
                        try {
                            Status st = GSAFactory.getModelObject(Status.class, response.getText());
                            display.showGsaReports(st.getReports());
                        } catch (GSAException ignored) { }
                    } else {
                        try {
                            GSAError error = GSAFactory.getModelObject(GSAError.class, response.getText());
                            if (error != null) {
                                Console.error("Couldn't retrieve reports from ReactomeGSA -> [Status: " + error.getStatus() + ", Title: " + error.getTitle() + ", Detail: " + error.getDetail() + "]");
                            }
                        } catch (GSAException ignored) { }
                        display.showGsaReports(null);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Console.error("Couldn't retrieve reports from ReactomeGSA " + exception.getMessage());
                    display.showGsaReports(null);
                }
            });
        } catch (RequestException ex) {
            Console.error("Couldn't retrieve reports from ReactomeGSA " + ex.getMessage());
            display.showGsaReports(null);
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
    public void onFilterChanged(FilterAppliedEvent event) {
        this.eventBus.fireEventFromSource(new AnalysisFilterChangedEvent(event.getFilter()), this);
    }

    private void loadAnalysisData(final String token, final Filter filter){
        AnalysisClient.getResult(token, filter, AnalysisResultTable.PAGE_SIZE, 1, null, null, new AnalysisHandler.Result() {
            @Override
            public void onAnalysisResult(final AnalysisResult result, long time) {
                // load GSA reports only when gsa token is available
                loadGSAReportLinks(result.getSummary().getGsaToken());

                Long speciesId = result.getSummary().getSpecies();
                if (speciesId != null) {
                    ContentClient.query(result.getSummary().getSpecies(), new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
                        @Override
                        public void onObjectLoaded(DatabaseObject databaseObject) {
                            result.getSummary().setSpeciesName(databaseObject.getDisplayName());
                            display.clearSelection();
                            display.showResult(result, filter);
                            display.selectPathway(selected);
                        }

                        @Override
                        public void onContentClientException(Type type, String message) {
                            String errorMsg = "No species information available for '" + filter.getResource() + "'. ERROR: " + message;
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
                    display.showResult(result, filter);
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
                String errorMsg = "The requested detailed data for '" + filter.getResource() + "' in the Analysis could not be received. ERROR: " + message;
                eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), AnalysisTabPresenter.this);
                display.setInitialState();
            }
        });
    }
}
