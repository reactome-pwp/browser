package org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ProvidesKey;
import org.reactome.web.analysis.client.model.IdentifierSummary;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.columns.AbstractColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.columns.ExpressionColumn;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.notfound.columns.NotFoundColumn;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NotFoundTable extends DataGrid<IdentifierSummary> {
    public final static Integer PAGE_SIZE = 40;

    public NotFoundTable(List<String> columnNames) {
        super(PAGE_SIZE, new ProvidesKey<IdentifierSummary>() {
            @Override
            public Object getKey(IdentifierSummary item) {
                return item == null ? null : item.getId();
            }
        });

        List<AbstractColumn<?>> columns = new LinkedList<AbstractColumn<?>>();

        columns.add(new NotFoundColumn());

        int i = 0;
        for (String columnName : columnNames) {
            columns.add(new ExpressionColumn(i++, columnName));
        }

        for (AbstractColumn<?> column : columns) {
            this.addColumn(column, column.buildHeader());
            this.setColumnWidth(column, column.getWidth(), com.google.gwt.dom.client.Style.Unit.PX);
        }

        this.setAutoHeaderRefreshDisabled(true);
        this.setWidth("100%");
        this.setVisible(true);
    }
}
