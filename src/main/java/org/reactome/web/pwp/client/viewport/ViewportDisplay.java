package org.reactome.web.pwp.client.viewport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import org.reactome.web.pwp.client.viewport.diagram.Diagram;
import org.reactome.web.pwp.client.viewport.fireworks.Fireworks;
import org.reactome.web.pwp.client.viewport.welcome.Welcome;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ViewportDisplay extends TabLayoutPanel implements Viewport.Display {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Viewport.Presenter presenter;

    private Fireworks.Display fireworks;
    private Diagram.Display diagram;
    private Welcome.Display welcome;

    public ViewportDisplay(Diagram.Display diagram, Fireworks.Display fireworks) {
        super(0, Style.Unit.PX);
        this.setAnimationDuration(0);
        setStyleName(RESOURCES.getCSS().viewportContainer());

        this.fireworks = fireworks;
        this.fireworks.asWidget().addStyleName(RESOURCES.getCSS().viewportPanel());
        this.diagram = diagram;
        this.diagram.asWidget().addStyleName(RESOURCES.getCSS().viewportPanel());

        add(this.fireworks);
        add(this.diagram);
    }

    public ViewportDisplay( Diagram.Display diagram, Welcome.Display welcome) {
        super(0, Style.Unit.PX);
        this.setAnimationDuration(0);
        setStyleName(RESOURCES.getCSS().viewportContainer());

        this.diagram = diagram;
        this.diagram.asWidget().addStyleName(RESOURCES.getCSS().viewportPanel());
        this.welcome = welcome;
        this.welcome.asWidget().addStyleName(RESOURCES.getCSS().viewportPanel());

        add(this.welcome);
        add(this.diagram);
    }

    @Override
    public void setPresenter(Viewport.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDiagram() {
        selectTab(getWidgetIndex(this.diagram));
    }

    @Override
    public void showFireworks() {
        selectTab(getWidgetIndex(this.fireworks));
    }

    @Override
    public void showWelcome() {
        selectTab(getWidgetIndex(this.welcome));
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
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-Viewport")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/viewport/Viewport.css";

        String viewportContainer();

        String viewportPanel();
    }
}
