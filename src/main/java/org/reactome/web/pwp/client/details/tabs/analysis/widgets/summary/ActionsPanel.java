package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ActionSelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.ActionSelectedHandler;

import java.util.List;

import static org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ActionSelectedEvent.Action.*;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ActionsPanel extends FlowPanel implements ClickHandler {

    private Button helpBtn;
    private Button filterBtn;
    private Button clusterBtn;
    private Button warningBtn;

    private SimplePanel overlay;

    private List<String> warningsList;
    private NotificationPopup popupInstance;

    private boolean helpBtnStatus;
    private boolean filterBtnStatus;
    private boolean clusterBtnStatus;

    public ActionsPanel(AnalysisResult analysisResult) {
        AnalysisTabStyleFactory.AnalysisTabResources resources = AnalysisTabStyleFactory.RESOURCES;
        setStyleName(resources.css().actionsPanel());

        add(filterBtn = new IconButton(resources.filterIcon(), resources.css().warningBtn(), "Filter your results", this));
        add(helpBtn = new IconButton(resources.helpIcon(), resources.css().warningBtn(), "Get help with the analysis", this));
        add(clusterBtn = new IconButton(resources.clusterIcon(), resources.css().warningBtn(), "Cluster your results", this));

        // Get tha analysis warnings
        warningsList = analysisResult.getWarnings();
        if (warningsList != null && !warningsList.isEmpty()) {
            add(warningBtn = new IconButton(resources.warningIcon(), resources.css().warningBtn(), warningsList.size() + " warnings reported", this));
        }

        popupInstance = new NotificationPopup();

        overlay = new SimplePanel();
        overlay.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().overlayOnActionsPanel());
        add(overlay);

    }

    public HandlerRegistration addActionSelectedHandler(ActionSelectedHandler handler){
        return this.addHandler(handler, ActionSelectedEvent.TYPE);
    }

    @Override
    public void onClick(ClickEvent event) {
        Button btn = (Button) event.getSource();
        if (btn.equals(filterBtn)) {
            fireEvent(new ActionSelectedEvent(filterBtnStatus ? FILTERING_OFF : FILTERING_ON));
            showFilteringPanel(filterBtnStatus);
        } else if (btn.equals(helpBtn)) {
            Console.info("Help is coming...");
        } else if (btn.equals(clusterBtn)) {
            clusterBtnStatus = !clusterBtnStatus;
            fireEvent(new ActionSelectedEvent(clusterBtnStatus ? CLUSTERING_ON : CLUSTERING_OFF));
        } else if (btn.equals(warningBtn)) {
            popupInstance.setMessage(warningsList);
            popupInstance.showPanel(btn);
        }
    }

    public void showFilteringPanel(boolean isVisible) {
        filterBtnStatus = isVisible;
        setOverlay(isVisible);
    }


    public void hideWarnings() {
        if(popupInstance.isDisplayed()) {
            popupInstance.hide();
        }
    }

    private void setOverlay(boolean overlayOn) {
        if(overlayOn) {
            overlay.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        } else {
            overlay.getElement().getStyle().setDisplay(Style.Display.NONE);
        }
    }

}
