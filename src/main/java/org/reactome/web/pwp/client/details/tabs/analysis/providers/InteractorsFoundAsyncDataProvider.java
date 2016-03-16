package org.reactome.web.pwp.client.details.tabs.analysis.providers;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.FoundInteractor;
import org.reactome.web.analysis.client.model.FoundInteractors;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.CustomPager;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.InteractorsFoundTable;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.NotFoundTable;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InteractorsFoundAsyncDataProvider extends AsyncDataProvider<FoundInteractor> {
    private InteractorsFoundTable table;
    private SimplePager pager;

    private String token;
    private String resource;
    private Long pathwayId;

    public InteractorsFoundAsyncDataProvider(InteractorsFoundTable table, CustomPager pager, String token, Long pathwayId, String resource) {
        this.table = table;
        this.pager = pager;
        this.token = token;
        this.resource = resource;
        this.pathwayId = pathwayId;
        this.addDataDisplay(this.table);
    }

    @Override
    protected void onRangeChanged(HasData<FoundInteractor> display) {
        final Integer page = this.pager.getPage() + 1;

        AnalysisClient.getPathwayFoundInteractors(token, resource, pathwayId, NotFoundTable.PAGE_SIZE, page, new AnalysisHandler.Interactors() {
            @Override
            public void onPathwayInteractorsLoaded(FoundInteractors interactors, long time) {
                table.setRowCount(interactors.getFound());
                table.setRowData(pager.getPageStart(), interactors.getIdentifiers());
            }

            @Override
            public void onPathwayInteractorsNotFound(long time) {
                Console.error("onPathwayInteractorsNotFound", InteractorsFoundAsyncDataProvider.this);
            }

            @Override
            public void onPathwayInteractorsError(AnalysisError error) {
                Console.error(error.getReason(), InteractorsFoundAsyncDataProvider.this);
            }

            @Override
            public void onAnalysisServerException(String message) {
                Console.error(message, InteractorsFoundAsyncDataProvider.this);
            }
        });
    }

}
