package org.reactome.web.pwp.client.details.tabs.downloads;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import org.reactome.web.diagram.events.AnalysisProfileChangedEvent;
import org.reactome.web.diagram.events.DiagramProfileChangedEvent;
import org.reactome.web.diagram.handlers.AnalysisProfileChangedHandler;
import org.reactome.web.diagram.handlers.DiagramProfileChangedHandler;
import org.reactome.web.diagram.profiles.analysis.AnalysisColours;
import org.reactome.web.diagram.profiles.diagram.DiagramColours;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DownloadsTabPresenter extends AbstractPresenter implements DownloadsTab.Presenter,
        DiagramProfileChangedHandler, AnalysisProfileChangedHandler {

    private DownloadsTab.Display display;
    private DatabaseObject currentlyShown;
    private AnalysisStatus currentlyShownAnalysis;
    private DatabaseObject currentlySelected;
    private String currentlyFlagged;

    public DownloadsTabPresenter(EventBus eventBus, DownloadsTab.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
        this.requestDBName();

        eventBus.addHandler(DiagramProfileChangedEvent.TYPE, this);
        eventBus.addHandler(AnalysisProfileChangedEvent.TYPE, this);
    }

    @Override
    public void swapToMolecules(Pathway pathway) {
//        //Molecules Tab needs to be loaded so this is necessary here, eventhough it is not the best way to do it
//        eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_DETAILS_TAB_SELECTED, DetailsTabType.PARTICIPATING_MOLECULES);
//        eventBus.fireELVEvent(ELVEventType.MOLECULES_DOWNLOAD_REQUIRED, pathway);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();

        //Is it me the one to show data?
        if(!state.getDetailsTab().equals(display.getDetailTabType())) return;

        //Get the analysisStatus from the state
        final AnalysisStatus analysisStatus = state.getAnalysisStatus();
        display.setAnalysisStatus(analysisStatus);

        //Get the selected entity
        final DatabaseObject selected = state.getSelected();
        display.setSelected(selected);

        //Get the flagged entity
        final String flag = state.getFlag();
        display.setFlag(flag);

        display.setDiagramProfile(DiagramColours.get().PROFILE.getName());
        display.setAnalysisProfile(AnalysisColours.get().PROFILE.getName());

        //Show the data
        final DatabaseObject databaseObject = state.getPathway(); //IMPORTANT! We only show information related to the selected Pathway!
        //noinspection Duplicates
        if (databaseObject == null) {
            currentlyShown = null;
            display.setInitialState();
        } else if (!databaseObject.equals(currentlyShown)
                || !analysisStatus.equals(currentlyShownAnalysis)
                || databaseObject != currentlySelected
                || !areEqual(flag, currentlyFlagged)) {
            //The download tab should be updated in case of any analysis-related state change
            databaseObject.load(new ContentClientHandler.ObjectLoaded() {
                @Override
                public void onObjectLoaded(DatabaseObject databaseObject) {
                    currentlyShown = databaseObject;
                    currentlyShownAnalysis = analysisStatus;
                    currentlySelected = selected;
                    currentlyFlagged = flag;

                    display.showDetails(databaseObject);
                }

                @Override
                public void onContentClientException(Type type, String message) {
                    currentlyShown = null;
                    eventBus.fireEventFromSource(new ErrorMessageEvent(databaseObject.getDisplayName() + " details could not be retrieved from the server."), this);
                }

                @Override
                public void onContentClientError(ContentClientError error) {
                    currentlyShown = null;
                    eventBus.fireEventFromSource(new ErrorMessageEvent(databaseObject.getDisplayName() + " details could not be retrieved from the server."), this);
                }
            });
        }
    }

    @Override
    public void onAnalysisProfileChanged(AnalysisProfileChangedEvent event) {
        display.setAnalysisProfile(event.getAnalysisProfile().getName());
        display.showDetails(currentlyShown);
    }

    @Override
    public void onDiagramProfileChanged(DiagramProfileChangedEvent event) {
        display.setDiagramProfile(event.getDiagramProfile().getName());
        display.showDetails(currentlyShown);
    }

    private void requestDBName() {
        String url = ContentClient.SERVER + ContentClient.CONTENT_SERVICE + "/data/database/name";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            String name = response.getText().trim();
                            display.setDbName(name);
                            break;
                        default:
                            String errorMsg = "Error retrieving the database name. ERROR " + response.getStatusText();
                            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DownloadsTabPresenter.this);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String errorMsg = "The database name could not be retrieved.";
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DownloadsTabPresenter.this);
                }
            });
        } catch (RequestException ex) {
            String errorMsg = "The database name could not be retrieved.";
            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DownloadsTabPresenter.this);
        }
    }

    private boolean areEqual(String a, String b) {
        boolean rtn;
        if (a == null) {
            rtn = b == null;
        } else {
            rtn = a.equals(b);
        }
        return rtn;
    }
}
