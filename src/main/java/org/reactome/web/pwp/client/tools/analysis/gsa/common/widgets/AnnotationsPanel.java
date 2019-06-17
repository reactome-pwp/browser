package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnnotationsPanel extends FlowPanel {


    public AnnotationsPanel() {
    }

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

//        @Source("../../images/unchecked.png")
//        ImageResource uncheckedIcon();
//
//        @Source("../../images/checked.png")
//        ImageResource checkedIcon();
//
//        @Source("../../images/parameters.png")
//        ImageResource parametersIcon();
    }

    @CssResource.ImportedWithPrefix("pwp-AnnotationsPanel")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/tools/analysis/gsa/common/widgets/AnnotationsPanel.css";

        String main();

    }
}
