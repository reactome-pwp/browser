package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.EntityWithAccessionedSequence;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EWASTable extends GenomeEncodedEntityTable {
    private EntityWithAccessionedSequence ewas;
    
    public EWASTable(EntityWithAccessionedSequence ewas) {
        super(ewas);
        this.ewas = ewas;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case COORDINATES:
                return TableRowFactory.getCoordinatesRow(title, this.ewas);
//            case REFERENCE_ENTITY:
//                return TableRowFactory.getReferenceEntityRow(title, this.ewas.getReferenceEntity());
            case MODIFICATION:
                return TableRowFactory.getAbstractModifiedResidue(title, this.ewas.getHasModifiedResidue());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
