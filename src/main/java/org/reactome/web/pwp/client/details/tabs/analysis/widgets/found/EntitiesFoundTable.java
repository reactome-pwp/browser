package org.reactome.web.pwp.client.details.tabs.analysis.widgets.found;

import com.google.gwt.user.cellview.client.DataGrid;
import org.reactome.web.analysis.client.model.FoundEntities;
import org.reactome.web.analysis.client.model.FoundEntity;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.AbstractColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.EntityResourceColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.ExpressionColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.IdentifierColumn;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesFoundTable extends DataGrid<FoundEntity> {
    public final static Integer PAGE_SIZE = 40;
    private boolean columnsAdded = false;

    public EntitiesFoundTable() {
        super(PAGE_SIZE, item -> item == null ? null : item.getId());

        this.setAutoHeaderRefreshDisabled(true);
        this.setWidth("100%");
        this.setVisible(true);
    }

    public void addColumns(List<String> resources, List<String> columnNames, FoundEntities foundEntities) {
        if (!columnsAdded) {


            List<AbstractColumn<FoundEntity, ?>> columns = new LinkedList<>();

            columns.add(new IdentifierColumn("Identifiers", "found (" + foundEntities.getFound() + ")"));

            for (String resource : resources) {
                if (resource.equals("TOTAL")) continue;
                columns.add(new EntityResourceColumn(resource, "Resource", resource + " (" + foundEntities.getResourceToMappedEntitiesCount().get(resource) + ")"));
            }

            int i = 0;
            for (String columnName : columnNames) {
                columns.add(new ExpressionColumn(i++, columnName));
            }

            for (AbstractColumn<FoundEntity, ?> column : columns) {
                this.addColumn(column, column.buildHeader());
                this.setColumnWidth(column, column.getWidth(), com.google.gwt.dom.client.Style.Unit.PX);
            }
            columnsAdded = true;
        }
    }
}
