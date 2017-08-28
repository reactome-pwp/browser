package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.common.widgets.panels.TextPanel;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DefaultTable extends OverviewTable{
    private DatabaseObject databaseObject;

    public DefaultTable(DatabaseObject databaseObject) {
        this.databaseObject = databaseObject;
    }

    @Override
    public void initialize() {
        initWidget(getTableRow(null));
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        TextPanel textPanel = new TextPanel("Table no available for " + databaseObject.toString());
        return TableRowFactory.getOverviewRow("Database Object", textPanel);
    }
}