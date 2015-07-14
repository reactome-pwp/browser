package org.reactome.web.pwp.client.details.tabs.processes.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.Controller;
import org.reactome.web.pwp.client.common.EventBus;
import org.reactome.web.pwp.client.common.data.factory.ModelFactory;
import org.reactome.web.pwp.model.classes.*;
import org.reactome.web.pwp.client.common.events.ELVEventType;
import org.reactome.web.pwp.client.common.model.Ancestors;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.DetailsTabView;
import org.reactome.web.pwp.client.details.tabs.processes.view.ProcessesView;
import org.reactome.web.pwp.client.manager.messages.MessageObject;
import org.reactome.web.pwp.client.manager.messages.MessageType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ProcessesPresenter extends Controller implements ProcessesView.Presenter {

    private ProcessesView view;

    public ProcessesPresenter(EventBus eventBus, ProcessesView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public DetailsTabView getView() {
        return this.view;
    }

    @Override
    public void setInstancesInitialState() {
        view.setInitialState();
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        view.showInstanceDetails(pathway, databaseObject);
    }

    @Override
    public void showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        this.view.showInstanceDetailsIfExists(pathway, databaseObject);
    }

    @Override
    public void getComplexesContaining(final PhysicalEntity physicalEntity) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/listByQuery/Complex";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            String requestData = "hasComponent=" + physicalEntity.getDbId();
            requestBuilder.sendRequest(requestData, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        List<Complex> complexList = new ArrayList<Complex>();
                        for(int i=0; i<list.size(); ++i){
                            Complex complex = (Complex) ModelFactory.getDatabaseObject(list.get(i).isObject());
                            complexList.add(complex);
                        }
                        view.setComplexesForPhysicalEntity(physicalEntity, complexList);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data about '" +
                                physicalEntity.getDisplayName() + "'\nfor Processes Tab is empty or faulty " +
                                "and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setComplexesForPhysicalEntity(physicalEntity, new ArrayList<Complex>());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    MessageObject msgObj = new MessageObject("The request for '" + physicalEntity.getDisplayName() +
                            "' in the Processes Tab received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    Console.error(getClass() + " ERROR: " + exception.getMessage());
                    view.setComplexesForPhysicalEntity(physicalEntity, new ArrayList<Complex>());
                }
            });
        }
        catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The required data for the Processes of\n'" +
                    physicalEntity.getDisplayName() + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            view.setComplexesForPhysicalEntity(physicalEntity, new ArrayList<Complex>());
        }
    }

    @Override
    public void getDetailedData(final DatabaseObject databaseObject) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/detailedView/DatabaseObject/" + databaseObject.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        String text = response.getText();
                        JSONObject json = JSONParser.parseStrict(text).isObject();
                        DatabaseObject databaseObject = ModelFactory.getDatabaseObject(json);
                        view.setDetailedData(databaseObject);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data about '" +
                                databaseObject.getDisplayName() + "'\nfor Processes Tab is empty or faulty " +
                                "and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setDetailedData(databaseObject);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                        MessageObject msgObj = new MessageObject("The request for '" + databaseObject.getDisplayName() +
                                "' in the Processes Tab received an error instead of a valid response.\n" +
                                "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }
                    view.setDetailedData(databaseObject);
                }
            });
        } catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The required data for the Processes of\n'" +
                    databaseObject.getDisplayName() + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            view.setDetailedData(databaseObject);
        }
    }

    @Override
    public void getEntitySetsContaining(final PhysicalEntity physicalEntity) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/listByQuery/EntitySet";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            String requestData = "hasMember=" + physicalEntity.getDbId();
            requestBuilder.sendRequest(requestData, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        List<EntitySet> entitySetList = new ArrayList<EntitySet>();
                        for(int i=0; i<list.size(); ++i){
                            EntitySet entitySet = (EntitySet) ModelFactory.getDatabaseObject(list.get(i).isObject());
                            entitySetList.add(entitySet);
                        }
                        view.setEntitySetsForPhysicalEntity(physicalEntity, entitySetList);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data about '" +
                                physicalEntity.getDisplayName() + "'\nfor Processes Tab is empty or faulty " +
                                "and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setEntitySetsForPhysicalEntity(physicalEntity, new ArrayList<EntitySet>());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    MessageObject msgObj = new MessageObject("The request for '" + physicalEntity.getDisplayName() +
                            "' in the Processes Tab received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    Console.error(getClass() + " ERROR: " + exception.getMessage());
                    view.setEntitySetsForPhysicalEntity(physicalEntity, new ArrayList<EntitySet>());
                }
            });
        }
        catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The required data for the Processes of\n'" +
                    physicalEntity.getDisplayName() + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            view.setEntitySetsForPhysicalEntity(physicalEntity, new ArrayList<EntitySet>());
        }
    }

    @Override
    public void getPathwaysForEntities(final PhysicalEntity physicalEntity) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/pathwaysForEntities";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            String requestData = "ID=" + physicalEntity.getDbId();
            requestBuilder.sendRequest(requestData, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        List<Pathway> pathways = new ArrayList<Pathway>();
                        for(int i=0; i<list.size(); ++i){
                            Pathway pathway = (Pathway) ModelFactory.getDatabaseObject(list.get(i).isObject());
                            pathways.add(pathway);
                        }
                        view.setPathwayForPhysicalEntity(physicalEntity, pathways);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data about '" +
                                physicalEntity.getDisplayName() + "'\nfor Processes Tab is empty or faulty " +
                                "and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setPathwayForPhysicalEntity(physicalEntity, new ArrayList<Pathway>());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    MessageObject msgObj = new MessageObject("The request for '" + physicalEntity.getDisplayName() +
                            "' in the Processes Tab received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    Console.error(getClass() + " ERROR: " + exception.getMessage());
                    view.setPathwayForPhysicalEntity(physicalEntity, new ArrayList<Pathway>());
                }
            });
        }
        catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The required data for the Processes of\n'" +
                    physicalEntity.getDisplayName() + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            view.setPathwayForPhysicalEntity(physicalEntity, new ArrayList<Pathway>());
        }
    }

    @Override
    public void getPathwaysForEvent(final Event event) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryEventAncestors/" + event.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        Ancestors ancestors = new Ancestors(list);
                        view.setPathwayForEvent(event, ancestors.getPathways());
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data about '" +
                                event.getDisplayName() + "'\nfor Processes Tab is empty or faulty " +
                                "and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setPathwayForEvent(event, new LinkedList<Pathway>());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    MessageObject msgObj = new MessageObject("The request for '" + event.getDisplayName() +
                            "' in the Processes Tab received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    Console.error(getClass() + " ERROR: " + exception.getMessage());
                    view.setPathwayForEvent(event, new LinkedList<Pathway>());
                }
            });
        }
        catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The required data for the Processes of\n'" +
                    event.getDisplayName() + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            view.setPathwayForEvent(event, new LinkedList<Pathway>());
        }
    }

    @Override
    public void getReactionsWhereInput(final PhysicalEntity physicalEntity) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/listByQuery/ReactionlikeEvent";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            String requestData = "input=" + physicalEntity.getDbId();
            requestBuilder.sendRequest(requestData, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        List<ReactionLikeEvent> reactionList = new ArrayList<ReactionLikeEvent>();
                        for(int i=0; i<list.size(); ++i){
                            ReactionLikeEvent reaction = (ReactionLikeEvent) ModelFactory.getDatabaseObject(list.get(i).isObject());
                            reactionList.add(reaction);
                        }
                        view.setReactionsWhereInput(physicalEntity, reactionList);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data about '" +
                                physicalEntity.getDisplayName() + "'\nfor Processes Tab is empty or faulty " +
                                "and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setReactionsWhereInput(physicalEntity, new ArrayList<ReactionLikeEvent>());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    MessageObject msgObj = new MessageObject("The request for '" + physicalEntity.getDisplayName() +
                            "' in the Processes Tab received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    Console.error(getClass() + " ERROR: " + exception.getMessage());
                    view.setReactionsWhereInput(physicalEntity, new ArrayList<ReactionLikeEvent>());
                }
            });
        }
        catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The required data for the Processes of\n'" +
                    physicalEntity.getDisplayName() + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            view.setReactionsWhereInput(physicalEntity, new ArrayList<ReactionLikeEvent>());
        }
    }

    @Override
    public void getReactionsWhereOutput(final PhysicalEntity physicalEntity) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/listByQuery/ReactionlikeEvent";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            String requestData = "output=" + physicalEntity.getDbId();
            requestBuilder.sendRequest(requestData, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        List<ReactionLikeEvent> reactionList = new ArrayList<ReactionLikeEvent>();
                        for(int i=0; i<list.size(); ++i){
                            ReactionLikeEvent reaction = (ReactionLikeEvent) ModelFactory.getDatabaseObject(list.get(i).isObject());
                            reactionList.add(reaction);
                        }
                        view.setReactionsWhereOutput(physicalEntity, reactionList);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data about '" +
                                physicalEntity.getDisplayName() + "'\nfor Processes Tab is empty or faulty " +
                                "and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setReactionsWhereOutput(physicalEntity, new ArrayList<ReactionLikeEvent>());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    MessageObject msgObj = new MessageObject("The request for '" + physicalEntity.getDisplayName() +
                            "' in the Processes Tab received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    Console.error(getClass() + " ERROR: " + exception.getMessage());
                    view.setReactionsWhereOutput(physicalEntity, new ArrayList<ReactionLikeEvent>());
                }
            });
        }
        catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The required data for the Processes of\n'" +
                    physicalEntity.getDisplayName() + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            view.setReactionsWhereOutput(physicalEntity, new ArrayList<ReactionLikeEvent>());
        }
    }

    @Override
    public void getOtherFormsForEWAS(final EntityWithAccessionedSequence ewas) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/listByQuery/EntityWithAccessionedSequence";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            String requestData = "referenceEntity=" + ewas.getReferenceEntity().getDbId();
            requestBuilder.sendRequest(requestData, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        List<EntityWithAccessionedSequence> ewasList = new ArrayList<EntityWithAccessionedSequence>();
                        for(int i=0; i<list.size(); ++i){
                            EntityWithAccessionedSequence aux = (EntityWithAccessionedSequence) ModelFactory.getDatabaseObject(list.get(i).isObject());
                            if(!ewas.equals(aux)){
                                ewasList.add(aux);
                            }
                        }
                        view.setOtherFormsForEWAS(ewas, ewasList);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data about '" +
                                ewas.getDisplayName() + "'\nfor Processes Tab is empty or faulty " +
                                "and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setOtherFormsForEWAS(ewas, new ArrayList<EntityWithAccessionedSequence>());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    MessageObject msgObj = new MessageObject("The request for '" + ewas.getDisplayName() +
                            "' in the Processes Tab received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    Console.error(getClass() + " ERROR: " + exception.getMessage());
                    view.setOtherFormsForEWAS(ewas, new ArrayList<EntityWithAccessionedSequence>());
                }
            });
        }
        catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The required data for the Processes of\n'" +
                    ewas.getDisplayName() + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            view.setOtherFormsForEWAS(ewas, new ArrayList<EntityWithAccessionedSequence>());
        }
    }
}
