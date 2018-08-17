package org.reactome.web.pwp.client.tools.analysis.tissues.client;

import com.google.gwt.http.client.*;
import org.reactome.web.pwp.client.tools.analysis.tissues.client.model.ExperimentError;
import org.reactome.web.pwp.client.tools.analysis.tissues.client.model.ExperimentSummary;
import org.reactome.web.pwp.client.tools.analysis.tissues.client.model.SummariesResult;
import org.reactome.web.pwp.client.tools.analysis.tissues.client.model.factory.ExperimentDigesterException;
import org.reactome.web.pwp.client.tools.analysis.tissues.client.model.factory.ExperimentDigesterFactory;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public abstract class ExperimentSummariesClient {

    private static final String URL = "/ExperimentDigester/experiments/summaries";
    private static Request request;

    public interface Handler {
        void onSummariesSuccess(List<ExperimentSummary> summaries);
        void onSummariesError(ExperimentError error);
        void onSummariesException(String msg);
    }

    public static void getSummaries(final Handler handler) {
        if (request != null && request.isPending()) {
            request.cancel();
        }

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, URL);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            handler.onSummariesSuccess(getSummaries(response.getText(), handler));
                            break;
                        default:
                            handler.onSummariesError(getError(response.getText(), handler));
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onSummariesException(exception.getMessage());
                }
            });
        } catch (RequestException ex) {
            handler.onSummariesException(ex.getMessage());
        }
    }

    private static List<ExperimentSummary> getSummaries(final String json, final Handler handler) {
        List<ExperimentSummary> rtn = null;
        try {
            SummariesResult result = ExperimentDigesterFactory.getModelObject(SummariesResult.class, json);
            rtn = result!=null ? result.getSummaries() : null;
        } catch (ExperimentDigesterException ex) {
            handler.onSummariesException(ex.getMessage());
        }
        return rtn;
    }

    private static ExperimentError getError(final String json, final Handler handler) {
        ExperimentError rtn = null;
        try {
            rtn = ExperimentDigesterFactory.getModelObject(ExperimentError.class, json);
        } catch (ExperimentDigesterException ex) {
            handler.onSummariesException(ex.getMessage());
        }
        return rtn;
    }
}
