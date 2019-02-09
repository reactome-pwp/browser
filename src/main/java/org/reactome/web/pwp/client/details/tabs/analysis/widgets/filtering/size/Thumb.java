package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Thumb {
    private static final String font = "bold 12px Arial";

    public static final double radius = 5;
    public static final int thickness = 3;
    private static final double sqrRadius = Math.pow(radius + 2, 2d);

    private final Point base;
    private double position;
    private String label;
    private ThumbStatus status;

    public Thumb(Point base, double position, String label) {
        this.base = base;
        this.position = position;
        this.label = label;
        status = ThumbStatus.NORMAL;
    }

    public ThumbStatus getStatus() {
        return status;
    }

    public void setStatus(ThumbStatus status) {
        this.status = status;
    }

    public boolean contains(Point p) {
        return Math.pow(base.x() + position - p.x(), 2) + Math.pow(base.y() - p.y(), 2) <= sqrRadius;
    }

    public void setPosition(double newPosition, String label) {
        this.position = newPosition;
        this.label = label;
    }

    public double getPosition() {
        return position;
    }

    public void draw(Context2d ctx) {
        ctx.save();

        ctx.setLineWidth(thickness);
        ctx.setStrokeStyle(status.colour);
        ctx.setFillStyle("#FFFFFF");

        ctx.beginPath();
        ctx.arc(base.x() + position, base.y(), radius, 0,  2 * Math.PI);
        ctx.fill();
        ctx.stroke();

        ctx.setFont(font);
        ctx.setFillStyle(status.colour);
        ctx.setTextBaseline(Context2d.TextBaseline.TOP);
        ctx.setTextAlign(Context2d.TextAlign.CENTER);
        ctx.fillText(label, base.x() + position, base.y() + 10);

        ctx.restore();
    }
}
