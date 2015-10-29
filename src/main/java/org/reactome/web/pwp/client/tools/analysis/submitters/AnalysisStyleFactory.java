package org.reactome.web.pwp.client.tools.analysis.submitters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class AnalysisStyleFactory {

    private static AnalysisResources RESOURCES = null;

    /**
     * A ClientBundle of resources used by this module.
     */
    public interface AnalysisResources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(AnalysisStyle.DEFAULT_CSS)
        AnalysisStyle analysisStyle();

    }

    /**
     * Styles used by this module.
     */
    @CssResource.ImportedWithPrefix("pwp-analysisPanel")
    public interface AnalysisStyle extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String DEFAULT_CSS = "org/reactome/web/pwp/client/tools/analysis/style/AnalysisStyle.css";

        String analysisContainer();

        String analysisTitle();

        String analysisText();

        String analysisBlock();

        String analysisCheckBox();

        String analysisSubmission();

        String analysisMainSubmitter();

        String disclosurePanel();

        String emphasis();

        String postSubmitterExamples();

        String postSubmitterCheckBox();

        String postSubmitterClear();

        String unselectable();

        String statusIcon();

        String statusIconVisible();
    }

    public static AnalysisStyle getAnalysisStyle(){
        if(RESOURCES==null){
            RESOURCES = GWT.create(AnalysisResources.class);
            RESOURCES.analysisStyle().ensureInjected();
        }
        return RESOURCES.analysisStyle();
    }


}
