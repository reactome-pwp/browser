package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.dom.client.Style;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.cells.CustomNumberCell;

import static org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType.FOUND_REACTIONS;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReactionsFoundColumn extends AbstractColumn<Number> {

    private static final String explanation = "The number of reactions in this pathway that contain at least one molecule from the query set for the selected molecular type";

    public ReactionsFoundColumn() {
        super(new CustomNumberCell(Style.FontStyle.ITALIC), "Reactions", "found", explanation);
        setWidth(85);
        setSortingBy(FOUND_REACTIONS);
    }

    @Override
    public Integer getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getReactions().getFound();
    }
}
