package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DiseaseFiltering extends FlowPanel implements FilteringWidget, ValueChangeHandler<Boolean> {
    private Handler handler;
    private FilteringPanel.Resources style;

    private boolean includeDisease;
    private CheckBox includeDiseaseCB;

    public DiseaseFiltering(Handler handler) {
        this.handler = handler;
        style = FilteringPanel.RESOURCES;
    }

    @Override
    public Widget initUI() {
        Label title = new Label("\u2022 By disease");
        title.setStyleName(style.getCSS().compactInnerTitle());
        add(title);

        Label subtitle = new Label("Show/hide disease pathways");
        subtitle.setStyleName(style.getCSS().compactSubtitle());
        add(subtitle);

        populate();

        includeDiseaseCB = new CheckBox("Include disease pathway in the results");
        includeDiseaseCB.setStyleName(style.getCSS().checkBox());
        includeDiseaseCB.setValue(includeDisease);
        includeDiseaseCB.addValueChangeHandler(this);
        add(includeDiseaseCB);

        return this;
    }

    @Override
    public void updateUI() {
        populate();
        includeDiseaseCB.setValue(includeDisease, false);
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        includeDisease = includeDiseaseCB.getValue();
        handler.onIncludeDiseaseChanged(includeDisease);
        handler.loadAnalysisData();
    }

    private void populate() {
        includeDisease = handler.getFilter().getIncludeDisease();
    }

}
