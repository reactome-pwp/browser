package org.reactome.web.pwp.client.details.tabs.expression;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.client.classes.*;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.LinkedList;
import java.util.List;

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
        ContentClient.getReferenceSequences(databaseObject, new ContentClientHandler.ObjectListLoaded<ReferenceEntity>() {
            @Override
            public void onObjectListLoaded(List<ReferenceEntity> list) {
                List<ReferenceEntity> rtn = new LinkedList<>();
                for (ReferenceEntity referenceEntity : list) {
                    if(referenceEntity instanceof ReferenceSequence){
                        rtn.add(referenceEntity);
                    }
                }
                if (!rtn.isEmpty()) {
                    display.showReferenceSequences(databaseObject, list);
                } else {
                    String errorMsg = "There is not information available for " + databaseObject.getDisplayName();
                    display.showErrorMessage(errorMsg);
                }
            }

            @Override
            public void onContentClientException(Type type, String message) {
                display.showErrorMessage(message);
                eventBus.fireEventFromSource(new ErrorMessageEvent(message), ExpressionTabPresenter.this);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                display.showErrorMessage(error.getReason());
                eventBus.fireEventFromSource(new ErrorMessageEvent(error.getMessage()), ExpressionTabPresenter.this);
            }
        });
    }

}
