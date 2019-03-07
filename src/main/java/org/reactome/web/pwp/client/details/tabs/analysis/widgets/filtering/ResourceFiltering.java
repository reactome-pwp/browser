package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.ResourceSummary;

import java.util.List;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ResourceFiltering extends FlowPanel implements FilteringWidget {
    private static NumberFormat resourceFormatter = NumberFormat.getFormat("#,###");

    private Handler handler;
    private FilteringPanel.Resources style;
    private ListBox resourceBox;
    private String selectedResource;


    public ResourceFiltering(Handler handler) {
        this.handler = handler;
        style = FilteringPanel.RESOURCES;
    }

    @Override
    public Widget initUI() {
        clear();
        add(getResourceTypePanel());

        Label resourceTitle = new Label("\u2022 By resource");
        resourceTitle.setStyleName(style.getCSS().innerTitle());
        add(resourceTitle);

        Label resourceSubtitle = new Label("Show pathways with size:");
        resourceSubtitle.setStyleName(style.getCSS().compactSubtitle());
        add(resourceSubtitle);

        return this;
    }

    @Override
    public void updateUI() {
        populateResources();
    }

    private Widget getResourceTypePanel(){
        SimplePanel resourcePanel = new SimplePanel();
        resourcePanel.addStyleName(style.getCSS().resourcePanel());
        resourceBox = new ListBox();
        resourceBox.setMultipleSelect(false);

        populateResources();

        resourcePanel.add(resourceBox);
        resourceBox.addChangeHandler(event -> {
            ListBox listBox = (ListBox) event.getSource();
            String resource = listBox.getValue(listBox.getSelectedIndex());
            handler.onResourceChanged(resource);
            handler.loadAnalysisData();
        });
        return resourcePanel;
    }

    private void populateResources() {
        AnalysisResult result = handler.getAnalysisResult();
        selectedResource = handler.getFilter().getResource();

        if(resourceBox.getItemCount() > 0) resourceBox.clear();

        if (result != null) {
            List<ResourceSummary> resources = handler.getAnalysisResult().getResourceSummary();
            for (ResourceSummary summary : resources) {
                String resource = summary.getResource();
                resourceBox.addItem(resource + " (" + resourceFormatter.format(summary.getPathways()) + ")", resource);
            }

            setResource(selectedResource);
        }
    }

    private void setResource(String resource){
        for (int i = 0; i < resourceBox.getItemCount(); i++) {
            String value = resourceBox.getValue(i);
            if(value.equals(resource)){
                resourceBox.setSelectedIndex(i);
                return;
            }
        }
    }
}
