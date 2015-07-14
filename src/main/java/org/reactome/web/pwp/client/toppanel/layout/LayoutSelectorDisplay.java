package org.reactome.web.pwp.client.toppanel.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class LayoutSelectorDisplay extends Composite implements LayoutSelector.Display, ClickHandler {

    private LayoutSelector.Presenter presenter;

    private LayoutButton hierarchy;
    private LayoutButton details;
    private LayoutButton viewport;

    public LayoutSelectorDisplay() {
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName(RESOURCES.getCSS().layoutPanel());
        flowPanel.add(new SimplePanel(new InlineLabel("Layout:")));
        flowPanel.add(this.hierarchy = new LayoutButton("show/hide hierarchy panel", RESOURCES.getCSS().hierarchy(), this));
        flowPanel.add(this.details = new LayoutButton("show/hide details panel", RESOURCES.getCSS().details(), this));
        flowPanel.add(this.viewport = new LayoutButton("expand/minimise centre display", RESOURCES.getCSS().centre(), this));
        initWidget(flowPanel);
    }

    @Override
    public void setPresenter(LayoutSelector.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClick(ClickEvent event) {
        LayoutButton btn = (LayoutButton) event.getSource();
        if (btn == this.hierarchy) {
            this.presenter.layoutSelectorChanged(LayoutSelectorType.HIERARCHY);
        } else if (btn == this.details) {
            this.presenter.layoutSelectorChanged(LayoutSelectorType.DETAILS);
        } else if (btn == this.viewport ) {
            this.presenter.layoutSelectorChanged(LayoutSelectorType.VIEWPORT);
        }
    }


    public static Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ResoruceCSS.CSS)
        ResoruceCSS getCSS();

        @Source("images/hierarchy_clicked.png")
        ImageResource hierarchyClicked();

        @Source("images/hierarchy_hovered.png")
        ImageResource hierarchyHovered();

        @Source("images/hierarchy_normal.png")
        ImageResource hierarchyNormal();

        @Source("images/details_clicked.png")
        ImageResource detailsClicked();

        @Source("images/details_hovered.png")
        ImageResource detailsHovered();

        @Source("images/details_normal.png")
        ImageResource detailsNormal();

        @Source("images/centre_clicked.png")
        ImageResource centreClicked();

        @Source("images/centre_hovered.png")
        ImageResource centreHovered();

        @Source("images/centre_normal.png")
        ImageResource centreNormal();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-LayoutSelector")
    public interface ResoruceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/toppanel/layout/Layout.css";

        String layoutPanel();

        String hierarchy();

        String details();

        String centre();
    }
}
