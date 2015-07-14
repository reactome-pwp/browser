package org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.columns;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.pwp.client.common.analysis.model.IdentifierSummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionColumn extends AbstractColumn<Number> {

    private static final String explanation = "The sample value submitted for the not found identifier for the column named ";
    private Integer index;

    public ExpressionColumn(Integer index, String title) {
        super(new NumberCell(NumberFormat.getDecimalFormat()), "", title, explanation + title);
        this.index = index;
        setWidth(100);
    }

    @Override
    public Number getValue(IdentifierSummary object) {
        if(object==null) return Double.NaN;
        Number number = object.getExp().get(index);
        if(number==null) return Double.NaN;
        return number;
    }
}
