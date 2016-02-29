package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.RowHoverEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.diagram.util.Console;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.common.widgets.button.CustomButton;
import org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.CustomPager;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwayHoveredEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwayHoveredResetEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwaySelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.ResultPathwaySelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwayHoveredHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwayHoveredResetHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwaySelectedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.ResultPathwaySelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisResultPanel extends DockLayoutPanel implements SelectionChangeEvent.Handler, RowHoverEvent.Handler, MouseOutHandler,
        AnalysisAsyncDataProvider.PageLoadedHandler,
        AnalysisHandler.Page,
        ResultPathwaySelectedHandler {

    private AnalysisAsyncDataProvider dataProvider;
    private AnalysisResultTable table;
    private CustomPager pager;

    private Long candidateForSelection;
    private Long selected;
    private Long hovered;
    private String resource;

    public AnalysisResultPanel() {
        super(Style.Unit.EM);

        this.pager = new CustomPager(); // Create paging controls.
        this.pager.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    }

    public HandlerRegistration addResultPathwaySelectedHandler(ResultPathwaySelectedHandler handler){
        return this.addHandler(handler, ResultPathwaySelectedEvent.TYPE);
    }

    public HandlerRegistration addPathwaySelectedHandler(PathwaySelectedHandler handler){
        return this.addHandler(handler, PathwaySelectedEvent.TYPE);
    }

    public HandlerRegistration addPathwayHoveredHandler(PathwayHoveredHandler handler){
        return this.addHandler(handler, PathwayHoveredEvent.TYPE);
    }

    public HandlerRegistration addPathwayHoveredResetHandler(PathwayHoveredResetHandler handler){
        return this.addHandler(handler, PathwayHoveredResetEvent.TYPE);
    }

    public void clearSelection() {
        if(this.table!=null){
            this.selected = null;
            this.table.clearSelection();
        }
    }

    public Long getSelected() {
        return selected;
    }

    @Override
    public void onAnalysisAsyncDataProvider(Integer page) {
        this.selectPathway(candidateForSelection);
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        fireEvent(new PathwayHoveredResetEvent());
        this.hovered = null;
    }

    @Override
    public void onRowHover(RowHoverEvent event) {
        PathwaySummary ps = dataProvider.getCurrentData().get(event.getHoveringRow().getRowIndex());
        if(ps!=null && !ps.getDbId().equals(this.hovered)){
            this.hovered = ps.getDbId();
            fireEvent(new PathwayHoveredEvent(this.hovered));
        }
    }

    @Override
    public void onSelectionChange(SelectionChangeEvent event) {
        PathwaySummary ps = this.table.getSelectedObject();
        if(ps!=null){
            candidateForSelection = ps.getDbId(); //Candidate always to be set here
            selected = ps.getDbId(); //Please DO NOT use the stable identifier here
            fireEvent(new PathwaySelectedEvent(selected));
        }
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void showPage(int page) {
        if(this.pager!=null){
            this.pager.setPage(page - 1);
        }
    }

    public void showResult(final AnalysisResult analysisResult, final String resource) {
//        ColumnSortEvent.ListHandler<PathwaySummary> sortHandler = new ColumnSortEvent.ListHandler<PathwaySummary>(analysisResult.getPathways());
        this.table = new AnalysisResultTable(analysisResult.getExpression().getColumnNames());
        this.table.addSelectionChangeHandler(this);
        this.table.addRowHoverHandler(this);
        this.table.addMouseOutHandler(this);

        this.table.addResultPathwaySelectedHandler(this);
        this.table.setRowCount(analysisResult.getPathwaysFound());

        this.pager.setDisplay(this.table);
        this.pager.setPageSize(AnalysisResultTable.PAGE_SIZE);

        this.dataProvider = new AnalysisAsyncDataProvider(table, pager, analysisResult, resource);
        this.dataProvider.addPageLoadedHanlder(this);

        CustomButton downloadCVS = new CustomButton(CommonImages.INSTANCE.downloadFile(), "Result");
        downloadCVS.setTitle("Click to download the pathway analysis results in Comma Separated Values format for " + resource);
        downloadCVS.getElement().getStyle().setFloat(Style.Float.LEFT);
        downloadCVS.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.open("/AnalysisService/download/" + analysisResult.getSummary().getToken() + "/pathways/" + resource + "/result.csv", "_self", "");
            }
        });

        CustomButton downloadMapping = new CustomButton(CommonImages.INSTANCE.downloadFile(), "Mapping");
        downloadMapping.setTitle("Click to download the identifier mapping between the submitted data and the selected resource (" + resource + ")");
        downloadMapping.getElement().getStyle().setFloat(Style.Float.LEFT);
        downloadMapping.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.open("/AnalysisService/download/" + analysisResult.getSummary().getToken() + "/entities/found/" + resource + "/mapping.csv", "_self", "");
            }
        });

        this.clear();
        FlowPanel pagerPanel = new FlowPanel();
        pagerPanel.setWidth("100%");
        pagerPanel.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        pagerPanel.add(downloadCVS);
        pagerPanel.add(downloadMapping);
        pagerPanel.add(pager);
        this.addSouth(pagerPanel, 2);

        this.add(this.table);
    }

    public void scrollToSelected() {
        if(table!=null && selected!=null){
            int i = 0;
            for (PathwaySummary pathwaySummary : dataProvider.getCurrentData()) {
                if(pathwaySummary.getDbId().equals(selected)){
                    table.scrollToItem(i);
                    return;
                }
                i++;
            }
        }
    }

    public void selectPathway(Long pathway) {
        if(pathway==null || pathway.equals(selected)) return;
        candidateForSelection = pathway;
        if(dataProvider!=null){
            int i = 0;
            for (PathwaySummary pathwaySummary : dataProvider.getCurrentData()) {
                if(pathwaySummary.getDbId().equals(pathway)){
                    selected = candidateForSelection;
                    table.selectPathway(pathwaySummary, i);
                    return;
                }
                i++;
            }
            this.dataProvider.findPathwayPage(pathway, this);
        }
    }

    public void setInitialState(){
        this.candidateForSelection = null;
        this.selected = null;
    }

    @Override
    public void onAnalysisServerException(String message) {
        Console.warn(getClass().getSimpleName() + " --> TODO");
    }

    @Override
    public void onPageFound(Integer page) {
        if( page == -1 ){
            this.clearSelection();
        }else{
            this.showPage(page);
        }
    }

    @Override
    public void onPageError(AnalysisError error) {
        //TODO
    }

    @Override
    public void onPathwayFoundEntitiesSelected(ResultPathwaySelectedEvent event) {
        fireEvent(event);
    }
}
