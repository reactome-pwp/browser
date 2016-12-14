package org.reactome.web.pwp.client.details.tabs.downloads;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.client.RESTFulClient;
import org.reactome.web.pwp.model.client.handlers.DBNameRetrievedHandler;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DownloadsTabPresenter extends AbstractPresenter implements DownloadsTab.Presenter {

    private DownloadsTab.Display display;
    private DatabaseObject currentlyShown;

    public DownloadsTabPresenter(final EventBus eventBus, DownloadsTab.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
        this.requestDBName();
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

        //Show the data
        final DatabaseObject databaseObject = state.getPathway(); //IMPORTANT! We only show information related to the selected Pathway!
        if(databaseObject==null){
            currentlyShown = null;
            display.setInitialState();
        }else if(!databaseObject.equals(currentlyShown)) {
            databaseObject.load(new DatabaseObjectLoadedHandler() {
                @Override
                public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                    currentlyShown = databaseObject;
                    display.showDetails(databaseObject);
                }

                @Override
                public void onDatabaseObjectError(Throwable trThrowable) {
                    currentlyShown = null;
                    eventBus.fireEventFromSource(new ErrorMessageEvent(databaseObject.getDisplayName() + " details could not be retrieved from the server."), this);
                }
            });
        }
    }

    private void requestDBName() {
        RESTFulClient.getDBName(new DBNameRetrievedHandler() {
            @Override
            public void onDBNameRetrieved(String name) {
                display.setDbName(name);
            }

            @Override
            public void onDBNameRetrievedError(Throwable throwable) {
                eventBus.fireEventFromSource(new ErrorMessageEvent(throwable.getMessage()), DownloadsTabPresenter.this);
            }
        });
    }
}
