package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseIdentifier;
import org.reactome.web.pwp.model.client.classes.PhysicalEntity;
import org.reactome.web.pwp.model.client.classes.ReferenceEntity;

import java.util.Collections;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PhysicalEntityTable extends OverviewTable {

    private PhysicalEntity physicalEntity;

    public PhysicalEntityTable(PhysicalEntity physicalEntity) {
        this.physicalEntity = physicalEntity;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType) {
            case NAMES:
                return TableRowFactory.getSynonymRow(title, this.physicalEntity.getName());
            case CELLULAR_COMPARTMENT:
                return TableRowFactory.getGOCellularComponentRow(title, this.physicalEntity.getCompartment());
            case DEDUCED_FROM:
                return TableRowFactory.getInferredFromPhysicalEntityRow(title, this.physicalEntity.getInferredFrom());
            case DEDUCED_ON:
                return TableRowFactory.getOrthologousPhysicalEntityRow(title, this.physicalEntity.getInferredTo());
            case REFERENCES:
                return TableRowFactory.getLiteratureReferencesRow(title, this.physicalEntity.getLiteratureReference());
            case CROSS_REFERENCES:
                List<DatabaseIdentifier> xrefs = physicalEntity.getCrossReference();
                ReferenceEntity re = physicalEntity.getReferenceEntity();
                if (re != null) xrefs.addAll(re.getCrossReference());
                Collections.sort(xrefs);
                if (re != null) xrefs.add(0, new DatabaseIdentifier(re));
                return TableRowFactory.getDatabaseIdentifierRow(title, xrefs);
            default:
                return null;
        }
    }
}
