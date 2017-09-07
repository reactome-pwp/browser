package org.reactome.web.pwp.client.details.tabs.structures;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.ReferenceEntity;
import org.reactome.web.pwp.model.client.classes.ReferenceSequence;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.LinkedList;
import java.util.List;

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
        ContentClient.getReferenceSequences(databaseObject, new ContentClientHandler.ObjectListLoaded<ReferenceEntity>() {
            @Override
            public void onObjectListLoaded(List<ReferenceEntity> list) {
                List<ReferenceEntity> rtn = new LinkedList<>();
                for (ReferenceEntity referenceEntity : list) {
                    if (referenceEntity instanceof ReferenceSequence) {
                        rtn.add(referenceEntity);
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
            public void onContentClientException(Type type, String message) {
                display.showErrorMessage(message);
                eventBus.fireEventFromSource(new ErrorMessageEvent(message), StructuresTabPresenter.this);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                display.showErrorMessage(error.getReason());
                eventBus.fireEventFromSource(new ErrorMessageEvent(error.getMessage()), StructuresTabPresenter.this);
            }
        });
    }

}
