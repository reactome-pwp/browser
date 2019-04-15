package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.reactome.web.analysis.client.model.PathwaySummary;

import static org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType.FOUND_INTERACTORS;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InteractorsFoundColumn extends AbstractColumn<String> {

    private static final String explanation = "The number of mapped identifiers that match to interactors for entities in the pathway for the selected molecular type";
    private static final String rowTitle = "Display matching (to interactors) submitted identifiers for the selected pathway";

    public InteractorsFoundColumn(FieldUpdater<PathwaySummary, String> fieldUpdater) {
        super(new ClickableTextCell(), "Interactors", "found", explanation);
        setWidth(76);
        setFieldUpdater(fieldUpdater);
        setSortingBy(FOUND_INTERACTORS);
    }

    @Override
    public String getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getInteractorsFound().toString();
    }

    @Override
    public void render(Cell.Context context, PathwaySummary object, SafeHtmlBuilder sb) {
        if(object.getEntities().getInteractorsFound() == 0){
            super.render(context, object, sb);
        } else {
            sb.appendHtmlConstant("<span style='cursor:pointer; text-decoration:underline' title='" + rowTitle + "'>" +
                    getValue(object) + "</span>");
        }
    }
}
