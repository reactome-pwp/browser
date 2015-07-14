package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.pwp.client.common.analysis.model.PathwaySummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesRatioColumn extends AbstractColumn<Number> {

    private static final String explanation = "The total entities in the pathway divided by the total number of entities for the entire species for the selected molecular type";

    public EntitiesRatioColumn() {
        super(new NumberCell(NumberFormat.getDecimalFormat()), "Entities", "ratio", explanation);
        setWidth(65);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getRatio();
    }
}
