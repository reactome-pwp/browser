package org.reactome.web.pwp.client.details.tabs.structures;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.ReferenceSequence;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectsCreatedHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StructuresTabPresenter extends AbstractPresenter implements StructuresTab.Presenter {

    private StructuresTab.Display display;
    private DatabaseObject currentlyShown;

    public StructuresTabPresenter(EventBus eventBus, StructuresTab.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();
        DatabaseObject target = state.getTarget();

        //Is it me the one to show data?
        if (!state.getDetailsTab().equals(display.getDetailTabType())){
            display.updateTitle(target);
            return;
        }

        //Show the data
        if(target==null){
            this.currentlyShown = null;
            display.setInitialState();
        }else if(!target.equals(this.currentlyShown)){
            this.currentlyShown = target;
            display.showDetails(target);
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
                                eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, ex), StructuresTabPresenter.this);
                            }
                            break;
                        default:
                            String errorMsg = "There was an error processing the request. ERROR: " + response.getStatusText();
                            display.showErrorMessage(errorMsg);
                            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), StructuresTabPresenter.this);
                    }

                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String errorMsg = "The request for '" + databaseObject.getDisplayName() + "' in the Expression Tab received an error instead of a valid response.";
                    display.showErrorMessage(errorMsg);
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), StructuresTabPresenter.this);
                }
            });
        } catch (RequestException ex) {
            String errorMsg = "The required data for the Expression of '" + databaseObject.getDisplayName() + "' could not be received";
            display.showErrorMessage(errorMsg);
            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, ex), StructuresTabPresenter.this);
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
                        eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), StructuresTabPresenter.this);
                    }
                }

                @Override
                public void onDatabaseObjectError(Throwable exception) {
                    String errorMsg = "There is not information available for " + databaseObject.getDisplayName();
                    display.showErrorMessage(errorMsg);
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), StructuresTabPresenter.this);
                }
            });

        }
    }
}
