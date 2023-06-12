package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.CellDevelopmentStep;

public class CellDevelopmentStepTable extends ReactionLikeEventTable {

    private CellDevelopmentStep cellDevelopmentStep;

    public CellDevelopmentStepTable(CellDevelopmentStep cellDevelopmentStep) {
        super(cellDevelopmentStep);
        this.cellDevelopmentStep = cellDevelopmentStep;
    }

    @Override
    public Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType) {
            case TISSUE:
                return TableRowFactory.getExternalOntologyRow(title, cellDevelopmentStep.getTissue());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
