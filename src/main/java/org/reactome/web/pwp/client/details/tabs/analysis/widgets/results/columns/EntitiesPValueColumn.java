package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.analysis.client.model.PathwaySummary;

import static org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType.ENTITIES_PVALUE;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesPValueColumn extends AbstractColumn<Number> {

    private static final String explanation = "Probability that the overlap between the query and the pathway has occurred by chance";

    public EntitiesPValueColumn() {
        super(new NumberCell(NumberFormat.getFormat("#.##E0")), "Entities", "pValue", explanation);
        setSortingBy(ENTITIES_PVALUE);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if(object==null) return null;
        return object.getEntities().getpValue();
    }

}
