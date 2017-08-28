package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.EntitySet;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitySetTable extends PhysicalEntityTable{
    private EntitySet entitySet;

    public EntitySetTable(EntitySet entitySet) {
        super(entitySet);
        this.entitySet = entitySet;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case HAS_MEMBER:
                return TableRowFactory.getPhysicalEntityRow(title, this.entitySet.getHasMember());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
