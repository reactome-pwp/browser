package org.reactome.web.pwp.client.manager.analytics;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.handlers.StateChangedHandler;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.manager.title.event.TitleChangedEvent;
import org.reactome.web.pwp.client.manager.title.handler.TitleChangedHandler;
import uk.ac.ebi.pwp.utils.analytics.client.GATracker;

/**
 * GAManager keeps track of those events that are of interest in order to get statistics of the web application.
 *
 * A part from the "trackPageview" option that GA offers, we track some internal events that have been defined to
 * know how the users use the application. NOTE: trackPageview does not differentiate pages by token (string after #)
 *
 * CATEGORY: The name supplied for the group of objects we want to track.
 * ACTION: A string that is uniquely paired with each category (commonly used to define the type of user interaction).
 * LABEL: An optional string to provide additional dimensions to the event data.
 *
 * There are actions like peptide selection that can be done in more than one place (PCM, PeptideTable or StateManager),
 * using Label we can differentiate the actions in order to know later on what the user use the most when working with
 * the webapp
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GAManager implements BrowserModule.Manager, StateChangedHandler, TitleChangedHandler {
    private static final String PREFIX = "\t\t[GAManager] ";

    private EventBus eventBus;

    //Set to true in order to see in the console what the GAManager is doing
    private static final boolean TRACK_GA_MANAGER = true;
    private boolean gaTrackerActive;

    public GAManager(EventBus eventBus) {
        this.eventBus = eventBus;

        this.eventBus.addHandler(StateChangedEvent.TYPE, this);
        this.eventBus.addHandler(TitleChangedEvent.TYPE, this);

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
    public void onStateChanged(StateChangedEvent event) {

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
}
