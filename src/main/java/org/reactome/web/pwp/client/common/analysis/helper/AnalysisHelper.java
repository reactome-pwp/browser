package org.reactome.web.pwp.client.common.analysis.helper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisResult;
import org.reactome.web.pwp.client.common.analysis.model.PathwaySummary;
import org.reactome.web.pwp.client.common.analysis.model.ResourceSummary;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.classes.ReactionLikeEvent;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectsCreatedHandler;

import java.util.*;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class AnalysisHelper {

    public static final String URL_PREFIX = "/AnalysisService";

    private static Map<String, List<ResourceSummary>> resourceSummaryMap = new HashMap<>();

    public interface TokenAvailabilityHandler {
        void onTokenAvailabilityChecked(boolean available, String message);
    }

    public static void checkTokenAvailability(final String token, final TokenAvailabilityHandler handler){
        String url = URL_PREFIX + "/token/" + token + "?pageSize=0&page=1";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            try {
                                AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class, response.getText());
                                resourceSummaryMap.put(token, result.getResourceSummary());
                                handler.onTokenAvailabilityChecked(true, null);
                            } catch (AnalysisModelException e) {
                                //ToDo: Look into new Error Handling
                                if(!GWT.isProdMode() && GWT.isClient()) Console.error(e.getMessage());
                            }
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
                    //ToDo: Look into new Error Handling
                    Console.error(exception.getMessage(), AnalysisHelper.class);
                }
            });
        }catch (RequestException ex) {
            //ToDo: Look into new Error Handling
            Console.error(ex.getMessage(), AnalysisHelper.class);
        }
    }

    public interface PathwaysAnalysisDataRetrievedHandler {
        void onPathwaysAnalysisDataRetrieved(List<PathwaySummary> result);
    }

    public static void getAnalysisDataForPathways(AnalysisStatus analysisStatus, Set<Pathway> pathways, final PathwaysAnalysisDataRetrievedHandler handler){
        if(analysisStatus.isEmpty() || pathways.isEmpty()) return;

        StringBuilder post = new StringBuilder();
        for (Pathway pathway : pathways) {
            post.append(pathway.getDbId()).append(",");
        }
        post.delete(post.length()-1, post.length());

        String url = AnalysisHelper.URL_PREFIX + "/token/" + analysisStatus.getToken() + "/filter/pathways?resource=" + analysisStatus.getResource();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        try {
            requestBuilder.sendRequest(post.toString(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        List<PathwaySummary> result = AnalysisModelFactory.getPathwaySummaryList(response.getText());
                        handler.onPathwaysAnalysisDataRetrieved(result);
                    } catch (Exception ex) {
                        //ToDo: Look into new Error Handling
                        Console.error(ex.getMessage(), AnalysisHelper.class);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //ToDo: Look into new Error Handling
                    Console.error(exception.getMessage(), AnalysisHelper.class);
                }
            });

        }catch (RequestException ex) {
            //ToDo: Look into new Error Handling
            Console.error(ex.getMessage(), AnalysisHelper.class);
        }
    }


    public interface ReactionsAnalysisDataRetrievedHandler {
        void onReactionsAnalysisDataRetrieved(Set<Long> hitReactions);
    }

    public static void getAnalysisDataForPathwaysWithReactions(AnalysisStatus analysisStatus, Set<Pathway> pathways, final ReactionsAnalysisDataRetrievedHandler handler){
        if(analysisStatus.isEmpty() || pathways.isEmpty()) return;

        StringBuilder post = new StringBuilder();
        for (Pathway pathway : pathways) {
            post.append(pathway.getDbId()).append(",");
        }
        post.delete(post.length()-1, post.length());

        String url = AnalysisHelper.URL_PREFIX + "/token/" + analysisStatus.getToken() + "/reactions/pathways?resource=" + analysisStatus.getResource();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        try {
            requestBuilder.sendRequest(post.toString(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONArray res = JSONParser.parseStrict(response.getText()).isArray();
                        Set<Long> hitReactions = new HashSet<>();
                        for (int i = 0; i < res.size(); i++) {
                            hitReactions.add(Long.valueOf(res.get(i).toString()));
                        }
                        handler.onReactionsAnalysisDataRetrieved(hitReactions);
                    }catch (Exception ex){
                        //ToDo: Look into new Error Handling
                        Console.error(ex.getMessage(), AnalysisHelper.class);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //ToDo: Look into new Error Handling
                    Console.error(exception.getMessage(), AnalysisHelper.class);
                }
            });

        }catch (RequestException ex) {
            //ToDo: Look into new Error Handling
            Console.error(ex.getMessage(), AnalysisHelper.class);
        }
    }


    public interface ResourceChosenHandler {
        void onResourceChosen(String resource);
    }

    public static void chooseResource(final String token, final ResourceChosenHandler handler){
        if(resourceSummaryMap.containsKey(token)){
            chooseResource(resourceSummaryMap.get(token), handler);
            return;
        }

        String url = URL_PREFIX + "/token/" + token + "/resources";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            try {
                                List<ResourceSummary> list = new LinkedList<>();
                                JSONArray aux = JSONParser.parseStrict(response.getText()).isArray();
                                for (int i = 0; i < aux.size(); i++) {
                                    JSONObject resource = aux.get(i).isObject();
                                    list.add(AnalysisModelFactory.getModelObject(ResourceSummary.class, resource.toString()));
                                }
                                resourceSummaryMap.put(token, list);
                                chooseResource(list, handler);
                            } catch (AnalysisModelException e) {
                                //ToDo: Look into new Error Handling
                                Console.error(e.getMessage(), AnalysisHelper.class);
                            }
                            break;
                        case Response.SC_GONE:
                            //ToDo: Look into new Error Handling
                            Console.error("Your result may have been deleted due to a new content release. " +
                                          "Please submit your data again to obtain results for the latest version of our database",
                                          AnalysisHelper.class);
                            break;
                        default:
                            //ToDo: Look into new Error Handling
                            Console.error(response.getStatusText(), AnalysisHelper.class);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //ToDo: Look into new Error Handling
                    Console.error(exception.getMessage(), AnalysisHelper.class);
                }
            });
        }catch (RequestException ex) {
            //ToDo: Look into new Error Handling
            Console.error(ex.getMessage(), AnalysisHelper.class);
        }
    }

    private static void chooseResource(List<ResourceSummary> resources, ResourceChosenHandler handler){
        if(handler!=null){
            ResourceSummary resource = resources.size()==2 ? resources.get(1) : resources.get(0);
            handler.onResourceChosen(resource.getResource());
        }
    }
}
