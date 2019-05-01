package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.cells.CustomSignCell;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class RegulationSignColumn extends AbstractColumn<Number> {

    private static final String explanation = "The average sample value for identifiers that match the pathway for the selected molecular type for the column named ";
    private Integer index;

    public RegulationSignColumn(Integer index, String title) {
        super(new CustomSignCell(), "", title, explanation + title);
        this.index = index;
        setWidth(100);
        setHorizontalAlignment(ALIGN_CENTER);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Number getValue(PathwaySummary object) {
        if(object==null) return Double.NaN;
        Number number = object.getEntities().getExp().get(index);
        if(number==null) return Double.NaN;
        return number;
    }
}
