package org.reactome.web.pwp.client.details.tabs.molecules;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.DatabaseObjectSelectedEvent;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.common.utils.LRUCache;
import org.reactome.web.pwp.client.common.utils.MapSet;
import org.reactome.web.pwp.client.details.delegates.MoleculeSelectedHandler;
import org.reactome.web.pwp.client.details.delegates.MoleculeSelectedListener;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Molecule;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.PhysicalToReferenceEntityMap;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.pwp.client.details.tabs.molecules.model.type.PathwayType;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.web.pwp.model.client.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.client.factory.SchemaClass;

import java.util.*;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesTabPresenter extends AbstractPresenter implements MoleculesTab.Presenter, MoleculeSelectedHandler {

    private MoleculesTab.Display display;

    private DatabaseObject currentDatabaseObject;
    private Pathway currentPathway;

    private int count = 0;
    private List<PhysicalToReferenceEntityMap> toHighlight = new ArrayList<>();

    private final LRUCache<Pathway, Result> cachePathway = new LRUCache<>(15);
    private final LRUCache<Long, HashSet<Molecule>> cacheDbObj= new LRUCache<>(10);
    private Set<Pathway> previouslyLoaded = new HashSet<>();

    public MoleculesTabPresenter(EventBus eventBus, MoleculesTab.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);

        //Needed because the view implementation will be listening to all the MoleculesPanel for molecules to be selected
        MoleculeSelectedListener.getMoleculeSelectedListener().setMoleculeSelectedHandler(this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();
        this.currentDatabaseObject = state.getTarget();
        this.currentPathway = state.getPathway();

        if(this.currentPathway==null){
            this.display.setInitialState();
        }else {
            if(state.getDetailsTab().equals(display.getDetailTabType()) || previouslyLoaded.contains(this.currentPathway)) {
                this.previouslyLoaded.add(this.currentPathway);
                this.display.showDetails(this.currentPathway, state.getSelected());
            }else{
                this.display.setInitialState();
            }
        }
    }

//    /**
//     * Used if DownloadTab requires MoleculesDownload
//     * @param pathway current selected pathway
//     */
//    @Override
//    public void onMoleculesDownloadRequired(Pathway pathway){
//        if(!cachePathway.containsKey(pathway)){
//            this.currentPathway = pathway;
//            this.currentDatabaseObject = pathway;
//            display.setCurrentPanel(pathway, true);
//        }else{
//            display.moleculesDownloadRequired();
//        }
//    }


    /**
     * Getting the data of all participating molecules in a Pathway-with-Diagram,
     * creating the intersection of participants in Pathway and selected Event or PhysicalEntity
     */
    @Override
    public void getMoleculesData() {
        //TODO
        String urlPathway  =  "/ContentService/data/participants/" + currentPathway.getDbId();
        String urlReaction = "/ContentService/data/participants/" + currentDatabaseObject.getDbId() + "/referenceEntities";
        if(cachePathway.containsKey(currentPathway)){
            Result result = cachePathway.get(currentPathway);
            if(currentPathway.getDbId().equals(currentDatabaseObject.getDbId())){
                result.highlight();
                display.setMoleculesData(result);
            }else{
                result.undoHighlighting(); //Previous highlighting needs to be undone before new one can be applied.

                if(cacheDbObj.containsKey(currentDatabaseObject.getDbId())){
                    useExistingReactionParticipants(result, false);
                }else{
                    getReactionParticipants(result, urlReaction, false, false);
                }

            }
        }else{
            //Not yet cached data needs to be requested.
            getPathwayParticipants(urlPathway, urlReaction);
        }
    }

    /**
     * Update list of molecules as far as highlighting is concerned
     * in case that Pathway-with-Diagram has already been loaded.
     */
    @Override
    public void updateMoleculesData() {
        String urlReaction = "/ContentService/data/participants/" + currentDatabaseObject.getDbId() + "/referenceEntities";

        Result result = cachePathway.get(currentPathway);
        result.undoHighlighting();

        if(cacheDbObj.containsKey(currentDatabaseObject.getDbId())){
            useExistingReactionParticipants(result, true);
        }else{
            getReactionParticipants(result, urlReaction, true, false);
        }
    }

    /**
     * This method receives molecules data for a pathway and calls the method for reaction if necessary.
     * @param urlPathway Request-URL for ContentService
     * @param urlReaction Request-URL for ContentService
     */
    private void getPathwayParticipants(final String urlPathway, final String urlReaction) {
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, urlPathway);
        requestBuilder.setHeader("Accept", "application/json");

        final MapSet<PhysicalToReferenceEntityMap, Molecule> mapSet = new MapSet<>();
        final ArrayList<Molecule> resultList = new ArrayList<>();

        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();
                        for(int i=0; i< jsonArray.size(); ++i){
                            //Splitting necessary to get the set of referenceEntities that map to specific peID.
                            String[] split = jsonArray.get(i).toString().split("\"refEntities\":");

                            //Getting the key.
                            String subKey = split[0];
                            subKey = subKey.substring(0, subKey.length()-2) + "}";
                            JSONObject keyObj = JSONParser.parseStrict(subKey).isObject();
                            PhysicalToReferenceEntityMap physicalEntity = new PhysicalToReferenceEntityMap(keyObj);

                            //Getting the set of referenceEntities.
                            String subSet = split[split.length - 1].subSequence(0, split[split.length - 1].length()-1).toString();
                            JSONArray jsonArray2 = JSONParser.parseStrict(subSet).isArray();
                            ArrayList<Molecule> refEntitySet = new ArrayList<>();
                            for(int j = 0; j < jsonArray2.size(); ++j){
                                JSONObject object = jsonArray2.get(j).isObject();
                                Molecule molecule = new Molecule(DatabaseObjectFactory.create(object).getSchemaClass());
                                molecule.load(object);
                                refEntitySet.add(molecule);
                            }
                            resultList.addAll(refEntitySet);
                            mapSet.add(physicalEntity, refEntitySet);
                        }

                        //Counting the occurrences of each molecule.
                        HashMap<Molecule, Integer> count = new HashMap<>();
                        for(Molecule m : resultList){
                            int value = 1;
                            if(count.containsKey(m)){
                                value = count.get(m) + 1;
                            }
                            count.put(m, value);
                        }

                        resultList.clear();

                        //Storing the occurrence-information in attribute of molecule.
                        for (Map.Entry<Molecule, Integer> entry : count.entrySet()) {
                            Molecule key = entry.getKey();
                            Integer value = entry.getValue();
                            key.setOccurrenceInPathway(value);
                            resultList.add(key);
                        }

                        Result result = new Result(resultList); //resultList is now duplicate-free.
                        result.setPhyEntityToRefEntitySet(mapSet);
                        cachePathway.put(currentPathway, result);
                        if(currentPathway.getDbId().equals(currentDatabaseObject.getDbId())){
                            result.highlight();
                            display.setMoleculesData(result);
                        }else{
                            if(cacheDbObj.containsKey(currentDatabaseObject.getDbId())){
                                useExistingReactionParticipants(result, false);
                            }else{
                                getReactionParticipants(result, urlReaction, false, false);
                            }
                        }
                    }catch (Exception ex){
                        String errorMsg = "The received object containing the Molecules for " + currentPathway.getDisplayName() + " is empty or faulty and could not be parsed. ERROR: " + ex.getMessage();
                        eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, ex), MoleculesTabPresenter.this);
                        display.setInitialState();
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String errorMsg = "The request for the object containing the Molecules for " + currentPathway.getDisplayName() + "' received an error instead of a response";
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), MoleculesTabPresenter.this);
                    display.setInitialState();
                }
            });
        } catch (RequestException ex) {
            String errorMsg = "Server connexion failed for object " + currentPathway.getDisplayName() + ". ERROR: " + ex.getMessage();
            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, ex), MoleculesTabPresenter.this);
            display.setInitialState();
        }
    }

    /**
     * This method receives molecules data for a reaction and either sets or updates it in the display.
     * @param result previous result, needed for intersection
     * @param urlReaction Request-URL for ContentService
     * @param update boolean if update necessary
     */
    private void getReactionParticipants(final Result result, String urlReaction, final boolean update, final boolean refreshTitle) {
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, urlReaction);
        requestBuilder.setHeader("Accept", "application/json");

        try {
            requestBuilder.sendRequest(null, new RequestCallback() {

                public void onResponseReceived(Request request, Response response) {
                    try {
                        // Parsing the result text to a JSONArray because a reaction can contain many elements.
                        JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();

                        Molecule molecule;
                        HashSet<Molecule> molecules = new HashSet<>();
                        for(int i=0; i<jsonArray.size(); ++i){
                            JSONObject object = jsonArray.get(i).isObject();
                            molecule = new Molecule(DatabaseObjectFactory.create(object).getSchemaClass());
                            molecule.load(object);
                            result.highlight(molecule);
                            molecules.add(molecule);
                        }
                        cacheDbObj.put(currentDatabaseObject.getDbId(), molecules);

                        if(update){
                            display.updateMoleculesData(result);
                        }else if(refreshTitle){
                            display.refreshTitle(result.getNumberOfHighlightedMolecules(), result.getNumberOfMolecules());
                        }else {
                            display.setMoleculesData(result);
                        }

                    }catch (Exception ex){
                        String errorMsg = "The received object is empty or faulty and the Molecules for '" + currentDatabaseObject.getDisplayName() + "' could not be parsed. ERROR: " + ex.getMessage();
                        eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, ex), MoleculesTabPresenter.this);
                        result.highlight();
                        display.setMoleculesData(result);
                    }

                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String errorMsg = "The request received an error instead of a valid response. ERROR: " + exception.getMessage();
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), MoleculesTabPresenter.this);
                    result.highlight();
                    display.setMoleculesData(result);
                }
            });
        } catch (RequestException ex) {
            String errorMsg = "Server connexion failed for object " + currentDatabaseObject.getDisplayName() + ". ERROR: " + ex.getMessage();
            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, ex), MoleculesTabPresenter.this);
            result.highlight();
            display.setMoleculesData(result);
        }
    }

    /**
     * If a molecule is selected, meaning its icon was clicked, then the corresponding entity in the diagram needs to
     * be selected. Further clicks allow circling through all entities that are or contain this specific molecule.
     * @param physicalEntityList list of physical entities in diagram that are or contain this specific molecule.
     */
    @Override
    public void moleculeSelected(List<PhysicalToReferenceEntityMap> physicalEntityList) {
        display.showLoadingMessage();

        if(physicalEntityList != null){
            for(int i = 0; i < physicalEntityList.size() && i < toHighlight.size(); ++i){
                if(!physicalEntityList.get(i).getPeDbId().equals(toHighlight.get(i).getPeDbId())){
                    count = 0;
                    break;
                }
            }
            toHighlight = physicalEntityList;
        }

        if(count >= toHighlight.size()){
            count = 0; // Circling through all entities in toHighlight.
        }

        PathwayType type = determineType();
        if(type.equals(PathwayType.NGB)){
            selectEntity();
        }else{ //CGB
            String msg = "Functionality not yet available for this Diagram";
            display.setLoadingMsg(msg);

            com.google.gwt.user.client.Timer timer = new com.google.gwt.user.client.Timer(){
                @Override
                public void run() {
                    GWT.runAsync(new RunAsyncCallback() {
                        public void onFailure(Throwable exception) {
                            String errorMsg = "Error while trying to run AsyncCallback and clear loading Msg. ERROR: " + exception.getMessage();
                            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), MoleculesTabPresenter.this);
                        }

                        public void onSuccess() {
                            display.clearLoadingMsg();
                        }
                    });
                }
            };
            timer.schedule(5000);
        }
    }

    /**
     * Method to determine the type of the currently displayed diagram
     * @return PathwayType (OGB, NGB, CGB)
     */
    private PathwayType determineType() {
        List<Event> events = this.currentPathway.getHasEvent();
        PathwayType pt = null;
        for(Event e : events){
            if (e.getSchemaClass() == SchemaClass.PATHWAY){
                Pathway p = (Pathway) e;

                if(pt == null){
                    if(p.getHasDiagram()){
                        pt = PathwayType.OGB; //only green boxes
                    }else{
                        pt = PathwayType.NGB; //no green boxes
                    }
                }else{
                    if(p.getHasDiagram() && pt == PathwayType.NGB || !p.getHasDiagram() && pt == PathwayType.OGB){
                        return PathwayType.CGB; //diagram that contains green boxes
                    }
                }
            }
        }
        return pt;
    }

    /**
     * Select a specific entity (identified by its peDbId) in the Diagram.
     */
    private void selectEntity() {
        if(toHighlight.size() <= count){
            count = 0; // for circling through
        }
        final Long peDbId = toHighlight.get(count).getPeDbId();
        ++count;

        ContentClient.query(peDbId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                Selection selection = new Selection(databaseObject);
                eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), MoleculesTabPresenter.this);
                display.clearLoadingMsg();
            }

            @Override
            public void onContentClientException(Type type, String message) {
                String errorMsg = "Error retrieving object for " + peDbId;
                eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), MoleculesTabPresenter.this);
                display.clearLoadingMsg();
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                String errorMsg = "Error retrieving object for " + peDbId;
                eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), MoleculesTabPresenter.this);
                display.clearLoadingMsg();
            }
        });
    }

    /**
     * Enables tracking the frequency users use available download.
     */
    @Override
    public void moleculeDownloadStarted() {
        Console.info("moleculeDownloadStarted needs NOTIFICATION!");
    }

    /**
     * If ReactionParticipants have already been loaded and are still stored in the cach then those should be used
     * to avoid requesting them again from the ContentService.
     * @param result current result
     * @param update decides whether view should be updated (true) or newly set (false)
     */
    private void useExistingReactionParticipants(Result result, boolean update){
        HashSet<Molecule> molecules = new HashSet<>(cacheDbObj.get(currentDatabaseObject.getDbId()));
        for(Molecule molecule : molecules){
            result.highlight(molecule);
        }
        if(update){
            display.updateMoleculesData(result);
        }else{
            display.setMoleculesData(result);
        }
    }

}
