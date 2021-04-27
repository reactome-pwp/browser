package org.reactome.web.pwp.client.tools.analysis.gsa.client;

import com.google.gwt.http.client.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.ExternalDataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAException;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAFactory;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.*;

import java.util.List;


/**
 * This is a client that handles communication with the GSA Service.
 * <p>
 * NOTE: Please keep in mind that to avoid Cross-Origin restrictions applied by most browsers
 * we proxy the gsa backend (gsa.reactome.org) through our servers at Apache level.
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@SuppressWarnings("Duplicates")
public class GSAClient {
    public static String GSA_SERVER =                           "/GSAServer";
    public static final String URL_METHODS =                   GSA_SERVER + "/0.1/methods";
    public static final String URL_TYPES =                     GSA_SERVER + "/0.1/types";

    public static final String URL_GET_EXAMPLES =              GSA_SERVER + "/0.1/data/examples";
    public static final String URL_GET_EXTERNAL_DATASOURCE =   GSA_SERVER + "/0.1/data/sources";
    public static final String URL_LOAD_DATASET =              GSA_SERVER + "/0.1/data/load";
    public static final String URL_LOADING_STATUS =            GSA_SERVER + "/0.1/data/status";
    public static final String URL_SUMMARY =                   GSA_SERVER + "/0.1/data/summary";

    public static final String URL_STATUS =                    GSA_SERVER + "/0.1/status";
    public static final String URL_REPORTS_STATUS =            GSA_SERVER + "/0.1/report_status";
    public static final String URL_ANALYSIS =                  GSA_SERVER + "/0.1/analysis";
    public static final String URL_RESULT =                    GSA_SERVER + "/0.1/result";

    /**
     * Retrieves the available methods and their specification/parameters.
     */
    public static Request getMethods(final GSAClientHandler.GSAMethodsHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_METHODS);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onMethodsSuccess(getMethods(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error != null) {
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
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onTypesSuccess(getTypes(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error != null) {
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
     * This function returns a list of all the available example datasets
     * stored on the gsa server.
     */
    public static Request getExampleDatasets(final GSAClientHandler.GSAExampleDatasetsHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_GET_EXAMPLES);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onExampleDatasetSuccess(getExamples(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error != null) {
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

    public static Request loadDataset(final ExternalDataset externalDataset, final GSAClientHandler.GSADatasetLoadHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL_LOAD_DATASET + "/" + externalDataset.getResourceId());
        requestBuilder.setHeader("Accept", "application/json");
        requestBuilder.setHeader("Content-Type", "application/json");

        try {
            request = requestBuilder.sendRequest(externalDataset.getPostData(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onDatasetLoadSuccess(response.getText());
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error != null) {
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
     * Returning the current status of the example's loading.
     */
    public static Request getDatasetLoadingStatus(final String statusToken, final GSAClientHandler.GSAStatusHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_LOADING_STATUS + "/" + statusToken);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onStatusSuccess(getStatus(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error != null) {
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
     * Retrieves a summary of the loaded example.
     * This function is only available once the example has been fully loaded.
     */
    public static Request getDatasetSummary(final String exampleId, final GSAClientHandler.GSADatasetSummaryHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_SUMMARY + "/" + exampleId);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onDatasetSummarySuccess(getExampleSummary(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error != null) {
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
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onAnalysisSubmissionSuccess(response.getText());
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error != null) {
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
     * Returning the current status of the analysis task.
     */
    public static Request getAnalysisStatus(final String analysisId, final GSAClientHandler.GSAStatusHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_STATUS + "/" + analysisId);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onStatusSuccess(getStatus(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error != null) {
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
     * Returning the current status of the Report generation
     */
    public static Request getAnalysisReportsStatus(final String analysisId, final GSAClientHandler.GSAReportsStatusHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_REPORTS_STATUS + "/" + analysisId);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onReportsStatusSuccess(getReportsStatus(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error != null) {
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
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onResultLinksSuccess(getResultLinks(response.getText(), handler));
                            break;
                        default:
                            GSAError error = getError(response.getText(), handler);
                            if (error != null) {
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
     */
    public static Request getExternalDatasources(final GSAClientHandler.GSAExternalDatasourcesHandler handler) {
        Request request = null;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL_GET_EXTERNAL_DATASOURCE);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            handler.onExternalDatasourcesSuccess(getExternalDatasources(response.getText(), handler));
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

    private static List<ExampleDataset> getExamples(final String json, final GSAClientHandler.GSAExampleDatasetsHandler handler) {
        List<ExampleDataset> rtn = null;
        try {
            ExampleDatasetsResult result = GSAFactory.getModelObject(ExampleDatasetsResult.class, "{\"examples\": " + json + "}");
            rtn = result != null ? result.getExamples() : null;
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


    private static ExampleDatasetSummary getExampleSummary(final String json, final GSAClientHandler.GSADatasetSummaryHandler handler) {
        ExampleDatasetSummary rtn = null;
        try {
            rtn = GSAFactory.getModelObject(ExampleDatasetSummary.class, json);
        } catch (GSAException ex) {
            handler.onException("Server unreachable");
            Console.error(ex.getMessage());
        }
        return rtn;
    }

    private static List<ExternalDatasource> getExternalDatasources(String json, final GSAClientHandler.GSAExternalDatasourcesHandler handler) {
        List<ExternalDatasource> rtn = null;
        try {
            ExternalDatasourceResult result = GSAFactory.getModelObject(ExternalDatasourceResult.class, "{\"external\": " + json + "}");
            rtn = result != null ? result.getExternal() : null;
        } catch (GSAException ex) {
            handler.onException("Server unreachable");
            Console.error(ex.getMessage());
        }
        return rtn;
    }

    private static Status getReportsStatus(final String json, final GSAClientHandler.GSAReportsStatusHandler handler) {
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
