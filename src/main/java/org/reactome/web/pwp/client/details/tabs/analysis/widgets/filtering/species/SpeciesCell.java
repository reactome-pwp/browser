package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * A custom {@link Cell} used to render the autocomplete items
 *
 */
public class SpeciesCell extends AbstractCell<Species> {

    interface Templates extends SafeHtmlTemplates {

        @Template("" +
                "<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis;\">" +
                    "<label style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; font-size:small; margin-top: 1px; line-height: 1em\">" +
                        "{0}" +
                    "</label>" +
                "</div>")
        SafeHtml minCell(SafeHtml primary);
    }

    private static Templates templates = GWT.create(Templates.class);
    private static NumberFormat formatter = NumberFormat.getFormat("#,###");

    public SpeciesCell() {
        super(BrowserEvents.CHANGE);
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, Species value, NativeEvent event, ValueUpdater<Species> valueUpdater) {
        String type = event.getType();
        if (BrowserEvents.CHANGE.equals(type)) {
            valueUpdater.update(value);
        }
    }

    @Override
    public void render(Context context, Species value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        double pathways = value.getSpeciesSummary().getPathways();
        double filtered = value.getSpeciesSummary().getFiltered();

        String number = " (" + (pathways == filtered ? formatter.format(pathways) : formatter.format(filtered) + "/" + formatter.format(pathways)) + ")";

        SafeHtml primary = SafeHtmlUtils.fromTrustedString("<input type=\"checkbox\"" + (value.isChecked() ? "checked" : "") + " />"
                + value.getName() + number);
        sb.append(templates.minCell(primary));
    }
}
