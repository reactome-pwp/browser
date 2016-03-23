package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.reactome.web.analysis.client.model.PathwaySummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesFoundColumn extends AbstractColumn<String> {

    private String rowTitle;
    private boolean clickable;

    public EntitiesFoundColumn(FieldUpdater<PathwaySummary, String> fieldUpdater) {
        super(new ClickableTextCell(),
                "Entities", "found",
                "The number of mapped identifiers that match the pathway for the selected molecular type");
        setWidth(65);
        setFieldUpdater(fieldUpdater);
        this.clickable = true;
        rowTitle = "Display matching submitted identifiers for the selected pathway";
    }

    public EntitiesFoundColumn() {
        super(new TextCell(),
                "Entities", "found",
                "The number of mapped identifiers aggregating curated and interactors that match the pathway for the selected molecular type");
        setWidth(65);
        this.clickable = false;
    }

    @Override
    public String getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getFound().toString();
    }

    @Override
    public void render(Cell.Context context, PathwaySummary object, SafeHtmlBuilder sb) {
        if (clickable) {
            sb.appendHtmlConstant("<span style='cursor:pointer; text-decoration:underline' title='" + this.rowTitle + "'>" +
                    getValue(object) + "</span>");
        } else {
            super.render(context, object, sb);
        }
    }
}
