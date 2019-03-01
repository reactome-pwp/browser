package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.RangePinMovedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.RangeValueChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.RangePinMovedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.RangeValueChangedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.pvalue.SimpleSlider;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class PValueFiltering extends FlowPanel implements FilteringWidget,
        RangeValueChangedHandler, RangePinMovedHandler {
    private static NumberFormat pValueFormatter = NumberFormat.getFormat("0.00");

    private Handler handler;
    private FilteringPanel.Resources style;

    private Label pValueFilterLb;
    private SimpleSlider pValueSlider;

    private double pValue;

    public PValueFiltering(Handler handler) {
        this.handler = handler;
        style = FilteringPanel.RESOURCES;
    }

    @Override
    public Widget initUI() {
        setStyleName(style.getCSS().byPValuePanel());

        pValueFilterLb = new Label();
        pValueFilterLb.setStyleName(style.getCSS().pValueFilterLb());
        add(pValueFilterLb);

        Label title = new Label("\u2022 By p-value");
        title.setStyleName(style.getCSS().innerTitle());
        add(title);

        Label subtitle = new Label("Filter by statistical significance:");
        subtitle.setStyleName(style.getCSS().compactSubtitle());
        add(subtitle);

        populatePValue();

        pValueSlider = new SimpleSlider(280, 27, 0, 1, pValue);
        pValueSlider.addRangeValueChangedHandler(this);
        pValueSlider.addRangePinMovedHandler(this);
        add(pValueSlider);

        updateLabel();
        return this;
    }

    @Override
    public void updateUI() {
        updateSlider();
        updateLabel();
    }

    @Override
    public void onRangePinMoved(RangePinMovedEvent event) {
        pValue = event.getMax();
        updateLabel();
    }

    @Override
    public void onRangeValueChanged(RangeValueChangedEvent event) {
        pValue = event.getMax();
        updateLabel();

        String rounded = pValueFormatter.format(pValue);
        handler.onPValueChanged(Double.parseDouble(rounded));
        handler.loadAnalysisData();
    }

    private void updateLabel() {
        String rounded = pValueFormatter.format(pValue);
        pValueFilterLb.setText("p ≤ " + rounded);
    }

    private void updateSlider() {
        populatePValue();
        pValueSlider.setValue(pValue);
    }

    private void populatePValue() {
        Filter filter = handler.getFilter();
        pValue = filter.getpValue() == null ? 1d : filter.getpValue();
    }

}
