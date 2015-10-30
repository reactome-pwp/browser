package org.reactome.web.pwp.client.common.analysis.helper;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.pwp.client.common.analysis.model.PathwaySummary;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.model.classes.Pathway;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class AnalysisHelper {

    public static final String URL_PREFIX = "/AnalysisService";

    private static final Set<String> validTokens = new HashSet<>();

    public interface TokenAvailabilityHandler {
        void onTokenAvailabilityChecked(boolean available, String message);
    }

    public static void addValidToken(String token){
        if(token!=null && !token.isEmpty()) validTokens.add(token);
    }

    public static void checkTokenAvailability(final String token, final TokenAvailabilityHandler handler) {
        if (token == null || validTokens.contains(token)) { //YES, a null token is valid (it means there is not analysis overlay))
            handler.onTokenAvailabilityChecked(true, null);
        } else {
            String url = URL_PREFIX + "/token/" + token + "?pageSize=0&page=1";
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
            try {
                requestBuilder.sendRequest(null, new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        switch (response.getStatusCode()) {
                            case Response.SC_OK:
                                validTokens.add(token);
                                handler.onTokenAvailabilityChecked(true, null);
                                break;
                            case Response.SC_GONE:
                                handler.onTokenAvailabilityChecked(false, "Your result may have been deleted due to a new content release.\n" +
                                                                          "Please submit your data again to obtain results from the latest version of our database");
                                break;
                            default:
                                handler.onTokenAvailabilityChecked(false, "There is no result associated with the provided token (in the url) from a previous analysis");
                        }
                    }

                    @Override
                    public void onError(Request request, Throwable exception) {
                        handler.onTokenAvailabilityChecked(false, "An error happened while checking the analysis results availability");
                    }
                });
            } catch (RequestException ex) {
                handler.onTokenAvailabilityChecked(false, "Could not connect to the server to check the analysis results availability");
            }
        }
    }

    public interface PathwaysAnalysisDataRetrievedHandler {
        void onPathwaysAnalysisDataRetrieved(List<PathwaySummary> result);
    }

    public static void getAnalysisDataForPathways(AnalysisStatus analysisStatus, Set<Pathway> pathways, final PathwaysAnalysisDataRetrievedHandler handler) {
        if (analysisStatus.isEmpty() || pathways.isEmpty()) return;

        StringBuilder post = new StringBuilder();
        for (Pathway pathway : pathways) {
            post.append(pathway.getDbId()).append(",");
        }
        post.delete(post.length() - 1, post.length());

        String url = AnalysisHelper.URL_PREFIX + "/token/" + analysisStatus.getToken() + "/filter/pathways?resource=" + analysisStatus.getResource();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        try {
            requestBuilder.sendRequest(post.toString(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            try {
                                List<PathwaySummary> result = AnalysisModelFactory.getPathwaySummaryList(response.getText());
                                handler.onPathwaysAnalysisDataRetrieved(result);
                            } catch (Exception ex) {
                                Console.error(ex.getMessage(), AnalysisHelper.class);
                            }
                            break;
                        default:
                            Console.error(response.getStatusText(), AnalysisHelper.class);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Console.error(exception.getMessage(), AnalysisHelper.class);
                }
            });

        } catch (RequestException ex) {
            Console.error(ex.getMessage(), AnalysisHelper.class);
        }
    }


    public interface ReactionsAnalysisDataRetrievedHandler {
        void onReactionsAnalysisDataRetrieved(Set<Long> hitReactions);
    }

    public static void getAnalysisDataForPathwaysWithReactions(AnalysisStatus analysisStatus, Set<Pathway> pathways, final ReactionsAnalysisDataRetrievedHandler handler) {
        if (analysisStatus.isEmpty() || pathways.isEmpty()) return;

        StringBuilder post = new StringBuilder();
        for (Pathway pathway : pathways) {
            post.append(pathway.getDbId()).append(",");
        }
        post.delete(post.length() - 1, post.length());

        String url = AnalysisHelper.URL_PREFIX + "/token/" + analysisStatus.getToken() + "/reactions/pathways?resource=" + analysisStatus.getResource();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        try {
            requestBuilder.sendRequest(post.toString(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            try {
                                JSONArray res = JSONParser.parseStrict(response.getText()).isArray();
                                Set<Long> hitReactions = new HashSet<>();
                                for (int i = 0; i < res.size(); i++) {
                                    hitReactions.add(Long.valueOf(res.get(i).toString()));
                                }
                                handler.onReactionsAnalysisDataRetrieved(hitReactions);
                            } catch (Exception ex) {
                                Console.error(ex.getMessage(), AnalysisHelper.class);
                            }
                            break;
                        default:
                            Console.error(response.getStatusText(), AnalysisHelper.class);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Console.error(exception.getMessage(), AnalysisHelper.class);
                }
            });

        } catch (RequestException ex) {
            Console.error(ex.getMessage(), AnalysisHelper.class);
        }
    }
}
