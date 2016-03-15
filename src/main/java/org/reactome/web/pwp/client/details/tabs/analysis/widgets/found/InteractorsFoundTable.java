package org.reactome.web.pwp.client.details.tabs.analysis.widgets.found;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ProvidesKey;
import org.reactome.web.analysis.client.model.PathwayInteractor;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.AbstractColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.ExpressionColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.InteractorColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.InteractorResourceColumn;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InteractorsFoundTable extends DataGrid<PathwayInteractor> {
    public final static Integer PAGE_SIZE = 40;

    public InteractorsFoundTable(List<String> resources, List<String> columnNames) {
        super(PAGE_SIZE, new ProvidesKey<PathwayInteractor>() {
            @Override
            public Object getKey(PathwayInteractor item) {
                return item == null ? null : item.getIdentifier();
            }
        });

        this.addColumns(resources, columnNames);
        this.setAutoHeaderRefreshDisabled(true);
        this.setWidth("100%");
        this.setVisible(true);
    }

    private void addColumns(List<String> resources, List<String> columnNames){
        List<AbstractColumn<PathwayInteractor, ?>> columns = new LinkedList<>();

        for (String resource : resources) {
            if(resource.equals("TOTAL")) continue;
            columns.add(new InteractorResourceColumn("Submitted", "Identifiers"));
        }

        columns.add(new InteractorColumn("Interact", "with"));

        int i = 0;
        for (String columnName : columnNames) {
            columns.add(new ExpressionColumn(i++, columnName));
        }

        for (AbstractColumn<PathwayInteractor, ?> column : columns) {
            this.addColumn(column, column.buildHeader());
            this.setColumnWidth(column, column.getWidth(), com.google.gwt.dom.client.Style.Unit.PX);
        }
    }
}
