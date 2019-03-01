package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.RangeValueChangedHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class RangeValueChangedEvent extends GwtEvent<RangeValueChangedHandler> {
    public static Type<RangeValueChangedHandler> TYPE = new Type<>();

    private double min;
    private double max;

    public RangeValueChangedEvent(double min, double max){
        this.min = min;
        this.max = max;
    }

    @Override
    public Type<RangeValueChangedHandler> getAssociatedType(){
        return TYPE;
    }

    @Override
    protected void dispatch(RangeValueChangedHandler handler){
        handler.onRangeValueChanged(this);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}