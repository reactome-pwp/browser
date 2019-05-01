package org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.cells;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class CustomSignCell extends NumberCell {

    private enum SignLabel {
        SIG_DOWN    (-2, "\u25BC\u25BC",    "#D94452"),
        DOWN        (-1, "\u25BC",          "#EC5564"),
        NOT_FOUND   ( 0, "-",               "#A9B1BC"),
        UP          ( 1, "\u25B2",          "#9ED36A"),
        SIG_UP      ( 2, "\u25B2\u25B2",    "#8AC054");

        int value;
        String symbol;
        String color;

        SignLabel(int value, String symbol, String color) {
            this.value = value;
            this.symbol = symbol;
            this.color = color;
        }

        public static SignLabel getByValue(int value) {
            for (SignLabel sign : SignLabel.values()) {
                if (sign.value == value)
                    return sign;
            }
            return null;
        }
    }


    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
        /**
         * The template for this Cell, which includes styles and a value.
         *
         * @param style the styles to include in the style attribute of the div
         * @param value  the safe value. Since the value type is {@link SafeHtml},
         *               it will not be escaped before including it in the template.
         *               Alternatively, you could make the value type String, in which
         *               case the value would be escaped.
         * @return a {@link SafeHtml} instance
         */
        @Template("<div style=\"{0}\">{1}</div>")
        SafeHtml cell(SafeStyles style, String value);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);


    public CustomSignCell() {
    }

    @Override
    public void render(Context context, Number value, SafeHtmlBuilder sb) {
        /*
        * Always do a null check on the value. Cell widgets can pass null to
        * cells if the underlying data contains a null, or if the data arrives
        * out of order.
        */
        if (value == null) {
            return;
        }
        SignLabel sign = SignLabel.getByValue(value.intValue());

        if(sign != null) {
            SafeStylesBuilder style = new SafeStylesBuilder();
            style.append(SafeStylesUtils.forFontSize(2, Style.Unit.EM))
                 .append(SafeStylesUtils.forLineHeight(0.1, Style.Unit.EM));
//                 .append(SafeStylesUtils.forTrustedColor(sign.color));

            SafeHtml rendered = templates.cell(style.toSafeStyles(), sign.symbol);
            sb.append(rendered);
        }
    }
}

