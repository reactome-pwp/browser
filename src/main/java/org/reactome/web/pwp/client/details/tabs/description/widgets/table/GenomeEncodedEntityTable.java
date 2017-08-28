package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.model.client.classes.GenomeEncodedEntity;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GenomeEncodedEntityTable extends PhysicalEntityTable {
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    private GenomeEncodedEntity genomeEncodedEntity;

    public GenomeEncodedEntityTable(GenomeEncodedEntity genomeEncodedEntity) {
        super(genomeEncodedEntity);
        this.genomeEncodedEntity = genomeEncodedEntity;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
//        String title = propertyType.getTitle();
        switch (propertyType){
            default:
                return super.getTableRow(propertyType);
        }
    }
}
