package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import org.reactome.web.analysis.client.model.PathwaySummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SpeciesColumn extends AbstractColumn<String> {

    private static final String explanation = "The name of the species for the pathway";

    public SpeciesColumn() {
        super(new TextCell(), Style.TextAlign.LEFT, "", "Species name", explanation);
        setWidth(150);
        setHorizontalAlignment(ALIGN_LEFT);
    }

    @Override
    public String getValue(PathwaySummary object) {
        return object.getSpecies().getName();
    }
}
