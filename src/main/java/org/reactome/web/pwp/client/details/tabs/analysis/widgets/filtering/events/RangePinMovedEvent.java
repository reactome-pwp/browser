package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.RangePinMovedHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class RangePinMovedEvent extends GwtEvent<RangePinMovedHandler> {
    public static Type<RangePinMovedHandler> TYPE = new Type<>();

    private double min;
    private double max;

    public RangePinMovedEvent(double min, double max){
        this.min = min;
        this.max = max;
    }

    @Override
    public Type<RangePinMovedHandler> getAssociatedType(){
        return TYPE;
    }

    @Override
    protected void dispatch(RangePinMovedHandler handler){
        handler.onRangePinMoved(this);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}