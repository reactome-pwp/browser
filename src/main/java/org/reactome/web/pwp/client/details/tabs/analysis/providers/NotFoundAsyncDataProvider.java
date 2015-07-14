package org.reactome.web.pwp.client.details.tabs.analysis.providers;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.pwp.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.pwp.client.common.analysis.model.IdentifierSummary;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.CustomPager;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.NotFoundTable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NotFoundAsyncDataProvider extends AsyncDataProvider<IdentifierSummary> {
    private NotFoundTable table;
    private SimplePager pager;

    private String token;

    public NotFoundAsyncDataProvider(NotFoundTable table, CustomPager pager, String token) {
        this.table = table;
        this.pager = pager;
        this.token = token;
        this.addDataDisplay(this.table);
    }

    @Override
    protected void onRangeChanged(HasData<IdentifierSummary> display) {
        final Integer page = this.pager.getPage() + 1;

        String url = AnalysisHelper.URL_PREFIX  + "/token/" + this.token + "/notFound?pageSize=" + NotFoundTable.PAGE_SIZE + "&page=" + page;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        List<IdentifierSummary> notFound = new LinkedList<IdentifierSummary>();
                        JSONArray aux = JSONParser.parseStrict(response.getText()).isArray();
                        for (int i = 0; i < aux.size(); i++) {
                            JSONObject obj = aux.get(i).isObject();
                            notFound.add(AnalysisModelFactory.getModelObject(IdentifierSummary.class, obj.toString()));
                        }
                        table.setRowData(pager.getPageStart(), notFound);
                    } catch (AnalysisModelException e) {
                        Console.error(e.getMessage());
                        //ToDo: Look into new Error Handling
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Console.error(exception.getMessage());
                    //ToDo: Look into new Error Handling
                }
            });
        }catch (RequestException ex) {
            Console.error(ex.getMessage());
            //ToDo: Look into new Error Handling
        }
    }


}
