package org.reactome.web.pwp.client.tools.analysis;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.PathwayPortalTool;
import org.reactome.web.pwp.client.common.events.BrowserReadyEvent;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.events.ToolSelectedEvent;
import org.reactome.web.pwp.client.common.handlers.BrowserReadyHandler;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.tools.analysis.event.AnalysisErrorEvent;
import org.reactome.web.pwp.model.classes.Species;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisLauncherPresenter extends AbstractPresenter implements AnalysisLauncher.Presenter, BrowserReadyHandler {

    private AnalysisLauncher.Display display;

    private List<Species> speciesList;

    public AnalysisLauncherPresenter(EventBus eventBus, AnalysisLauncher.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);

        this.eventBus.addHandler(BrowserReadyEvent.TYPE, this);
    }

    @Override
    public void displayClosed() {
        this.eventBus.fireEventFromSource(new ToolSelectedEvent(PathwayPortalTool.NONE), this);
    }

    @Override
    public void analysisCompleted(AnalysisCompletedEvent event) {
        this.display.hide();
        this.eventBus.fireEventFromSource(event, this);
    }

    @Override
    public void analysisError(AnalysisErrorEvent event) {
        this.eventBus.fireEventFromSource(event, this);
    }

    @Override
    public void onBrowserReady(BrowserReadyEvent event) {
        retrieveSpeciesList();
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        PathwayPortalTool tool = event.getState().getTool();
        if(tool.equals(PathwayPortalTool.ANALYSIS)){
            display.show();
            display.center();
        }else{
            display.hide();
        }
    }

    private void retrieveSpeciesList() {
        this.speciesList = new LinkedList<>();
        String url = "/ReactomeRESTfulAPI/RESTfulWS/speciesList/";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");

        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                            for (int i = 0; i < list.size(); ++i) {
                                JSONObject object = list.get(i).isObject();
                                Species species = (Species) DatabaseObjectFactory.create(object);
                                //Sometimes the RESTFul services retrieves several times the same speciesList
                                //Note a Set does not help because order is important here
                                if (!speciesList.contains(species)) {
                                    speciesList.add(species);
                                }
                            }
                            display.setSpeciesList(speciesList);
                            break;
                        default:
                            display.setSpeciesList(speciesList);
                            eventBus.fireEventFromSource(new ErrorMessageEvent("There was an error while retrieving species list: " + response.getStatusText()), this);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    display.setSpeciesList(speciesList);
                    eventBus.fireEventFromSource(new ErrorMessageEvent("Species list not available", exception), this);
                }
            });
        } catch (RequestException e) {
            display.setSpeciesList(speciesList);
            eventBus.fireEventFromSource(new ErrorMessageEvent("Species list can not be retrieved", e), this);
        }
    }
}
