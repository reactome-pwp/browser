package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 *
 */
public class PathwayTable extends EventTable {
    private Pathway pathway;
    
    public PathwayTable(Pathway pathway) {
        super(pathway);
        this.pathway = pathway;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case DOI:
                return TableRowFactory.getTextPanelRow(title, this.pathway.getDoi());
            case NORMAL_PATHWAY:
                return TableRowFactory.getEventRow(title, this.pathway.getNormalPathway());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
