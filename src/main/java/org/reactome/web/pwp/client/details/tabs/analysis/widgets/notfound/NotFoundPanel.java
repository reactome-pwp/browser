package org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.providers.NotFoundAsyncDataProvider;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.CustomPager;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NotFoundPanel extends DockLayoutPanel {
    private String token;
    private Integer notFound;
    private boolean forceLoad;

    private NotFoundTable table;
    private CustomPager pager;
    private NotFoundAsyncDataProvider dataProvider;

    public NotFoundPanel() {
        super(Style.Unit.EM);

        this.pager = new CustomPager(); // Create paging controls.
        this.pager.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.pager.setPageSize(NotFoundTable.PAGE_SIZE);
    }

    public void showNotFound(final String token, List<String> columnNames){
        if(!forceLoad) return; //Will only force to reload the data when the analysis details has been changed
        this.forceLoad = false;

        this.table = new NotFoundTable(columnNames);
        this.table.setRowCount(this.notFound);

        this.pager.setDisplay(this.table);

        this.dataProvider = new NotFoundAsyncDataProvider(this.table, this.pager, this.token);

        this.clear();
        FlowPanel pagerPanel = new FlowPanel();
        pagerPanel.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().panelFooter());
        pagerPanel.add(pager);
        this.addSouth(pagerPanel, 1.9);

        this.add(this.table);
    }

    public void setAnalysisDetails(String token, Integer notFound) {
        this.token = token;
        this.notFound = notFound;
        this.forceLoad = true;
    }
}
