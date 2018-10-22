package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.Drug;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DrugTable extends PhysicalEntityTable {
    private Drug drug;

    public DrugTable(Drug drug) {
        super(drug);
        this.drug = drug;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case REFERENCE_THERAPEUTIC:
                return TableRowFactory.getReferenceTherapeuticRow(title, drug.getReferenceTherapeutic());
            case DISEASE:
                return TableRowFactory.getExternalOntologyRow(title, drug.getDisease());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
