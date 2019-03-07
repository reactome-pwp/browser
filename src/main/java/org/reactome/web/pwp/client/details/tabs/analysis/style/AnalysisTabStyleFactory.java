package org.reactome.web.pwp.client.details.tabs.analysis.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public abstract class AnalysisTabStyleFactory {

    public static final AnalysisTabResources RESOURCES;
    static {
        RESOURCES = GWT.create(AnalysisTabResources.class);
        RESOURCES.css().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this module.
     */
    public interface AnalysisTabResources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ResourceCSS.DEFAULT_CSS)
        ResourceCSS css();

        @Source("../images/results_tab.png")
        ImageResource resultsIcon();

        @Source("../images/notfound_tab.png")
        ImageResource notFoundIcon();

        @Source("../images/downloads_tab.png")
        ImageResource downloadsIcon();

        @Source("../images/project_to_human.png")
        ImageResource projectToHumanIcon();

        @Source("../images/include_interactors.png")
        ImageResource includeInteractorsIcon();

        @Source("../images/warning.png")
        ImageResource warningIcon();

        @Source("../images/cluster.png")
        ImageResource clusterIcon();

        @Source("../images/filter.png")
        ImageResource filterIcon();

        @Source("../images/help.png")
        ImageResource helpIcon();

        @Source("../images/back.png")
        ImageResource backIcon();
    }

    /**
     * Styles used by this module.
     */
    @CssResource.ImportedWithPrefix("reactome-analysisPanel")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String DEFAULT_CSS = "org/reactome/web/pwp/client/details/tabs/analysis/style/AnalysisTabStyle.css";

        String summaryInfoPanel();

        String overlay();

        String overlayOnActionsPanel();

        String summaryItem();

        String summaryResource();

        String namePanel();

        String optionBadge();

        String actionsPanel();

        String warningBtn();

        String analysisRowSelector();

        String unselectable();

        String undraggable();

        String tabButtonsPanel();

        String tabButton();

        String tabButtonSelected();

        String tabBadge();

        String panelHeader();

        String panelFooter();

        String downloadContainer();
    }
}
