package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.cells.CustomHeader;

import static org.reactome.web.pwp.client.details.tabs.analysis.providers.AnalysisAsyncDataProvider.SortingType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class AbstractColumn<T> extends Column<PathwaySummary, T> {

    protected final String COLUMN_NAME_TITLE;
    protected final String COLUMN_GROUP;
    protected final String EXPLANATION;
    protected final Style.TextAlign HEADER_ALIGN;
    protected SortingType sortingBy = SortingType.ENTITIES_PVALUE;

    protected Integer width = 90;

    public AbstractColumn(Cell<T> cell, String group, String title, String explanation) {
        this(cell, Style.TextAlign.CENTER, group, title, explanation);
    }

    public AbstractColumn(Cell<T> cell, Style.TextAlign headerAlign, String group, String title, String explanation) {
        super(cell);

        setDataStoreName(title);
        COLUMN_NAME_TITLE = title;
        COLUMN_GROUP = group;
        HEADER_ALIGN = headerAlign;
        EXPLANATION = explanation;
        this.setHorizontalAlignment(ALIGN_RIGHT);
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public SortingType getSortingBy() {
        return sortingBy;
    }

    public void setSortingBy(SortingType sortingBy) {
        this.sortingBy = sortingBy;
    }

    public final Header buildHeader() {
        return new CustomHeader(new ClickableTextCell(), HEADER_ALIGN, COLUMN_GROUP, COLUMN_NAME_TITLE, EXPLANATION);
    }

}
