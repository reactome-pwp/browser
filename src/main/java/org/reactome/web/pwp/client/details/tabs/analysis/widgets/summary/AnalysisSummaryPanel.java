package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ResourceChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.OptionSelectedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ResourceChangedHandler;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisSummaryPanel extends DockLayoutPanel {
    private String token;
    private ListBox resourceBox;

    private TableSelectorPanel selectorPanel;

    public AnalysisSummaryPanel(AnalysisResult analysisResult) {
        super(Style.Unit.PX);
        this.token = analysisResult.getSummary().getToken();
        AnalysisSummary summary = analysisResult.getSummary();

        boolean speciesComparison = summary.getSpecies()!=null;
        this.selectorPanel = new TableSelectorPanel(analysisResult.getIdentifiersNotFound(), speciesComparison);
        this.addEast(this.selectorPanel, 330);

        FlowPanel fp = new FlowPanel();
        fp.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().analysisTabSummary());
        fp.add(getResourceTypePanel(analysisResult.getResourceSummary()));
        fp.add(getTypePanel(summary));
        fp.add(getOptionPanel(summary));

        // Get tha analysis warnings
        List<String> warningsList = analysisResult.getWarnings();
        if(warningsList!=null && !warningsList.isEmpty()) {
            NotificationPanel notificationPanel = new NotificationPanel(warningsList);
            fp.add(notificationPanel);
        }

        this.add(fp);
    }

    public HandlerRegistration addOptionSelectedHandler(OptionSelectedHandler handler){
        return this.selectorPanel.addOptionSelectedHandler(handler);
    }

    public HandlerRegistration addResourceChangeHandler(ResourceChangedHandler handler){
        return this.addHandler(handler, ResourceChangedEvent.TYPE);
    }

    public String getToken() {
        return token;
    }

    public void setResource(String resource){
        for (int i = 0; i < resourceBox.getItemCount(); i++) {
            String value = resourceBox.getValue(i);
            if(value.equals(resource)){
                resourceBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private Widget getResourceTypePanel(List<ResourceSummary> resourceSummary){
        FlowPanel resourcePanel = new FlowPanel();
        resourcePanel.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().analysisTabSummaryInfo());
        resourcePanel.add(new InlineLabel("Results for: "));
        resourceBox = new ListBox();
        resourceBox.setMultipleSelect(false);
//        boolean noTotal = resourceSummary.size()==2;
        for (ResourceSummary summary : resourceSummary) {
            String resource = summary.getResource();
//            if(noTotal && resource.equals("TOTAL")) continue;
            resourceBox.addItem(resource + " (" + summary.getPathways() + ")", resource);
        }
        resourcePanel.add(resourceBox);
        resourceBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                ListBox listBox = (ListBox) event.getSource();
                String resource = listBox.getValue(listBox.getSelectedIndex());
                fireEvent(new ResourceChangedEvent(resource));
            }
        });
        return resourcePanel;
    }

    private Widget getTypePanel(AnalysisSummary summary){
        FlowPanel typePanel = new FlowPanel();
        typePanel.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().analysisTabSummaryInfo());
        typePanel.add(new InlineLabel("Type: "));
        InlineLabel type = new InlineLabel(summary.getType().replaceAll("_", " ").toLowerCase());
        type.getElement().getStyle().setTextTransform(Style.TextTransform.CAPITALIZE);
        typePanel.add(type);

        return typePanel;
    }

    private Widget getOptionPanel(AnalysisSummary summary){
        FlowPanel optPanel = new FlowPanel();
        optPanel.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().analysisTabSummaryInfo());
        if(summary.getSpecies()==null){
            StringBuilder sb = new StringBuilder();
            String fileName = summary.getFileName();
            if(fileName!=null && !fileName.isEmpty()){
                sb.append("[File: ").append(fileName.trim()).append("] ");
            }
            String identifiersName = summary.getSampleName();
            if(identifiersName!=null && !identifiersName.isEmpty()){
                sb.append("[Data: ").append(identifiersName.trim()).append("]");
            }
            if(sb.length()==0){
                sb.append("Data submitted with no name");
            }
            optPanel.add(new InlineLabel(sb.toString()));
        }else{
            optPanel.add(new InlineLabel("Homo sapiens compared with "));
            optPanel.add(new InlineLabel(summary.getSpeciesName()));
        }
        return optPanel;
    }

    public void setSelected(AnalysisInfoType type){
        this.selectorPanel.setSelected(type);
    }

    public void setDownAll(boolean down){
        this.selectorPanel.setDownAll(down);
    }
}
