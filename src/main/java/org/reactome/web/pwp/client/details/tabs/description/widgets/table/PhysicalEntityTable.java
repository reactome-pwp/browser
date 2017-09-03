package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.*;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PhysicalEntityTable extends OverviewTable {
    PhysicalEntity physicalEntity;

    public PhysicalEntityTable(PhysicalEntity physicalEntity) {
        this.physicalEntity = physicalEntity;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case STABLE_IDENTIFIER:
                return TableRowFactory.getStableIdentifierRow(title, this.physicalEntity.getStId());
            case CELLULAR_COMPARTMENT:
                return TableRowFactory.getGOCellularComponentRow(title, this.physicalEntity.getCompartment());
            case DEDUCED_FROM:
                return TableRowFactory.getInferredFromPhysicalEntityRow(title, this.physicalEntity.getInferredFrom());
            case DEDUCED_ON:
                return TableRowFactory.getOrthologousPhysicalEntityRow(title, this.physicalEntity.getInferredTo());
            case REFERENCES:
                return TableRowFactory.getLiteratureReferencesRow(title, this.physicalEntity.getLiteratureReference());
            case CROSS_REFERENCES:
                List<DatabaseIdentifier> xrefs = this.physicalEntity.getCrossReference();
                if(this.physicalEntity instanceof EntityWithAccessionedSequence){
                    xrefs.addAll(((EntityWithAccessionedSequence) this.physicalEntity).getReferenceEntity().getCrossReference());
                }else if(this.physicalEntity instanceof OpenSet){
                    ReferenceEntity referenceEntity = ((OpenSet) this.physicalEntity).getReferenceEntity();
                    if(referenceEntity!=null){
                        xrefs.addAll(referenceEntity.getCrossReference());
                    }
                }
                return TableRowFactory.getDatabaseIdentifierRow(title, xrefs);
            default:
                return null;
        }
    }
}
