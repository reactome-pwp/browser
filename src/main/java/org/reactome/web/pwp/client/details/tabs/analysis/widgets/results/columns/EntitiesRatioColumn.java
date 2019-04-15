package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.analysis.client.model.PathwaySummary;

import static org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType.ENTITIES_RATIO;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesRatioColumn extends AbstractColumn<Number> {

    private static final String explanation = "The total entities in the pathway divided by the total number of entities for the entire species for the selected molecular type";

    public EntitiesRatioColumn() {
        super(new NumberCell(NumberFormat.getDecimalFormat()), "Entities", "ratio", explanation);
        setWidth(65);
        setSortingBy(ENTITIES_RATIO);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getRatio();
    }
}
