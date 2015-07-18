package org.reactome.web.pwp.client.details.tabs.expression;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.classes.*;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectsCreatedHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionTabPresenter extends AbstractPresenter implements ExpressionTab.Presenter {

    private ExpressionTab.Display display;
    private DatabaseObject currentlyShown;

    public ExpressionTabPresenter(EventBus eventBus, ExpressionTab.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();

        //Is it me the one to show data?
        if (!state.getDetailsTab().equals(display.getDetailTabType())) return;

        //Show the data
        DatabaseObject databaseObject = state.getSelected();
        if (databaseObject instanceof Pathway) {
            if(!databaseObject.equals(this.currentlyShown)) {
                this.currentlyShown = databaseObject;
                this.display.showPathway((Pathway) databaseObject);
            }
        } else if (databaseObject instanceof PhysicalEntity || databaseObject instanceof Event) {
            if(!databaseObject.equals(this.currentlyShown)) {
                this.currentlyShown = databaseObject;
                this.display.showProteins(databaseObject);
            }
        } else if (state.getPathway() != null) {
            Pathway pathway = state.getPathway();
            if(!pathway.equals(this.currentlyShown)) {
                this.currentlyShown = pathway;
                this.display.showPathway(pathway);
            }
        } else {
            this.currentlyShown = null;
            this.display.setInitialState();
        }
    }

    @Override
    public void getReferenceSequences(final DatabaseObject databaseObject) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/referenceEntity/" + databaseObject.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            try {
                                JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();
                                List<ReferenceSequence> referenceSequenceList = new LinkedList<>();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject json = jsonArray.get(i).isObject();
                                    DatabaseObject databaseObject = DatabaseObjectFactory.create(json);
                                    if (databaseObject instanceof ReferenceSequence) {
                                        referenceSequenceList.add((ReferenceSequence) databaseObject);
                                    }
                                }
                                processReferenceSequences(databaseObject, referenceSequenceList);
                            } catch (Exception ex) {
                                String errorMsg =  "The received data for the reference entities of '" + databaseObject.getDisplayName() + "' is empty or faulty and could not be parsed.";
                                display.showErrorMessage(errorMsg);
                                eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, ex), ExpressionTabPresenter.this);
                            }
                            break;
                        default:
                            String errorMsg = "There was an error processing the request. ERROR: " + response.getStatusText();
                            display.showErrorMessage(errorMsg);
                            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), ExpressionTabPresenter.this);
                    }

                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String errorMsg = "The request for '" + databaseObject.getDisplayName() + "' in the Expression Tab received an error instead of a valid response.";
                    display.showErrorMessage(errorMsg);
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), ExpressionTabPresenter.this);
                }
            });
        } catch (RequestException ex) {
            String errorMsg = "The required data for the Expression of '" + databaseObject.getDisplayName() + "' could not be received";
            display.showErrorMessage(errorMsg);
            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, ex), ExpressionTabPresenter.this);
        }
    }

    private void processReferenceSequences(final DatabaseObject databaseObject, List<ReferenceSequence> referenceSequenceList){
        if (referenceSequenceList.isEmpty()) {
            display.showReferenceSequences(databaseObject, referenceSequenceList);
        } else {
            DatabaseObjectFactory.get(referenceSequenceList, new DatabaseObjectsCreatedHandler() {
                @Override
                public void onDatabaseObjectsLoaded(Map<String, DatabaseObject> databaseObjects) {
                    List<ReferenceSequence> rtn = new LinkedList<>();
                    for (DatabaseObject object : databaseObjects.values()) {
                        if (object instanceof ReferenceSequence) {
                            rtn.add((ReferenceSequence) object);
                        }
                    }
                    if (!rtn.isEmpty()) {
                        display.showReferenceSequences(databaseObject, rtn);
                    } else {
                        String errorMsg = "There is not information available for " + databaseObject.getDisplayName();
                        display.showErrorMessage(errorMsg);
                        eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), ExpressionTabPresenter.this);
                    }
                }

                @Override
                public void onDatabaseObjectError(Throwable exception) {
                    String errorMsg = "There is not information available for " + databaseObject.getDisplayName();
                    display.showErrorMessage(errorMsg);
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), ExpressionTabPresenter.this);
                }
            });

        }
    }
}
