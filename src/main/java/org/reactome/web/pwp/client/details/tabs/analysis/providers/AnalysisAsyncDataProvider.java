package org.reactome.web.pwp.client.details.tabs.analysis.providers;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.PathwaySummary;
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
        if (page == 1) {  //TODO: Find a better way for the first result to be shown so we DO NOT kept it longer ;)
            this.currentData = this.firstPage;
            this.table.setRowData(0, this.firstPage);
            if (pageLoadedHandler != null) {
                pageLoadedHandler.onAnalysisAsyncDataProvider(page);
            }
        } else {
            AnalysisClient.getResult(token, resource, AnalysisResultTable.PAGE_SIZE, page, new AnalysisHandler.Result() {
                @Override
                public void onAnalysisResult(AnalysisResult result, long time) {
                    currentData = result.getPathways();
                    table.setRowData(pager.getPageStart(), result.getPathways());
                    if (pageLoadedHandler != null) {
                        pageLoadedHandler.onAnalysisAsyncDataProvider(page);
                    }
                }

                @Override
                public void onAnalysisError(AnalysisError error) {
                    Console.error(error.getReason());
                }

                @Override
                public void onAnalysisServerException(String message) {
                    Console.error(message);
                    //ToDo: Look into new Error Handling
                }
            });
        }
    }

    public List<PathwaySummary> getCurrentData() {
        return currentData != null ? currentData : new LinkedList<PathwaySummary>();
    }

    public void findPathwayPage(Long pathway, AnalysisHandler.Page handler) {
        AnalysisClient.findPathwayPage(pathway, this.token, this.resource, handler);
    }
}
