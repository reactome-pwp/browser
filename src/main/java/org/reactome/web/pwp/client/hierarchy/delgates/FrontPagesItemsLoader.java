package org.reactome.web.pwp.client.hierarchy.delgates;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.classes.Species;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FrontPagesItemsLoader {

    public interface FrontPagesItemsLoadedHandler {
        void onFrontPagesItemsLoaded(List<Event> frontPageItems);
        void onFrontPagesItemsLoadingError(String msg);
    }

    public static void loadFrontPageItems(final Species species, final FrontPagesItemsLoadedHandler handler){
        String speciesName = species.getDisplayName().replaceAll(" ", "+");
        String url = "/ReactomeRESTfulAPI/RESTfulWS/frontPageItems/" + speciesName;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            List<Event> frontPageItems = new ArrayList<>();
                            try {
                                JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                                for (int i = 0; i < list.size(); ++i) {
                                    Event event = (Event) DatabaseObjectFactory.create(list.get(i).isObject());
                                    frontPageItems.add(event);
                                }
                            } catch (Exception ex) {
                                String msg = "The received data for the front page items of '" + species.getDisplayName() + "' is empty or faulty and could not be parsed. ERROR: " + ex.getMessage();
                                handler.onFrontPagesItemsLoadingError(msg);
                                return;
                            }
                            handler.onFrontPagesItemsLoaded(frontPageItems);
                            break;
                        default:
                            String msg = "Server error while retrieving hierarchy top level pathways: " + response.getStatusText();
                            handler.onFrontPagesItemsLoadingError(msg);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String msg = "The front-page-items-request for '" + species.getDisplayName() + "' received an error instead of a valid response. ERROR: " + exception.getMessage();
                    handler.onFrontPagesItemsLoadingError(msg);
                }
            });
        } catch (RequestException ex) {
            String msg = "The front page items for '" + species.getDisplayName() + "' could not be received. ERROR: " + ex.getMessage();
            handler.onFrontPagesItemsLoadingError(msg);
        }
    }
}
