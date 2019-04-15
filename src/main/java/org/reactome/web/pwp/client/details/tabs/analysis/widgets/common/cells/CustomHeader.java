package org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.cells;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Header;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CustomHeader extends Header<String> {
    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
//        @SafeHtmlTemplates.Template("<div style=\"{0}\" title=\"{1}\">{2}<br>{3}</div>")
        @Template("<div style=\"{0}\"><span style=\"cursor:help\" title=\"{1}\">{2}<br>{3}</span></div>")
        SafeHtml cell(SafeStyles styles, String title, SafeHtml group, SafeHtml name);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);

    private final String group;
    private final String name;
    private final String title;
    private final Style.TextAlign textAlign;

    /**
     * Construct a Header with a given {@link Cell}.
     *
     * @param cell the {@link Cell} responsible for rendering items in the header
     * @param group the group of the header
     * @param name the name of the header
     */
    public CustomHeader(Cell<String> cell, Style.TextAlign textAlign, String group, String name, String explanation) {
        super(cell);
        this.textAlign = textAlign;
        this.group = group;
        this.name = name;
        this.title = explanation;
    }

    @Override
    public String getValue() {
        return group + " " + name;
    }

    @Override
    public void render(Cell.Context context, SafeHtmlBuilder sb) {
        SafeHtml group = (new SafeHtmlBuilder()).appendEscaped(this.group).toSafeHtml();
        SafeHtml name = (new SafeHtmlBuilder()).appendEscaped(this.name).toSafeHtml();
        SafeStyles safeStyles = SafeStylesUtils.forTextAlign(this.textAlign);
        SafeHtml rendered = templates.cell(safeStyles, this.title, group, name);
        sb.append(rendered);
//        super.render(context, sb);
    }
}
