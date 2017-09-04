package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseIdentifier;
import org.reactome.web.pwp.model.client.classes.PhysicalEntity;
import org.reactome.web.pwp.model.client.classes.ReferenceEntity;

import java.util.Collections;
import java.util.LinkedList;
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
        switch (propertyType) {
            case CELLULAR_COMPARTMENT:
                return TableRowFactory.getGOCellularComponentRow(title, this.physicalEntity.getCompartment());
            case DEDUCED_FROM:
                return TableRowFactory.getInferredFromPhysicalEntityRow(title, this.physicalEntity.getInferredFrom());
            case DEDUCED_ON:
                return TableRowFactory.getOrthologousPhysicalEntityRow(title, this.physicalEntity.getInferredTo());
            case REFERENCES:
                return TableRowFactory.getLiteratureReferencesRow(title, this.physicalEntity.getLiteratureReference());
            case CROSS_REFERENCES:
                ReferenceEntity re = physicalEntity.getReferenceEntity();
                List<DatabaseIdentifier> xrefs = re != null ? re.getCrossReference() : new LinkedList<>();
                Collections.sort(xrefs);
                if (re != null) xrefs.add(0, new DatabaseIdentifier(re));
                return TableRowFactory.getDatabaseIdentifierRow(title, xrefs);
            default:
                return null;
        }
    }
}
