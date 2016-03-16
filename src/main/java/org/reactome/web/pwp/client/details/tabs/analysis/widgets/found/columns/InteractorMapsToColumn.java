package org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import org.reactome.web.analysis.client.model.FoundInteractor;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InteractorMapsToColumn extends AbstractColumn<FoundInteractor, String> {

    private static final String explanation = "Entities interacting with the submitted identifier";

    public InteractorMapsToColumn(String group, String title) {
        super(new TextCell(), Style.TextAlign.LEFT, group, title, explanation);
        setHorizontalAlignment(ALIGN_LEFT);
    }

    @Override
    public String getValue(FoundInteractor object) {
        StringBuilder sb = new StringBuilder();
        for (String s : object.getMapsTo()) {
            sb.append(s).append(", ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }
}
