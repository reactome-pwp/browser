package org.reactome.web.pwp.client.hierarchy.delgates;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.model.util.Ancestors;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AncestorsLoader {

    public interface AncestorsLoadedHandler {
        void onAncestorsLoaded(Ancestors ancestors);
        void onAncestorsLoadingError(String msg);
    }

    public static void loadAncestors(final Long dbId, final AncestorsLoadedHandler handler) {

        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryEventAncestors/" + dbId;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            Ancestors ancestors;
                            try {
                                JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                                ancestors = new Ancestors(list);
                            } catch (Exception ex){
                                String msg = "The received data to expand the path down to 'dbId=" + dbId +"' is empty or faulty and could not be parsed. ERROR: " + ex.getMessage();
                                handler.onAncestorsLoadingError(msg);
                                return;
                            }
                            handler.onAncestorsLoaded(ancestors);
                            break;
                        default:
                            String msg = "Server error while retrieving ancestors event " + dbId + ": " + response.getStatusText();
                            handler.onAncestorsLoadingError(msg);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String msg = "The ancestors-request for '" + dbId + "' received an error instead of a valid response. ERROR: " + exception.getMessage();
                    handler.onAncestorsLoadingError(msg);
                }
            });
        }catch (RequestException ex) {
            String msg = "The ancestors for '" + dbId + "' could not be received. ERROR: " + ex.getMessage();
            handler.onAncestorsLoadingError(msg);
        }
    }
}
