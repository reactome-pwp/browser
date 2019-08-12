package org.reactome.web.pwp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.manager.state.token.Token;
import org.reactome.web.pwp.client.manager.title.event.TitleChangedEvent;
import org.reactome.web.pwp.model.client.classes.DBInfo;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Browser implements EntryPoint, ContentClientHandler.DatabaseInfo {

    public static final String VERSION = "3.6";
    public static final Boolean BETA = false;

    public static boolean VERBOSE = true;
    public static String GSA_SERVER = "";
    private AppController appViewer;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        initConfig();
        ContentClient.getDatabaseInformation(this);
    }

    @Override
    public void onDatabaseInfoLoaded(DBInfo dbInfo) {
        Scheduler.get().scheduleDeferred(() -> {
            appViewer = new AppController(dbInfo);
            appViewer.go(RootLayoutPanel.get());
            removeLoadingMessage();
        });
    }

    @Override
    public void onContentClientException(Type type, String message) {
        showErrorMessage();
    }

    @Override
    public void onContentClientError(ContentClientError error) {
        showErrorMessage();
    }

    private void initConfig() {
        String hostName = Window.Location.getHostName();
        Browser.VERBOSE = (hostName.equals("localhost") || hostName.equals("127.0.0.1"));
        Browser.GSA_SERVER = hostName.equals("localhost") || hostName.equals("127.0.0.1") ? "/gsa" : "//gsa.reactome.org";
        TitleChangedEvent.REPORT = false;
        Token.DEFAULT_SPECIES_ID = 48887L; //Homo sapiens
        Token.DELIMITER = "&";
        FireworksFactory.SHOW_FOAM_BTN = true;
    }

    private void removeLoadingMessage() {
        try {
            if (DOM.getElementById("appLoadMessage") != null) {
                DOM.getElementById("appLoadMessage").removeFromParent();
            }
        } catch (JavaScriptException exception) {
            Console.error(exception.getMessage(), this);
        }
    }

    private void showErrorMessage() {
        removeLoadingMessage();
        try {
            if (DOM.getElementById("appErrorMessage") != null) {
                DOM.getElementById("appErrorMessage").getStyle().setDisplay(Style.Display.BLOCK);
            }
        } catch (JavaScriptException exception) {
            Console.error(exception.getMessage(), this);
        }
    }
}
