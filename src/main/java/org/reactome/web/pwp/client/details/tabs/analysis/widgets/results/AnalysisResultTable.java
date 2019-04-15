package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.Event;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns.*;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.EntitiesPathwaySelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.InteractorsPathwaySelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.SortingChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.EntitiesPathwaySelectedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.InteractorsPathwaySelectedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.SortingChangedHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisResultTable extends DataGrid<PathwaySummary> {
    public final static Integer PAGE_SIZE = 20;

    private SingleSelectionModel<PathwaySummary> selectionModel;

    public AnalysisResultTable(List<String> expColumnNames, boolean interactors) {
        super(PAGE_SIZE, CUSTOM_TABLE_RESOURCES, new ProvidesKey<PathwaySummary>() {
            @Override
            public Object getKey(PathwaySummary item) {
                return item == null ? null : item.getDbId();
            }
        });
        this.setAutoHeaderRefreshDisabled(true);
        this.setWidth("100%");
        this.setVisible(true);

        List<AbstractColumn<?>> columns = new LinkedList<>();
        columns.add(new PathwayNameColumn());

        if(interactors) {
            columns.add(new CuratedFoundColumn(new FieldUpdater<PathwaySummary, String>() {
                @Override
                public void update(int i, PathwaySummary pathwaySummary, String s) {
                    if(pathwaySummary.getEntities().getCuratedFound() > 0) {
                        fireEvent(new EntitiesPathwaySelectedEvent(getSelectedObject()));
                    }
                }
            }));
            columns.add(new CuratedTotalColumn());

            columns.add(new InteractorsFoundColumn(new FieldUpdater<PathwaySummary, String>() {
                @Override
                public void update(int i, PathwaySummary pathwaySummary, String s) {
                    if(pathwaySummary.getEntities().getInteractorsFound() > 0) {
                        fireEvent(new InteractorsPathwaySelectedEvent(getSelectedObject()));
                    }
                }
            }));
            columns.add(new InteractorsTotalColumn());

            columns.add(new EntitiesFoundColumn());
        } else {
            columns.add(new EntitiesFoundColumn(new FieldUpdater<PathwaySummary, String>() {
                @Override
                public void update(int index, PathwaySummary object, String value) {
                    fireEvent(new EntitiesPathwaySelectedEvent(getSelectedObject()));
                }
            }));
        }
        columns.add(new EntitiesTotalColumn(interactors));

//        if(!analysisResult.getSummary().getType().equals("EXPRESSION")){
            columns.add(new EntitiesRatioColumn());
            columns.add(new EntitiesPValueColumn());
            columns.add(new EntitiesFDRColumn());
            columns.add(new ReactionsFoundColumn());
            columns.add(new ReactionsTotalColumn());
            columns.add(new ReactionsRatioColumn());
//        }

        int i = 0;
        for (String columnName : expColumnNames) {
            columns.add(new ExpressionColumn(i++, columnName));
        }

        columns.add(new SpeciesColumn());

        for (AbstractColumn<?> column : columns) {
            Header header = column.buildHeader();
            header.setUpdater(new ValueUpdater() {
                @Override
                public void update(Object value) {
                    fireEvent(new SortingChangedEvent(column.getSortingBy()));
                }
            });

            this.addColumn(column, header);
            this.setColumnWidth(column, column.getWidth(), com.google.gwt.dom.client.Style.Unit.PX);
        }

        this.selectionModel = new SingleSelectionModel<>();
        this.setSelectionModel(selectionModel);

        this.sinkEvents(Event.ONMOUSEOUT);
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler){
        return this.addHandler(handler, MouseOutEvent.getType());
    }

    public HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler){
        return this.selectionModel.addSelectionChangeHandler(handler);
    }

    public HandlerRegistration addEntitiesPathwaySelectedHandler(EntitiesPathwaySelectedHandler handler){
        return this.addHandler(handler, EntitiesPathwaySelectedEvent.TYPE);
    }

    public HandlerRegistration addInteractorsPathwaySelectedHandler(InteractorsPathwaySelectedHandler handler){
        return this.addHandler(handler, InteractorsPathwaySelectedEvent.TYPE);
    }

    public HandlerRegistration addSortingChangedHandler(SortingChangedHandler handler) {
        return this.addHandler(handler, SortingChangedEvent.TYPE);
    }

    public PathwaySummary getSelectedObject(){
        return this.selectionModel.getSelectedObject();
    }

    public void scrollToItem(int index){
        this.getRowElement(index).scrollIntoView();
    }

    public void selectPathway(PathwaySummary pathwaySummary, Integer index){
        selectionModel.setSelected(pathwaySummary, true);
        this.getRowElement(index).scrollIntoView();
    }

    public void clearSelection(){
        this.selectionModel.clear();
    }

    public void switchClusterColouring(boolean enable) {
        if (enable) {
            this.setRowStyles(clustersRowStyles);
        } else {
            this.setRowStyles(null);
        }

        this.redraw();
    }


    private RowStyles<PathwaySummary> clustersRowStyles = new RowStyles<PathwaySummary>() {
        @Override
        public String getStyleNames(PathwaySummary row, int rowIndex) {
            if (selectionModel.isSelected(row)) {
                return null;
            } else {
                if (rowIndex % 2 == 0) {
                    return RESOURCES.getCSS().clusterA();
                } else {
                    return RESOURCES.getCSS().clusterB();
                }
            }
        }
    };

    private static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    @CssResource.ImportedWithPrefix("pwp-AnalysisResultTable")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/results/ClusterRowsStyles.css";

        String clusterA();

        String clusterB();
    }

    private static CustomResources CUSTOM_TABLE_RESOURCES;
    static {
        CUSTOM_TABLE_RESOURCES = GWT.create(CustomResources.class);
    }

    public interface CustomResources extends DataGrid.Resources {

        @Override
        @Source(CustomStyle.CSS)
        CustomStyle dataGridStyle();

        @CssResource.ImportedWithPrefix("pwp-CustomResultsTable")
        interface CustomStyle extends Style {
            String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/results/CustomResultsTable.css";
        }
    }
}
