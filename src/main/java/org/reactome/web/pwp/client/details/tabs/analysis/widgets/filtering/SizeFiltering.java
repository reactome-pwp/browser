package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.Bin;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.RangePinMovedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.RangeValueChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.RangePinMovedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.RangeValueChangedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size.RangeSlider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class SizeFiltering extends FlowPanel implements FilteringWidget,
        RangeValueChangedHandler, RangePinMovedHandler {
    private Handler handler;
    private FilteringPanel.Resources style;

    private Label sizeFilterLb;
    private RangeSlider sizeSlider;

    private final static int binSize = 200;
    private final static int histWidth = 300;
    private final static int histHeight = 70;

    private int min, max;
    private double filterMin, filterMax;

    private List<Integer> histogram;

    public SizeFiltering(Handler handler) {
        this.handler = handler;
        style = FilteringPanel.RESOURCES;
    }

    @Override
    public Widget initUI() {
        sizeFilterLb = new Label();
        sizeFilterLb.setStyleName(style.getCSS().sizeFilterLb());
        add(sizeFilterLb);

        Label title = new Label("\u2022 By pathway size");
        title.setStyleName(style.getCSS().innerTitle());
        add(title);

        Label subtitle = new Label("Show pathways with size:");
        subtitle.setStyleName(style.getCSS().compactSubtitle());
        add(subtitle);

        updateHistogram();
        return this;
    }

    @Override
    public void updateUI() {
        updateHistogram();
    }

    @Override
    public void onRangePinMoved(RangePinMovedEvent event) {
        filterMin = event.getMin();
        filterMax = event.getMax();
        updateLabel();
    }

    @Override
    public void onRangeValueChanged(RangeValueChangedEvent event) {
        filterMin = event.getMin();
        filterMax = event.getMax();
        updateLabel();

        handler.onSizeChanged(min, max, (int) filterMin, (int) filterMax);
        handler.loadAnalysisData();
    }


    private void updateLabel() {
        sizeFilterLb.setText(filterMin == min && filterMax == max ? "All entities" : (int) filterMin + " - " + (int) filterMax + " entities");
    }

    private void updateHistogram() {
        this.histogram = new ArrayList<>();
        AnalysisResult result = handler.getAnalysisResult();
        Filter filter = handler.getFilter();

        AnalysisClient.getPathwaysBinnedBySize(result.getSummary().getToken(), binSize, filter.getResultFilter(), new AnalysisHandler.PathwaysBinned() {
            @Override
            public void onPathwaysBinnedLoaded(List<Bin> pathwaysBinned) {
                if (pathwaysBinned != null && !pathwaysBinned.isEmpty()) {
                    Bin lastBin = pathwaysBinned.get(pathwaysBinned.size() - 1);
                    Integer[] binValues = new Integer[lastBin.getKey() + 1];
                    for (Bin bin : pathwaysBinned) {
                        binValues[bin.getKey()] =  bin.getValue();
                    }
                    histogram = Arrays.stream(binValues).map(i -> i == null ? 0 : i).collect(Collectors.toList());

                    min = 0;
                    max = histogram.size() * binSize;

                    if (!filter.getAppliedFilters().contains(Filter.Type.BY_SIZE)) {
                        filterMin = min;
                        filterMax = max;
                        handler.onSizeChanged(min, max, (int) filterMin, (int) filterMax);
                    } else {
                        filterMin = filter.getSizeMin() < min || filter.getSizeMin() > max ? min : filter.getSizeMin();
                        filterMax = filter.getSizeMax() > max ? max : filter.getSizeMax();

                        handler.onSizeChanged(min, max, (int) filterMin, (int) filterMax);
                    }

                    if (sizeSlider != null) sizeSlider.removeFromParent();

                    sizeSlider = new RangeSlider(histWidth, histHeight, min, max, filterMin, filterMax, histogram);
                    sizeSlider.addRangeValueChangedHandler(SizeFiltering.this);
                    sizeSlider.addRangePinMovedHandler(SizeFiltering.this);
                    add(sizeSlider);
                }
            }

            @Override
            public void onPathwaysBinnedError(AnalysisError error) {
                Console.error(error.getReason());
            }

            @Override
            public void onAnalysisServerException(String message) {
                Console.error(message);
            }
        });
    }
}
