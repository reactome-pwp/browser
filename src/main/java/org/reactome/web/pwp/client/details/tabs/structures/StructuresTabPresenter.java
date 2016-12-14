package org.reactome.web.pwp.client.details.tabs.structures;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.ReferenceSequence;
import org.reactome.web.pwp.model.client.RESTFulClient;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectsCreatedHandler;
import org.reactome.web.pwp.model.handlers.DatabaseObjectsLoadedHandler;

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
        RESTFulClient.loadReferenceSequences(databaseObject, new DatabaseObjectsLoadedHandler<ReferenceSequence>() {
            @Override
            public void onDatabaseObjectLoaded(List<ReferenceSequence> referenceSequenceList) {
                processReferenceSequences(databaseObject, referenceSequenceList);
            }

            @Override
            public void onDatabaseObjectError(Throwable ex) {
                display.showErrorMessage(ex.getMessage());
                eventBus.fireEventFromSource(new ErrorMessageEvent(ex.getMessage(), ex), StructuresTabPresenter.this);
            }
        });
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
