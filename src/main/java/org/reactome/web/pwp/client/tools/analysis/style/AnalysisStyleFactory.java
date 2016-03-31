package org.reactome.web.pwp.client.tools.analysis.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class AnalysisStyleFactory {

    private static AnalysisWizardResources RESOURCES = null;

    /**
     * A ClientBundle of resources used by this module.
     */
    public interface AnalysisWizardResources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(AnalysisWizardStyle.DEFAULT_CSS)
        AnalysisWizardStyle analysisStyle();

    }

    /**
     * Styles used by this module.
     */
    @CssResource.ImportedWithPrefix("pwp-analysisWizardPanel")
    public interface AnalysisWizardStyle extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String DEFAULT_CSS = "org/reactome/web/pwp/client/tools/analysis/style/AnalysisStyle.css";

        String analysisTitle();

        String analysisText();

        String analysisExample();

        String analysisCheckBox();

        String analysisSubmission();

        String analysisMainSubmitter();

        String emphasis();

        String postSubmitter();

        String postSubmitterExamples();

        String postSubmitterClear();

        String optionsPanel();

        String optionsPanelCheckBox();

        String unselectable();

        String statusIcon();

        String statusIconVisible();
    }

    public static AnalysisWizardStyle getAnalysisStyle(){
        if(RESOURCES==null){
            RESOURCES = GWT.create(AnalysisWizardResources.class);
            RESOURCES.analysisStyle().ensureInjected();
        }
        return RESOURCES.analysisStyle();
    }


}
