package org.reactome.web.pwp.client.tools.analysis.gsa.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAStyleFactory {

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public static Style getStyle() {
        return  RESOURCES.getCSS();
    }


    /**
     * A ClientBundle of resources used by this module.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(Style.CSS)
        Style getCSS();

        @Source("../images/parameters.png")
        ImageResource nextIcon();

        @Source("../images/parameters.png")
        ImageResource previousIcon();

        @Source("../images/addNewItem.png")
        ImageResource addIcon();
    }

    /**
     * Styles used by this module.
     */
    @CssResource.ImportedWithPrefix("pwp-GSAStyleFactory")
    public interface Style extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/tools/analysis/gsa/style/GSAStyleFactory.css";

        String container();

        String title();

        String methodsPanel();

        String navigationPanel();

        String rightButton();

        String leftButton();

        String navigationBtn();

        String datasetsListContainer();

        String datasetsList();

        String addNewDatasetBtn();

        String rotate();

        String nameLabel();

        String nameTextBox();

        String annotationsPlaceholder();

    }
}
