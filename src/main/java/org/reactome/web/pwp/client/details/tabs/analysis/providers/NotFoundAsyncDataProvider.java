package org.reactome.web.pwp.client.details.tabs.analysis.providers;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.IdentifierSummary;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.CustomPager;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.NotFoundTable;

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

        AnalysisClient.getNotFoundIdentifiers(token, NotFoundTable.PAGE_SIZE, page, new AnalysisHandler.NotFoundIdentifiers() {
            @Override
            public void onNotFoundIdentifiersLoaded(List<IdentifierSummary> notFoundIdentifiers) {
                table.setRowData(pager.getPageStart(), notFoundIdentifiers);
            }

            @Override
            public void onNotFoundIdentifiersError(AnalysisError error) {
                Console.error(error.getReason());
                //ToDo: Look into new Error Handling
            }

            @Override
            public void onAnalysisServerException(String message) {
                Console.error(message);
                //ToDo: Look into new Error Handling
            }
        });
    }


}
