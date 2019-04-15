package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.dom.client.Style;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.cells.CustomNumberCell;

import static org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType.TOTAL_REACTIONS;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReactionsTotalColumn extends AbstractColumn<Number> {

    private static final String explanation = "The total number of reactions in the pathway for the selected molecular type";

    public ReactionsTotalColumn() {
        super(new CustomNumberCell(Style.FontStyle.ITALIC), "Reactions", "total", explanation);
        setWidth(85);
        setSortingBy(TOTAL_REACTIONS);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if(object==null) return null;
        return object.getReactions().getTotal();
    }
}
