package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.reactome.web.analysis.client.model.PathwaySummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesFoundColumn extends AbstractColumn<String> {

    private static final String explanation = "The number of mapped identifiers that match the pathway for the selected molecular type";
    private static final String rowTitle = "Display matching submitted identifiers for the selected pathway";

    public EntitiesFoundColumn(FieldUpdater<PathwaySummary, String> fieldUpdater) {
        super(new ClickableTextCell(), "Entities", "found", explanation);
        setWidth(65);
        setFieldUpdater(fieldUpdater);
    }

    @Override
    public String getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getFound().toString();
    }

    @Override
    public void render(Cell.Context context, PathwaySummary object, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<span style='cursor:pointer; text-decoration:underline' title='" + this.rowTitle + "'>" +
                getValue(object) + "</span>");
    }
}
