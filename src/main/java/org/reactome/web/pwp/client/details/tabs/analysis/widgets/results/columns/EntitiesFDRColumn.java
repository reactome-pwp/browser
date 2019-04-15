package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.analysis.client.model.PathwaySummary;

import static org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType.ENTITIES_FDR;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesFDRColumn extends AbstractColumn<Number> {

    private static final String explanation = "Probability corrected for multiple comparisons";

    public EntitiesFDRColumn() {
        super(new NumberCell(NumberFormat.getFormat("#.##E0")), "Entities", "FDR", explanation);
        setSortingBy(ENTITIES_FDR);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getFdr();
    }
}
