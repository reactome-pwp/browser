package org.reactome.web.pwp.client.tools.analysis.gsa.client;

import com.google.gwt.http.client.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAException;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAFactory;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.*;

import java.util.List;


/**
 * This is a client that handles communication with the GSA Service.
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAClient {

    private static final String URL_METHODS = "/gsa/0.1/methods";
    private static final String URL_TYPES = "/gsa/0.1/types";
    private static final String URL_STATUS = "/gsa/0.1/status";
    private static final String URL_ANALYSIS = "/gsa/0.1/analysis";
    private static final String URL_RESULT = "/gsa/0.1/result";

    /**
     * Retrieves the available methods and their specification/parameters.
     *
     */
    public static Request getMethods(final GSAClientHandler.GSAMethodsHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_METHODS);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            handler.onMethodsSuccess(getMethods(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error!=null) {
                                handler.onError(error);
                            }
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onException(exception.getMessage());
                }
            });
        } catch (RequestException ex) {
            handler.onException(ex.getMessage());
        }
        return request;
    }

    /**
     * The Reactome GSA platform can analyse different types of 'omics data.
     * This function returns a list of all currently supported dataset types.
     */
    public static Request getDatasetTypes(final GSAClientHandler.GSADatasetTypesHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_TYPES);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            handler.onTypesSuccess(getTypes(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error!=null) {
                                handler.onError(error);
                            }
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onException(exception.getMessage());
                }
            });
        } catch (RequestException ex) {
            handler.onException(ex.getMessage());
        }
        return request;
    }

    /**
     * Initiates an analysis by submitting the annotated
     * datasets and parameters in JSON format
     */
    public static Request analyse(final String data, final GSAClientHandler.GSAAnalysisHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL_ANALYSIS);
        requestBuilder.setHeader("Content-Type", "application/json");
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(data, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            handler.onAnalysisSuccess(response.getText());
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error!=null) {
                                handler.onError(error);
                            }
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onException(exception.getMessage());
                }
            });
        } catch (RequestException ex) {
            handler.onException(ex.getMessage());
        }
        return request;
    }

    /***
     * returning the current status of the task.
     */
    public static Request getAnalysisStatus(final String analysisId, final GSAClientHandler.GSAStatusHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_STATUS + "/" + analysisId);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    Console.info("OOOO > " + response.getText() + " " + response.getStatusCode());
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            handler.onStatusSuccess(getStatus(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error!=null) {
                                handler.onError(error);
                            }
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onException(exception.getMessage());
                }
            });
        } catch (RequestException ex) {
            handler.onException(ex.getMessage());
        }
        return request;
    }

    public static Request getAnalysisResultLinks(final String analysisId, final GSAClientHandler.GSAResultLinksHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_RESULT + "/" + analysisId);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            handler.onResultLinksSuccess(getResultLinks(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error!=null) {
                                handler.onError(error);
                            }
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onException(exception.getMessage());
                }
            });
        } catch (RequestException ex) {
            handler.onException(ex.getMessage());
        }
        return request;
    }

    private static List<Method> getMethods(final String json, final GSAClientHandler.GSAMethodsHandler handler) {
        List<Method> rtn = null;
        try {
            MethodsResult result = GSAFactory.getModelObject(MethodsResult.class, "{\"methods\": " + json + "}");
            rtn = result != null ? result.getMethods() : null;
        } catch (GSAException ex) {
            handler.onException("Server unreachable");
            Console.error(ex.getMessage());
        }
        return rtn;
    }

    private static List<DatasetType> getTypes(final String json, final GSAClientHandler.GSADatasetTypesHandler handler) {
        List<DatasetType> rtn = null;
        try {
            DatasetTypesResult result = GSAFactory.getModelObject(DatasetTypesResult.class, "{\"types\": " + json + "}");
            rtn = result != null ? result.getTypes() : null;
        } catch (GSAException ex) {
            handler.onException("Server unreachable");
            Console.error(ex.getMessage());
        }
        return rtn;
    }

    private static Status getStatus(final String json, final GSAClientHandler.GSAStatusHandler handler) {
        Status rtn = null;
        try {
            rtn = GSAFactory.getModelObject(Status.class, json);
        } catch (GSAException ex) {
            handler.onException("Server unreachable");
            Console.error(ex.getMessage());
        }
        return rtn;
    }

    private static ResultLinks getResultLinks(final String json, final GSAClientHandler.GSAResultLinksHandler handler) {
        ResultLinks rtn = null;
        try {
            rtn = GSAFactory.getModelObject(ResultLinks.class, json);
        } catch (GSAException ex) {
            handler.onException("Server unreachable");
            Console.error(ex.getMessage());
        }
        return rtn;
    }

    private static GSAError getError(final String json, final GSAClientHandler handler) {
        GSAError rtn = null;
        try {
            rtn = GSAFactory.getModelObject(GSAError.class, json);
        } catch (GSAException ex) {
            handler.onException("Server unreachable");
            Console.error(ex.getMessage());
        }
        return rtn;
    }

}
