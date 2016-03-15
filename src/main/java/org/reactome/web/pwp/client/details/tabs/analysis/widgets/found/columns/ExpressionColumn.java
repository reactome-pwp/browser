package org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.analysis.client.model.IdentifierSummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionColumn<T extends IdentifierSummary> extends AbstractColumn<T, Number> {

    private static final String explanation = "The submitted sample value associated with the identifier for the column named ";
    private Integer index;

    public ExpressionColumn(Integer index, String title) {
        super(new NumberCell(NumberFormat.getDecimalFormat()), "", title, explanation + title);
        this.index = index;
        setWidth(100);
    }

    @Override
    public Number getValue(T object) {
        if(object==null) return Double.NaN;
        Number number = object.getExp().get(index);
        if(number==null) return Double.NaN;
        return number;
    }
}
