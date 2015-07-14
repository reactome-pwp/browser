package org.reactome.web.pwp.client.details.tabs.analysis.providers;

import com.google.gwt.http.client.*;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.pwp.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.pwp.client.common.analysis.model.PathwaySummary;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisResult;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.AnalysisResultTable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisAsyncDataProvider extends AsyncDataProvider<PathwaySummary> {

    public interface PageLoadedHandler {
        void onAnalysisAsyncDataProvider(Integer page);
    }

    private AnalysisResultTable table;
    private SimplePager pager;
    private List<PathwaySummary> firstPage;
    private List<PathwaySummary> currentData;
    private String token;
    private String resource;

    private PageLoadedHandler pageLoadedHandler;

    public AnalysisAsyncDataProvider(AnalysisResultTable table, SimplePager pager, AnalysisResult analysisResult, String resource) {
        super(new ProvidesKey<PathwaySummary>() {
            @Override
            public Object getKey(PathwaySummary item) {
                return item == null ? null : item.getDbId();
            }
        });
        this.table = table;
        this.pager = pager;
        this.token = analysisResult.getSummary().getToken();
        this.resource = resource;
        this.firstPage = analysisResult.getPathways();
        this.currentData = this.firstPage;

        this.addDataDisplay(this.table);
    }

    public void addPageLoadedHanlder(PageLoadedHandler pageLoadedHandler) {
        this.pageLoadedHandler = pageLoadedHandler;
    }

    @Override
    protected void onRangeChanged(HasData<PathwaySummary> display) {
//        ColumnSortList sortList = ((AbstractCellTable<PathwaySummary>) display).getColumnSortList();
//        String sortOnName = "name";
//        boolean isAscending = true;
//        if( (sortList!=null) && (sortList.size()>0) ){
//            sortOnName = sortList.get(0).getColumn().getDataStoreName();
//            isAscending = sortList.get(0).isAscending();
//        }

        final Integer page = this.pager.getPage() + 1;
        if(page==1){  //TODO: Find a better way for the first result to be shown so we DO NOT kept it longer ;)
            this.currentData = this.firstPage;
            this.table.setRowData(0, this.firstPage);
            if(pageLoadedHandler !=null){
                pageLoadedHandler.onAnalysisAsyncDataProvider(page);
            }
        }else{
            String url = AnalysisHelper.URL_PREFIX  + "/token/" + this.token + "?pageSize=" + AnalysisResultTable.PAGE_SIZE + "&page=" + page + "&resource=" + this.resource;
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
            requestBuilder.setHeader("Accept", "application/json");
            try {
                requestBuilder.sendRequest(null, new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        try {
                            AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class, response.getText());
                            currentData = result.getPathways();
                            table.setRowData(pager.getPageStart(), result.getPathways());
                            if(pageLoadedHandler !=null){
                                pageLoadedHandler.onAnalysisAsyncDataProvider(page);
                            }
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

    public List<PathwaySummary> getCurrentData() {
        return currentData!=null ? currentData : new LinkedList<PathwaySummary>();
    }

    public interface PageFoundHandler { void onPageFound(Integer page);}

    public void findPathwayPage(Long pathway, final PageFoundHandler handler) {
        String url = AnalysisHelper.URL_PREFIX + "/token/" + this.token + "/page/" + pathway + "?resource=" + this.resource;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    int page = Integer.valueOf(response.getText());
                    handler.onPageFound(page);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Console.error(exception.getMessage());
                    //ToDo: Check for new Error Handling
                }
            });
        }catch (RequestException ex) {
            Console.error(ex.getMessage());
            //ToDo: Check for new Error Handling
        }
    }
}
