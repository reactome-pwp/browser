package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import org.reactome.web.analysis.client.model.PathwaySummary;

import static org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType.TOTAL_INTERACTORS;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InteractorsTotalColumn extends AbstractColumn<Number> {

    private static final String explanation = "The total number of identifiers that match to interactors for entities in the pathway for the selected molecular type";

    public InteractorsTotalColumn() {
        super(new NumberCell(), "Interactors", "Total", explanation);
        setWidth(76);
        setSortingBy(TOTAL_INTERACTORS);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getInteractorsTotal();
    }
}
