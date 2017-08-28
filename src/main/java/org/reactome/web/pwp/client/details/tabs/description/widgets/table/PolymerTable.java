package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.Polymer;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PolymerTable extends PhysicalEntityTable {
    private Polymer polymer;

    public PolymerTable(Polymer polymer) {
        super(polymer);
        this.polymer = polymer;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case MAX_UNITS:
                return TableRowFactory.getTextPanelRow(title, this.polymer.getMaxUnitCount());
            case MIN_UNITS:
                return TableRowFactory.getTextPanelRow(title, this.polymer.getMinUnitCount());
            case REPEATED_UNITS:
                return TableRowFactory.getPhysicalEntityRow(title, this.polymer.getRepeatedUnit());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
