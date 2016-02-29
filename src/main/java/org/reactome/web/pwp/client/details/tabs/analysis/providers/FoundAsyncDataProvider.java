package org.reactome.web.pwp.client.details.tabs.analysis.providers;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.PathwayIdentifier;
import org.reactome.web.analysis.client.model.PathwayIdentifiers;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.CustomPager;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.FoundTable;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.NotFoundTable;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FoundAsyncDataProvider extends AsyncDataProvider<PathwayIdentifier> {
    private FoundTable table;
    private SimplePager pager;

    private String token;
    private String resource;
    private Long pathwayId;

    public FoundAsyncDataProvider(FoundTable table, CustomPager pager, String token, Long pathwayId, String resource) {
        this.table = table;
        this.pager = pager;
        this.token = token;
        this.resource = resource;
        this.pathwayId = pathwayId;
        this.addDataDisplay(this.table);
    }

    @Override
    protected void onRangeChanged(HasData<PathwayIdentifier> display) {
        final Integer page = this.pager.getPage() + 1;

        AnalysisClient.getPathwayIdentifiers(token, resource, pathwayId, NotFoundTable.PAGE_SIZE, page, new AnalysisHandler.Identifiers() {
            @Override
            public void onPathwayIdentifiersLoaded(PathwayIdentifiers identifiers, long time) {
                table.setRowCount(identifiers.getFound());
                table.setRowData(pager.getPageStart(), identifiers.getIdentifiers());
            }

            @Override
            public void onPathwayIdentifiersNotFound(long time) {
                Console.error("onPathwayIdentifiersNotFound", FoundAsyncDataProvider.this);
            }

            @Override
            public void onPathwayIdentifiersError(AnalysisError error) {
                Console.error(error.getReason(), FoundAsyncDataProvider.this);
            }

            @Override
            public void onAnalysisServerException(String message) {
                Console.error(message, FoundAsyncDataProvider.this);
            }
        });
    }

}
