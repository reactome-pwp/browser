package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.pvalue;

import com.google.gwt.canvas.dom.client.Context2d;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.Point;

/** The bottom bar of the slider
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Axis {
    private static final String colour = "#898989";
    private static final String highlightColour = "#02a0e3";
    private static final int thickness = 4;

    private final Point base;

    private double min;
    private double max;
    private double filterMax;

    public Axis(Point base, double min, double max, double filterMax) {
        this.base = base;
        this.min = min;
        this.max = max;
        this.filterMax = filterMax;
    }

    public void setFilterMax(double filterMax) {
        this.filterMax = filterMax;
    }

    public void draw(Context2d ctx){
        int w = ctx.getCanvas().getWidth();
        ctx.save();
        ctx.setLineWidth(thickness);
        ctx.setStrokeStyle(colour);
        ctx.setFillStyle(colour);

        ctx.setFont("bold 12px Arial");
        ctx.setTextBaseline(Context2d.TextBaseline.MIDDLE);
        ctx.setTextAlign(Context2d.TextAlign.END);
        ctx.fillText(min + "", base.x() - 8, base.y());
        ctx.setTextAlign(Context2d.TextAlign.START);
        ctx.fillText(max + "", w - base.x() + 8, base.y());

        ctx.beginPath();
        ctx.moveTo(base.x(), base.y());
        ctx.lineTo(w - base.x(), base.y());
        ctx.stroke();

        ctx.setStrokeStyle(highlightColour);
        ctx.beginPath();
        ctx.moveTo(base.x(), base.y());
        ctx.lineTo(base.x() + filterMax, base.y());
        ctx.stroke();
        ctx.restore();
    }
}
