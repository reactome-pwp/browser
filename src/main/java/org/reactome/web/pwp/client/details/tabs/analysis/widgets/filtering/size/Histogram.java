package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size;

import com.google.gwt.canvas.dom.client.Context2d;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Histogram {
    private static final String colour = "#c5c6c6";
    private static final String highlightColour = "#02a0e3";

    private Point base;
    private double filterMin;
    private double filterMax;
    private List<Bin> bins;

    public Histogram(Point base, double filterMin, double filterMax, List<Bin> bins) {
        this.base = base;
        this.filterMin = filterMin;
        this.filterMax = filterMax;
        this.bins = bins;
    }

    public void setFilterMin(double filterMin) {
        this.filterMin = filterMin;
    }

    public void setFilterMax(double filterMax) {
        this.filterMax = filterMax;
    }


    public void draw(Context2d ctx) {
        ctx.save();
        ctx.setFillStyle(colour);
        for (Bin bin : bins) {
//            Console.info(bin.x() + " -> " + (bin.x() + bin.w()));
            ctx.setFillStyle(bin.x() >= filterMin && (bin.x() + bin.w() <= filterMax) ? highlightColour: colour);
            ctx.fillRect(base.x() + bin.x(), bin.y(), bin.w(), bin.h());
        }
        ctx.restore();
    }
}
