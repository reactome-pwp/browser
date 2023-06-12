package org.reactome.web.pwp.client.details.tabs.description.widgets.table;


import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.Cell;


public class CellTable extends PhysicalEntityTable {

    private final Cell cell;

    public CellTable(Cell cell) {
        super(cell);
        this.cell = cell;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType) {
            case ORGAN:
                return TableRowFactory.getAnatomyRow(title, this.cell.getOrgan());
            case TISSUE:
                return TableRowFactory.getAnatomyRow(title, this.cell.getTissue());
            case TISSUE_LAYER:
                return TableRowFactory.getAnatomyRow(title, this.cell.getTissueLayer());
            case PROTEIN_MARKERS:
                return TableRowFactory.getCellMarkersRow(title, this.cell, this.cell.getProteinMarker());
            case RNA_MARKERS:
                return TableRowFactory.getCellMarkersRow(title, this.cell, this.cell.getRnaMarker());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
