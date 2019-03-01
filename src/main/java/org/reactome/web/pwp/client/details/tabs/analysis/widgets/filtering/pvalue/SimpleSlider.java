package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.pvalue;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.RangePinMovedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.RangeValueChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.RangePinMovedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.RangeValueChangedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.Point;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.Thumb;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.ThumbStatus;


/**
 * A simple slider tool allowing the user to
 * select a value between a min and a max.
 *
 * Fires a RangePinMovedEvent when a pin has a new position
 * and a RangeValueChangedEvent when the pin has settled in a
 * new position.
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class SimpleSlider extends Composite implements HasHandlers,
        MouseMoveHandler, MouseDownHandler, MouseOutHandler, MouseUpHandler {
    private static NumberFormat formatter = NumberFormat.getFormat("#.00");
    private Point base;

    private Canvas canvas;
    private int width;
    private int height;
    private double min;
    private double max;
    private double value;

    private double previousValue;
    private boolean pinMoved;

    private Axis axis;
    private Thumb thumb;

    public SimpleSlider(int width, int height, double min, double max, double value) {
        this.width = width;
        this.height = height;
        base = new Point(30, height - 20);

        if (min >= max) throw new RuntimeException("Min value in SimpleSlider has to be always lower than max.");
        this.min = min;
        this.max = max;

        this.value = value > max && value < min ? max : value;

        initUI();
        initHandlers();
        draw();
    }

    public HandlerRegistration addRangeValueChangedHandler(RangeValueChangedHandler handler){
        return addHandler(handler, RangeValueChangedEvent.TYPE);
    }

    public HandlerRegistration addRangePinMovedHandler(RangePinMovedHandler handler) {
        return addHandler(handler, RangePinMovedEvent.TYPE);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        Point point = getMousePosition(event);
        thumb.setStatus(thumb.contains(point) ? ThumbStatus.CLICKED : ThumbStatus.NORMAL);
        draw();

        if (thumb.getStatus() == ThumbStatus.CLICKED) {
            previousValue = value;
            pinMoved = false;
        }
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        Point point = getMousePosition(event);
        int pos = point.x() - base.x();
        if (thumb.getStatus() == ThumbStatus.CLICKED) {
            double newPosition = pos > (width - 2 * base.x()) ? (width - 2 * base.x()) : pos;
            newPosition = newPosition < 0 ? 0 : newPosition;
            value = translatePointOnAxisToValue(newPosition);
            thumb.setPosition(newPosition, formatter.format(value));
            axis.setFilterMax(newPosition);
            pinMoved = true;
            fireEvent(new RangePinMovedEvent(0, value));
        } else {
            boolean isHovered = thumb.contains(point);
            thumb.setStatus(isHovered ? ThumbStatus.HOVERED : ThumbStatus.NORMAL);
            getElement().getStyle().setCursor(isHovered ? Style.Cursor.POINTER : Style.Cursor.DEFAULT);
        }
        draw();
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        Point point = getMousePosition(event);
        thumb.setStatus(thumb.contains(point) ? ThumbStatus.HOVERED : ThumbStatus.NORMAL);
        draw();

        if (value != previousValue && pinMoved) {
            pinMoved = false;
            fireEvent(new RangeValueChangedEvent(0, value));
        }
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        thumb.setStatus(ThumbStatus.NORMAL);
        draw();

        if (value != previousValue && pinMoved) {
            pinMoved = false;
            fireEvent(new RangeValueChangedEvent(0, value));
        }
    }

    public void setValue(double value) {
        this.value = value;
        double tValue = translateValueToPointOnAxis(this.value);
        thumb.setPosition(tValue, formatter.format(this.value));
        axis.setFilterMax(tValue);
        draw();
    }

    private void initUI() {
        canvas = Canvas.createIfSupported();
        if (canvas !=null) {
            canvas.setWidth(width + "px");
            canvas.setHeight(height + "px");
            canvas.setCoordinateSpaceWidth(width);
            canvas.setCoordinateSpaceHeight(height);
            canvas.setStyleName(RESOURCES.getCSS().simpleSlider());
            FlowPanel main = new FlowPanel();
            main.add(canvas);

            initWidget(main);
            initialize();
        }
    }

    private void initHandlers() {
        canvas.addMouseDownHandler(this);
        canvas.addMouseMoveHandler(this);
        canvas.addMouseOutHandler(this);
        canvas.addMouseUpHandler(this);
    }

    private void initialize() {
        double tValue = translateValueToPointOnAxis(value);
        axis = new Axis(base, min, max, tValue);
        thumb = new Thumb(base, tValue, formatter.format(value));
        thumb.setFont("bold 11px Arial");

    }

    private double translateValueToPointOnAxis(double value) {
        double p = ((width - 2 * base.x()) * (value - min) / (double)(max - min));
        return Math.round(p);
    }

    private double translatePointOnAxisToValue(double point) {
        return ((max - min) * point / (double)(width - 2 * base.x())) + min;
    }

    private void draw(){
        Context2d ctx = canvas.getContext2d();
        ctx.clearRect(0, 0, width, height);
        axis.draw(ctx);
        thumb.draw(ctx);
    }

    private Point getMousePosition(MouseEvent event){
        int x = event.getRelativeX(this.canvas.getElement());
        int y = event.getRelativeY(this.canvas.getElement());
        return new Point(x,y);
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    @CssResource.ImportedWithPrefix("pwp-SimpleSlider")
    public interface ResourceCSS extends CssResource {
        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/filtering/pvalue/SimpleSlider.css";

        String simpleSlider();

    }
}
