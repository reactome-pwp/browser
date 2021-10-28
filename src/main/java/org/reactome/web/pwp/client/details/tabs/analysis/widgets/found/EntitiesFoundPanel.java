package org.reactome.web.pwp.client.details.tabs.analysis.widgets.found;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.details.tabs.analysis.providers.EntitiesFoundAsyncDataProvider;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.CustomPager;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.NotFoundTable;

import java.util.Collections;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesFoundPanel extends DockLayoutPanel {
    private String token;
    private String resource = "TOTAL";
    private Long pathwayId;
    private String pathwayName;
    private boolean forceLoad = true;

    private CustomPager pager;

    private Handler handler;

    public interface Handler {
        void onEntitiesFoundPanelClosed();
    }

    public EntitiesFoundPanel(Handler handler) {
        super(Style.Unit.EM);
        this.handler = handler;
        this.pager = new CustomPager(); // Create paging controls.
        this.pager.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.pager.setPageSize(NotFoundTable.PAGE_SIZE);
    }

    public void showFoundEntities(List<String> resources, List<String> columnNames) {
        if (!forceLoad) return; //Will only force to reload the data when the analysis details has been changed
        this.forceLoad = false;

        EntitiesFoundTable table;
        table = new EntitiesFoundTable();
        this.pager.setDisplay(table);

        new EntitiesFoundAsyncDataProvider(table, this.pager, this.token, this.pathwayId, this.resource, this.resource.equals("TOTAL") ? resources : Collections.singletonList(this.resource), columnNames);

        this.clear();
        this.addNorth(getHeader(pathwayName), 1.2);
        FlowPanel pagerPanel = new FlowPanel();
        pagerPanel.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().panelFooter());
        pagerPanel.add(pager);
        this.addSouth(pagerPanel, 1.6);

        this.add(table);
    }

    public void setAnalysisDetails(String token, PathwaySummary pathway) {
        this.token = token;
        this.pathwayId = pathway.getDbId();
        this.pathwayName = pathway.getName();
        this.forceLoad = true;
    }

    public void setResource(String resource) {
        this.resource = resource;
        this.forceLoad = true;
    }

    private Widget getHeader(String pathwayName) {
        Image icon = new Image(AnalysisTabStyleFactory.RESOURCES.backIcon());

        Anchor backAnchor = new Anchor("Back to results overview");
        backAnchor.addClickHandler(event -> handler.onEntitiesFoundPanelClosed());

        FlowPanel fp = new FlowPanel();
        fp.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().panelHeader());
        fp.add(icon);
        fp.add(backAnchor);
        fp.add(new Label("Matching identifiers for: " + pathwayName));

        return fp;
    }
}
