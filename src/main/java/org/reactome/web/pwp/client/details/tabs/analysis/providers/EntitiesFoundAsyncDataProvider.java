package org.reactome.web.pwp.client.details.tabs.analysis.providers;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.FoundEntities;
import org.reactome.web.analysis.client.model.FoundEntity;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.CustomPager;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.EntitiesFoundTable;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.NotFoundTable;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesFoundAsyncDataProvider extends AsyncDataProvider<FoundEntity> {
    private EntitiesFoundTable table;
    private SimplePager pager;

    private String token;
    private String resource;
    private Long pathwayId;

    public EntitiesFoundAsyncDataProvider(EntitiesFoundTable table, CustomPager pager, String token, Long pathwayId, String resource) {
        this.table = table;
        this.pager = pager;
        this.token = token;
        this.resource = resource;
        this.pathwayId = pathwayId;
        this.addDataDisplay(this.table);
    }

    @Override
    protected void onRangeChanged(HasData<FoundEntity> display) {
        final Integer page = this.pager.getPage() + 1;

        AnalysisClient.getPathwayFoundEntities(token, resource, pathwayId, NotFoundTable.PAGE_SIZE, page, new AnalysisHandler.Entities() {
            @Override
            public void onPathwayEntitiesLoaded(FoundEntities entities, long time) {
                table.setRowCount(entities.getFound());
                table.setRowData(pager.getPageStart(), entities.getIdentifiers());
            }

            @Override
            public void onPathwayEntitiesNotFound(long time) {
                Console.error("onPathwayEntitiesNotFound", EntitiesFoundAsyncDataProvider.this);
            }

            @Override
            public void onPathwayEntitiesError(AnalysisError error) {
                Console.error(error.getReason(), EntitiesFoundAsyncDataProvider.this);
            }

            @Override
            public void onAnalysisServerException(String message) {
                Console.error(message, EntitiesFoundAsyncDataProvider.this);
            }
        });
    }

}
