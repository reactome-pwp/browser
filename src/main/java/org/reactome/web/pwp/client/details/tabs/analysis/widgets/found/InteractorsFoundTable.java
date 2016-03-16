package org.reactome.web.pwp.client.details.tabs.analysis.widgets.found;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ProvidesKey;
import org.reactome.web.analysis.client.model.FoundInteractor;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InteractorsFoundTable extends DataGrid<FoundInteractor> {
    public final static Integer PAGE_SIZE = 40;

    public InteractorsFoundTable(List<String> resources, List<String> columnNames) {
        super(PAGE_SIZE, new ProvidesKey<FoundInteractor>() {
            @Override
            public Object getKey(FoundInteractor item) {
                return item == null ? null : item.getId();
            }
        });

        this.addColumns(resources, columnNames);
        this.setAutoHeaderRefreshDisabled(true);
        this.setWidth("100%");
        this.setVisible(true);
    }

    private void addColumns(List<String> resources, List<String> columnNames){
        List<AbstractColumn<FoundInteractor, ?>> columns = new LinkedList<>();

        for (String resource : resources) {
            if(resource.equals("TOTAL")) continue;
            columns.add(new InteractorResourceColumn("Submitted", "Identifiers"));
        }

        columns.add(new InteractorMapsToColumn("Maps", "to"));
        columns.add(new InteractsWithColumn("Interact", "with"));

        int i = 0;
        for (String columnName : columnNames) {
            columns.add(new ExpressionColumn(i++, columnName));
        }

        for (AbstractColumn<FoundInteractor, ?> column : columns) {
            this.addColumn(column, column.buildHeader());
            this.setColumnWidth(column, column.getWidth(), com.google.gwt.dom.client.Style.Unit.PX);
        }
    }
}
