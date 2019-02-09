package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ResourceChangedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ActionSelectedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ResourceChangedHandler;

import java.util.List;

import static org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.OptionBadge.Type.INLCUDE_INTERACTORS;
import static org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.OptionBadge.Type.PROJECT_TO_HUMAN;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnalysisSummaryPanel extends DockLayoutPanel {
    private static final NumberFormat formatter = NumberFormat.getFormat( "#,###" );
    private static final String DOC_URL = "/user/guide/analysis";
    static final int baseSummaryPanel = 850;
    static final int baseNameSize = 320;

    private String token;
    private ListBox resourceBox;
    private FlowPanel mainPanel;
    private FlowPanel namePanel;
    private SimplePanel overlay;

    private ActionsPanel actionsPanel;

    public AnalysisSummaryPanel(AnalysisResult analysisResult) {
        super(Style.Unit.PX);
        this.token = analysisResult.getSummary().getToken();
        AnalysisSummary summary = analysisResult.getSummary();

        boolean speciesComparison = summary.getSpecies() != null;

        this.mainPanel = new FlowPanel();
        this.mainPanel.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().summaryInfoPanel());
        this.mainPanel.add(getTypePanel(summary));
        this.mainPanel.add(getResourceTypePanel(analysisResult.getResourceSummary()));
        this.mainPanel.add(getNamePanel(summary));

        if (summary.getProjection()) {
            this.mainPanel.add(new OptionBadge(PROJECT_TO_HUMAN));
        }

        if (summary.getInteractors()) {
            this.mainPanel.add(new OptionBadge(INLCUDE_INTERACTORS));
        }

        actionsPanel = new ActionsPanel(analysisResult);
        this.addEast(actionsPanel, 150);

        this.mainPanel.add(getOverlay());
        this.add(mainPanel);
    }

    public HandlerRegistration addResourceChangeHandler(ResourceChangedHandler handler){
        return this.addHandler(handler, ResourceChangedEvent.TYPE);
    }

    public HandlerRegistration addActionSelectedHandler(ActionSelectedHandler handler) {
        return this.actionsPanel.addActionSelectedHandler(handler);
    }

    public String getToken() {
        return token;
    }


//    @Override
//    public void onActionSelected(ActionSelectedEvent event) {
//        ActionSelectedEvent.Action action = event.getAction();
//        switch (action) {
//            case FILTERING_ON:
//                setOverlay(true);
//                break;
//            case FILTERING_OFF:
//                setOverlay(false);
//                break;
//            case HELP_ON:
//                //TODO
//                break;
//            case HELP_OFF:
//                //TODO
//                break;
//            case CLUSTERING_ON:
//                //TODO
//                break;
//            case CLUSTERING_OFF:
//                //TODO
//                break;
//        }
//    }

    public void showFilteringPanel(boolean isVisible) {
        setOverlay(isVisible);
        actionsPanel.showFilteringPanel(isVisible);
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
        SimplePanel resourcePanel = new SimplePanel();
        resourcePanel.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().resourcePanel());
        resourceBox = new ListBox();
        resourceBox.setMultipleSelect(false);

        for (ResourceSummary summary : resourceSummary) {
            String resource = summary.getResource();
            resourceBox.addItem(resource + " (" + formatter.format(summary.getPathways()) + ")", resource);
        }
        resourcePanel.add(resourceBox);
        resourceBox.addChangeHandler(event -> {
            ListBox listBox = (ListBox) event.getSource();
            String resource = listBox.getValue(listBox.getSelectedIndex());
            fireEvent(new ResourceChangedEvent(resource));
        });
        return resourcePanel;
    }

    private Widget getTypePanel(AnalysisSummary summary) {
        String type = summary.getType().replaceAll("_", " ").toLowerCase();

        Anchor typeAnchor = new Anchor( type, DOC_URL, "_blank");
        typeAnchor.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().summaryItem());
        typeAnchor.setTitle("Find out more about this type of analysis");
        typeAnchor.getElement().getStyle().setTextTransform(Style.TextTransform.CAPITALIZE);

        Label lb = new Label(type.startsWith("species") ? "for" : "analysis results for");
        lb.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().summaryItem());

        FlowPanel typePanel = new FlowPanel();
        typePanel.add(typeAnchor);
        typePanel.add(lb);
        return typePanel;
    }

    private Widget getNamePanel(AnalysisSummary summary) {
        namePanel = new FlowPanel();
        namePanel.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().namePanel());
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
            namePanel.add(new InlineLabel(sb.toString()));
        }else{
            namePanel.add(new InlineLabel("Homo sapiens compared with "));
            namePanel.add(new InlineLabel(summary.getSpeciesName()));
        }
        return namePanel;
    }

    private Widget getOverlay() {
        overlay = new SimplePanel();
        overlay.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().overlay());
        return overlay;
    }

    private void setOverlay(boolean overlayOn) {
        if(overlayOn) {
            overlay.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        } else {
            overlay.getElement().getStyle().setDisplay(Style.Display.NONE);
        }
    }

    private void updateNamePanelSize() {
        int curPanelSize = this.getElement().getOffsetWidth();
        if (curPanelSize > 860) {
            namePanel.getElement().setAttribute("max-width", baseNameSize + (curPanelSize - baseSummaryPanel) + "px");
        } else {
            namePanel.getElement().setAttribute("max-width", baseNameSize + "px");
        }
    }

    @Override
    public void onResize() {
        super.onResize();
        actionsPanel.hideWarnings();
        updateNamePanelSize();
    }
}
