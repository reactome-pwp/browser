package org.reactome.web.pwp.client.tools.analysis.gsa.client;

import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.FormPanel;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.*;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAException;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory.GSAFactory;

import java.util.List;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAClient {

    private static final String URL_METHODS = "/gsa/0.1/methods";
    private static final String URL_TYPES = "/gsa/0.1/types";
    private static final String URL_UPLOAD_FILE = "/gsa/upload";

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
                            handler.onError(getError(response.getText(), handler));
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
                            handler.onError(getError(response.getText(), handler));
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
            handler.onException(ex.getMessage());
        }
        return rtn;
    }

    private static List<DatasetType> getTypes(final String json, final GSAClientHandler.GSADatasetTypesHandler handler) {
        List<DatasetType> rtn = null;
        try {
            DatasetTypesResult result = GSAFactory.getModelObject(DatasetTypesResult.class, "{\"types\": " + json + "}");
            rtn = result != null ? result.getTypes() : null;
        } catch (GSAException ex) {
            handler.onException(ex.getMessage());
        }
        return rtn;
    }

    private static GSAError getError(final String json, final GSAClientHandler handler) {
        GSAError rtn = null;
        try {
            rtn = GSAFactory.getModelObject(GSAError.class, json);
        } catch (GSAException ex) {
            handler.onException(ex.getMessage());
        }
        return rtn;
    }

    public static void uploadFile(FormPanel form) {
        form.setMethod(FormPanel.METHOD_POST);
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setAction(URL_UPLOAD_FILE);
        form.addSubmitHandler(new FormPanel.SubmitHandler() {
            @Override
            public void onSubmit(FormPanel.SubmitEvent event) {

            }
        });
        form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {

            }
        });
        form.submit();
    }

}
