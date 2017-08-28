package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.OpenSet;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class OpenSetTable extends EntitySetTable {
    private OpenSet openSet;

    public OpenSetTable(OpenSet openSet) {
        super(openSet);
        this.openSet = openSet;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case REFERENCE_ENTITY:
                return TableRowFactory.getReferenceEntityRow(title, this.openSet.getReferenceEntity());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
