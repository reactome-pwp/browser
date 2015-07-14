package org.reactome.web.pwp.client.details.tabs.analysis.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
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
        @Source(AnalysisTabStyle.DEFAULT_CSS)
        AnalysisTabStyle css();

    }

    /**
     * Styles used by this module.
     */
    @CssResource.ImportedWithPrefix("reactome-analysisPanel")
    public interface AnalysisTabStyle extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String DEFAULT_CSS = "org/reactome/web/pwp/client/details/tabs/analysis/style/AnalysisTabStyle.css";

        String analysisTabSummary();

        String analysisTabSummaryInfo();

//        String analysisOptions();

        String analysisTableSelector();

        String analysisTableSelectorButton();

        String analysisRowSelector();

//        String notFoundIdentifiers();
//
//        String notFoundIdentifierSelected();
    }
}
