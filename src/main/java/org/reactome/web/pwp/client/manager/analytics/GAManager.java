package org.reactome.web.pwp.client.manager.analytics;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Command;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.DatabaseObjectSelectedEvent;
import org.reactome.web.pwp.client.common.events.DetailsTabChangedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.handlers.DatabaseObjectSelectedHandler;
import org.reactome.web.pwp.client.common.handlers.DetailsTabChangedHandler;
import org.reactome.web.pwp.client.common.handlers.StateChangedHandler;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.client.manager.title.event.TitleChangedEvent;
import org.reactome.web.pwp.client.manager.title.handler.TitleChangedHandler;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.utils.analytics.client.GATracker;

import java.util.Objects;

/**
 * GAManager keeps track of those events that are of interest in order to get statistics of the web application.
 *
 * A part from the "trackPageview" option that GA offers, we track some internal events that have been defined to
 * know how the users use the application. NOTE: trackPageview does not differentiate pages by token (string after #)
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GAManager implements BrowserModule.Manager,
        DatabaseObjectSelectedHandler, DetailsTabChangedHandler,
        StateChangedHandler, TitleChangedHandler {

    private static final String PREFIX = "\t\t[GAManager] ";

    private State currentState;

    //Set to true in order to see in the console what the GAManager is doing
    private static final boolean TRACK_GA_MANAGER = true;
    private boolean gaTrackerActive;

    public GAManager(EventBus eventBus) {
        eventBus.addHandler(DatabaseObjectSelectedEvent.TYPE, this);
        eventBus.addHandler(DetailsTabChangedEvent.TYPE, this);
        eventBus.addHandler(StateChangedEvent.TYPE, this);
        eventBus.addHandler(TitleChangedEvent.TYPE, this);

        LocationHelper.Location location = LocationHelper.getLocation();
        try{
            switch (location){
                case PRODUCTION:
                    GATracker.setAccount("UA-42985898-1", "reactome.org");
                    this.gaTrackerActive = true;
                    break;
                case DEV:
                    GATracker.setAccount("UA-42985898-2", "oicr.on.ca");
                    this.gaTrackerActive = true;
                    break;
                case CURATOR:
                    GATracker.setAccount("UA-42985898-3", "oicr.on.ca");
                    this.gaTrackerActive = true;
                    break;
                default:
                    this.gaTrackerActive = false;
            }
        }catch (JavaScriptException ex){
            this.gaTrackerActive = false;
        }
        //noinspection PointlessBooleanExpression,ConstantConditions
        if(!this.gaTrackerActive && TRACK_GA_MANAGER){
            Console.info("[GAManager] set for DEV purposes");
        }
    }

    @Override
    public void onDatabaseObjectSelected(DatabaseObjectSelectedEvent event) {
        String module = event.getSource().getClass().getSimpleName();
        String action = "SELECTED";

        Selection selection = event.getSelection();
        if(selection!=null){
            if(!Objects.equals(selection.getDiagram(), currentState.getPathway())){
                trackEvent(selection.getDiagram(), action, module);
            }
            if(!Objects.equals(selection.getDatabaseObject(), currentState.getSelected())){
                trackEvent(selection.getDatabaseObject(), action, module);
            }
        }
    }

    @Override
    public void onDetailsTabChanged(DetailsTabChangedEvent event) {
        String module = event.getSource().getClass().getSimpleName();
        trackEvent(event.getDetailsTab().toString(), "SELECTED", module);
    }

    @Override
    public void onStateChanged(final StateChangedEvent event) {
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                currentState = new State(event.getState());
            }
        });
    }

    @Override
    public void onTitleChanged(TitleChangedEvent event) {
        if(gaTrackerActive){
            GATracker.trackPageview();
        }
        if(TRACK_GA_MANAGER){
            Console.info(PREFIX + "Event tracked: [ Page name changed to : \"" + event.getTitle() + "\"]");
        }
    }

    private void trackEvent(DatabaseObject element, String action, String module){
        if(element==null) return;
        String category = element.getSchemaClass().toString();
        trackEvent(category, action, module);
    }

    private void trackEvent(String category, String action, String module){
        if(category==null || action == null || module == null) return;

        category = category.toUpperCase();
        module = module.replace("Presenter", "").replace("TAB", "_TAB").toUpperCase();
        action = action.toUpperCase();

        if(gaTrackerActive){
            GATracker.trackEvent(category, action, module);
        }
        if(TRACK_GA_MANAGER){
            Console.info(PREFIX + "Event tracked: [" + category + ", " + action + ", " + module + "]");
        }
    }
}
