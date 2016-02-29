package org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.columns;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.dom.client.Style;
import org.reactome.web.analysis.client.model.IdentifierSummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NotFoundColumn extends AbstractColumn<String> {

    private static final String explanation = "The entities in the sample that have not beef found in Reactome";

    public NotFoundColumn() {
        super(new ClickableTextCell(), Style.TextAlign.LEFT, "Entities", "not found", explanation);
        setWidth(65);
        this.setHorizontalAlignment(ALIGN_LEFT);
    }

    @Override
    public String getValue(IdentifierSummary object) {
        if (object == null) return null;
        return object.getId();
    }

}
