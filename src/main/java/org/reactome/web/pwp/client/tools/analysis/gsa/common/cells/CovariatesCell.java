package org.reactome.web.pwp.client.tools.analysis.gsa.common.cells;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.AnnotationProperty;

/**
 * A custom {@link Cell} with a checkbox used to render blocking factors (covariates)
 *
 */
public class CovariatesCell extends AbstractCell<AnnotationProperty> {

    interface Templates extends SafeHtmlTemplates {

        @Template("" +
                "<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis;\">" +
                    "<label style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; margin-top: 1px; line-height: 1em\">" +
                        "{0}" +
                    "</label>" +
                "</div>")
        SafeHtml minCell(SafeHtml primary);
    }

    private static Templates templates = GWT.create(Templates.class);

    public CovariatesCell() {
        super(BrowserEvents.CHANGE);
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, AnnotationProperty value, NativeEvent event, ValueUpdater<AnnotationProperty> valueUpdater) {
        String type = event.getType();
        if (BrowserEvents.CHANGE.equals(type)) {
            valueUpdater.update(value);
        }
    }

    @Override
    public void render(Context context, AnnotationProperty value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }


        SafeHtml primary = SafeHtmlUtils.fromTrustedString("<input type=\"checkbox\"" + (value.isChecked() ? "checked" : "") + " />"
                + value.getName());
        sb.append(templates.minCell(primary));
    }
}
