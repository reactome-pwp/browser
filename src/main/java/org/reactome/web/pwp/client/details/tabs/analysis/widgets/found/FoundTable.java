package org.reactome.web.pwp.client.details.tabs.analysis.widgets.found;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ProvidesKey;
import org.reactome.web.pwp.client.common.analysis.model.PathwayIdentifier;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.AbstractColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.ExpressionColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.IdentifierColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.ResourceColumn;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FoundTable extends DataGrid<PathwayIdentifier> {
    public final static Integer PAGE_SIZE = 40;

    public FoundTable(List<String> resources, List<String> columnNames) {
        super(PAGE_SIZE, new ProvidesKey<PathwayIdentifier>() {
            @Override
            public Object getKey(PathwayIdentifier item) {
                return item == null ? null : item.getIdentifier();
            }
        });

        this.addColumns(resources, columnNames);
        this.setAutoHeaderRefreshDisabled(true);
        this.setWidth("100%");
        this.setVisible(true);
    }

    private void addColumns(List<String> resources, List<String> columnNames){
        List<AbstractColumn<?>> columns = new LinkedList<AbstractColumn<?>>();

        columns.add(new IdentifierColumn("Identifiers", "found"));

        for (String resource : resources) {
            if(resource.equals("TOTAL")) continue;
            columns.add(new ResourceColumn(resource, "Resource", resource));
        }

        int i = 0;
        for (String columnName : columnNames) {
            columns.add(new ExpressionColumn(i++, columnName));
        }

        for (AbstractColumn<?> column : columns) {
            this.addColumn(column, column.buildHeader());
            this.setColumnWidth(column, column.getWidth(), com.google.gwt.dom.client.Style.Unit.PX);
        }
    }
}
