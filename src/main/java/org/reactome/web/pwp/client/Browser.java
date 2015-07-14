package org.reactome.web.pwp.client;

import com.google.gwt.core.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.manager.state.token.Token;
import org.reactome.web.pwp.client.manager.title.event.TitleChangedEvent;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Browser implements EntryPoint {

    public static final String VERSION = "3.0";
    public static final Boolean BETA = true;

    public static boolean VERBOSE = true;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        initConfig();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                AppController appViewer = new AppController();
                appViewer.go(RootLayoutPanel.get());
                removeLoadingMessage();
            }
        });
    }

    private void initConfig(){
        Browser.VERBOSE = true;
        TitleChangedEvent.REPORT = false;
        Token.DEFAULT_SPECIES_ID = 48887L; //Homo sapiens
        Token.DELIMITER = "&";
    }

    private void removeLoadingMessage(){
        try {
            if(DOM.getElementById("appLoadMessage")!=null) {
                DOM.getElementById("appLoadMessage").removeFromParent();
            }
        }catch (JavaScriptException exception){
            Console.error(exception.getMessage(), this);
        }
    }
}
