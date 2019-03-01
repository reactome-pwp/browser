package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.RangePinMovedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.RangeValueChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.RangePinMovedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.RangeValueChangedHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * A slider tool with two pins allowing the user to
 * select a min and a max value.
 *
 * Fires a RangePinMovedEvent when a pin has a new position
 * and a RangeValueChangedEvent when the pin has settled in a
 * new position
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class RangeSlider extends Composite implements HasHandlers,
        MouseMoveHandler, MouseDownHandler, MouseOutHandler, MouseUpHandler {
    private Point base;

    private Canvas canvas;
    private int width;
    private int height;
    private int min;
    private int max;
    private double filterMin;
    private double filterMax;
    private List<Bin> bins;

    private Axis axis;
    private Histogram histogram;
    private Thumb minThumb;
    private Thumb maxThumb;

    private double previousMin;
    private double previousMax;
    private boolean pinMoved;

    public RangeSlider(int width, int height, int min, int max, double filterMin, double filterMax, List<Integer> histValues) {
        this.width = width;
        this.height = height;
        base = new Point(35, height - 20);

        update(min, max,filterMin, filterMax, histValues);
    }

    public void update(int min, int max, double filterMin, double filterMax, List<Integer> histValues) {
        if (min >= max) throw new RuntimeException("Min value in RangeSlider has to be always lower than max.");
        this.min = min;
        this.max = max;

        this.filterMin = filterMin < min ? min : filterMin;
        this.filterMax = filterMax > max ? max : filterMax;

        if (histValues == null) throw new RuntimeException("Histogram values is null");

        this.bins = createListOfBins(histValues);
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
        minThumb.setStatus(minThumb.contains(point) ? ThumbStatus.CLICKED : ThumbStatus.NORMAL);
        maxThumb.setStatus(maxThumb.contains(point) ? ThumbStatus.CLICKED : ThumbStatus.NORMAL);
        draw();

        if (minThumb.getStatus() == ThumbStatus.CLICKED || maxThumb.getStatus() == ThumbStatus.CLICKED) {
            previousMin = filterMin;
            previousMax = filterMax;
            pinMoved = false;
        }
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        Point point = getMousePosition(event);
        int pos = point.x() - base.x();
        boolean isAnyThumbHovered;
        if (minThumb.getStatus() == ThumbStatus.CLICKED) {
            isAnyThumbHovered = true;
            double newPosition = pos < 0 ? 0 : pos;
            double limit = maxThumb.getPosition() - (int) (2 * Thumb.radius + Thumb.thickness);
            newPosition = newPosition < limit ? newPosition : limit;
            filterMin = translatePointOnAxisToValue(newPosition);
            minThumb.setPosition(newPosition, filterMin + "");
            axis.setFilterMin(newPosition);
            histogram.setFilterMin(newPosition);
            pinMoved = true;
            fireEvent(new RangePinMovedEvent(filterMin, filterMax));
        } else {
            boolean isHovered = minThumb.contains(point);
            isAnyThumbHovered = isHovered;
            minThumb.setStatus(isHovered ? ThumbStatus.HOVERED : ThumbStatus.NORMAL);
        }

        if (maxThumb.getStatus() == ThumbStatus.CLICKED) {
            isAnyThumbHovered = true;
            double newPosition = pos > (width - 2 * base.x()) ? (width - 2 * base.x()) : pos;
            double limit = minThumb.getPosition() + (int) (2 * Thumb.radius + Thumb.thickness);
            newPosition = newPosition > limit ? newPosition : limit;
            filterMax = translatePointOnAxisToValue(newPosition);
            maxThumb.setPosition(newPosition, filterMax + "");
            axis.setFilterMax(newPosition);
            histogram.setFilterMax(newPosition);
            pinMoved = true;
            fireEvent(new RangePinMovedEvent(filterMin, filterMax));
        } else {
            boolean isHovered = maxThumb.contains(point);
            isAnyThumbHovered = isAnyThumbHovered || isHovered;
            maxThumb.setStatus(isHovered ? ThumbStatus.HOVERED : ThumbStatus.NORMAL);
        }
        getElement().getStyle().setCursor(isAnyThumbHovered ? Style.Cursor.POINTER : Style.Cursor.DEFAULT);
        draw();
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        Point point = getMousePosition(event);
        minThumb.setStatus(minThumb.contains(point) ? ThumbStatus.HOVERED : ThumbStatus.NORMAL);
        maxThumb.setStatus(maxThumb.contains(point) ? ThumbStatus.HOVERED : ThumbStatus.NORMAL);
        draw();

        if ((filterMin != previousMin || filterMax != previousMax) && pinMoved) {
            pinMoved = false;
            fireEvent(new RangeValueChangedEvent(filterMin, filterMax));
        }
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        minThumb.setStatus(ThumbStatus.NORMAL);
        maxThumb.setStatus(ThumbStatus.NORMAL);
        draw();

        if ((filterMin != previousMin || filterMax != previousMax) && pinMoved) {
            pinMoved = false;
            fireEvent(new RangeValueChangedEvent(filterMin, filterMax));
        }
    }

    private void initUI() {
        canvas = Canvas.createIfSupported();
        if (canvas !=null) {
            canvas.setWidth(width + "px");
            canvas.setHeight(height + "px");
            canvas.setCoordinateSpaceWidth(width);
            canvas.setCoordinateSpaceHeight(height);
            canvas.setStyleName(RESOURCES.getCSS().rangeSlider());
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
        double tFilterMin = translateValueToPointOnAxis(filterMin);
        double tFilterMax = translateValueToPointOnAxis(filterMax);
        axis = new Axis(base, min, max, tFilterMin, tFilterMax);
        minThumb = new Thumb(base, tFilterMin, filterMin + "");
        maxThumb = new Thumb(base, tFilterMax, filterMax + "");
        histogram = new Histogram(base, tFilterMin, tFilterMax, bins);

    }

    private double translateValueToPointOnAxis(double value) {
        double p = ((width - 2 * base.x()) * (value - min) / (double)(max - min));
        return Math.round(p);
    }

    private double translatePointOnAxisToValue(double point) {
        double value = ((max - min) * point / (double)(width - 2 * base.x())) + min;
        return Math.round(value);
    }

    private List<Bin> createListOfBins(List<Integer> histValues) {
        List<Bin> bins =  new ArrayList<>();

        if(histValues != null && !histValues.isEmpty() ) {
            Integer maxAvailableHeight = base.y() - 10;
            Integer maxValue = histValues.stream().mapToInt(v -> v).max().orElse(1);
            Integer binSize = (max - min) / histValues.size();
            Integer pixelWidth = (width - 2 * base.x()) / histValues.size();

            for (int i = 0; i <histValues.size(); i++) {
                Double nValue = ((maxAvailableHeight - 2) * histValues.get(i) / (double) maxValue);
                if (nValue > 0 && nValue < 1) {
                    nValue = 1d;
                }
                int normalisedValue = nValue.intValue();
                int start = i * binSize;
                Bin bin  = new Bin(
                        i,
                        histValues.get(i),
                        normalisedValue,
                        start,
                        start + binSize - 1,
                        i * pixelWidth,
                        maxAvailableHeight - normalisedValue,
                        pixelWidth,
                        normalisedValue);
                bins.add(bin);
            }
        }

        return bins;
    }

    private void draw(){
        Context2d ctx = canvas.getContext2d();
        ctx.clearRect(0, 0, width, height);
        axis.draw(ctx);
        histogram.draw(ctx);
        minThumb.draw(ctx);
        maxThumb.draw(ctx);
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

    @CssResource.ImportedWithPrefix("pwp-RangeSlider")
    public interface ResourceCSS extends CssResource {
        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/filtering/size/RangeSlider.css";

        String rangeSlider();

    }
}
