package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import org.reactome.web.analysis.client.model.PathwaySummary;

import static org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType.NAME;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PathwayNameColumn extends AbstractColumn<String> {

    private static final String explanation = "The name of the pathway";

    public PathwayNameColumn() {
        super(new TextCell(), Style.TextAlign.LEFT, "", "Pathway name", explanation);
        setDataStoreName(COLUMN_NAME_TITLE);
        setWidth(400);
        setHorizontalAlignment(ALIGN_LEFT);
        setSortingBy(NAME);
    }

    @Override
    public String getValue(PathwaySummary object) {
        return object!=null?object.getName():"";
    }

}