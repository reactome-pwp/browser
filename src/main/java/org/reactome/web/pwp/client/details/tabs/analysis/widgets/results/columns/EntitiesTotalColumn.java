package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import org.reactome.web.pwp.client.common.analysis.model.PathwaySummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesTotalColumn extends AbstractColumn<Number> {

    private static final String explanation = "The total number of identifiers in the pathway for the selected molecular type";

    public EntitiesTotalColumn() {
        super(new NumberCell(), "Entities", "Total", explanation);
        setWidth(65);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getTotal();
    }
}
