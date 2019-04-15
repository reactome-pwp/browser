package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import org.reactome.web.analysis.client.model.PathwaySummary;

import static org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType.TOTAL_ENTITIES;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesTotalColumn extends AbstractColumn<Number> {

    private static final String standard = "The total number of identifiers in the pathway for the selected molecular type";
    private static final String interactors = "The total number of identifiers in the pathway aggregating curated and interactors for the selected molecular type";

    public EntitiesTotalColumn(boolean includeInteractors) {
        super(new NumberCell(), "Entities", "Total", includeInteractors ? interactors : standard);
        setWidth(65);
        setSortingBy(TOTAL_ENTITIES);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getTotal();
    }
}
