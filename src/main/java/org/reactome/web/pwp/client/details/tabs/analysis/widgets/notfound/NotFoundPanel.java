package org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.common.widgets.button.CustomButton;
import org.reactome.web.pwp.client.details.tabs.analysis.providers.NotFoundAsyncDataProvider;
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

        CustomButton downloadNotFound = new CustomButton(CommonImages.INSTANCE.downloadFile(), "Not found");
        downloadNotFound.setTitle("Click to download the not found identifiers");
        downloadNotFound.getElement().getStyle().setFloat(Style.Float.LEFT);
        downloadNotFound.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.open("/AnalysisService/download/" + token + "/entities/notfound/not_found.csv", "_self", "");
            }
        });

        this.clear();
        FlowPanel pagerPanel = new FlowPanel();
        pagerPanel.setWidth("100%");
        pagerPanel.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        pagerPanel.add(downloadNotFound);
        pagerPanel.add(pager);
        this.addSouth(pagerPanel, 2);

        this.add(this.table);
    }

    public void setAnalysisDetails(String token, Integer notFound) {
        this.token = token;
        this.notFound = notFound;
        this.forceLoad = true;
    }
}
