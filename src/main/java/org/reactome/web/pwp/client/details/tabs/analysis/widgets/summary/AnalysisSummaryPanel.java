package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ActionSelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ActionSelectedHandler;

import static org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.OptionBadge.Type.INLCUDE_INTERACTORS;
import static org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.OptionBadge.Type.PROJECT_TO_HUMAN;
import static org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ActionSelectedEvent.Action.FILTERING_ON;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnalysisSummaryPanel extends DockLayoutPanel {
    private static final String DOC_URL = "/user/guide/analysis";
    static final int baseSummaryPanel = 830;
    static final int baseNameSize = 380;

    private String token;
    private String resource = "TOTAL";
    private FlowPanel mainPanel;
    private FlowPanel namePanel;
    private Label resourceLabel;
    private SimplePanel overlay;

    private ActionsPanel actionsPanel;

    public AnalysisSummaryPanel(AnalysisResult analysisResult, String resource) {
        super(Style.Unit.PX);
        AnalysisSummary summary = analysisResult.getSummary();

        this.token = summary.getToken();
        this.resource = resource;

        boolean speciesComparison = summary.getSpecies() != null;

        this.mainPanel = new FlowPanel();
        this.mainPanel.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().summaryInfoPanel());
        this.mainPanel.add(getTypePanel(summary));
        this.mainPanel.add(getNamePanel(summary));

        if (!speciesComparison && summary.getProjection()) {
            this.mainPanel.add(new OptionBadge(PROJECT_TO_HUMAN));
        }

        if (!speciesComparison && summary.getInteractors()) {
            this.mainPanel.add(new OptionBadge(INLCUDE_INTERACTORS));
        }

        actionsPanel = new ActionsPanel(analysisResult);
        this.addEast(actionsPanel, 110);

        this.mainPanel.add(getOverlay());
        this.add(mainPanel);
    }

    public HandlerRegistration addActionSelectedHandler(ActionSelectedHandler handler) {
        this.addHandler(handler, ActionSelectedEvent.TYPE);
        return this.actionsPanel.addActionSelectedHandler(handler);
    }

    public void setResource(String resource) {
        this.resource = resource;
        updateResourceLabel();
    }

    public String getToken() {
        return token;
    }

    public void showFilteringPanel(boolean isVisible) {
        setOverlay(isVisible);
        actionsPanel.showFilteringPanel(isVisible);
    }

    private Widget getTypePanel(AnalysisSummary summary) {
        String type = summary.getType().replaceAll("_", " ").toLowerCase();

        Anchor typeAnchor = new Anchor( type, DOC_URL, "_blank");
        typeAnchor.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().summaryItem());
        typeAnchor.setTitle("Find out more about this type of analysis");
        typeAnchor.getElement().getStyle().setTextTransform(Style.TextTransform.CAPITALIZE);

        Label lb = new Label(type.startsWith("species") ? "" : "analysis results for ");
        lb.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().summaryItem());

        resourceLabel = new Label(type.startsWith("species") ? "" : resource);
        resourceLabel.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().summaryResource());
        resourceLabel.setTitle("Click to change the selected resource");
        resourceLabel.addClickHandler(e -> fireEvent(new ActionSelectedEvent(FILTERING_ON)));

        FlowPanel typePanel = new FlowPanel();
        typePanel.add(typeAnchor);
        typePanel.add(lb);
        typePanel.add(resourceLabel);
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

    private void updateResourceLabel() {
        resourceLabel.setText(resource);
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
            namePanel.getElement().getStyle().setProperty("maxWidth", baseNameSize + (curPanelSize - baseSummaryPanel) + "px");
        } else {
            namePanel.getElement().getStyle().setProperty("maxWidth", baseNameSize + "px");
        }
    }

    @Override
    public void onResize() {
        super.onResize();
        actionsPanel.hideWarnings();
        updateNamePanelSize();
    }
}
