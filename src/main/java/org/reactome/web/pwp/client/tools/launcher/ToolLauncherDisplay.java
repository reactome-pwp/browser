package org.reactome.web.pwp.client.tools.launcher;

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
import org.reactome.web.pwp.client.common.PathwayPortalTool;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ToolLauncherDisplay extends Composite implements ToolLauncher.Display, ClickHandler /*, CloseHandler<PopupPanel>*/ {

    private ToolLauncher.Presenter presenter;
    private LauncherButton analysisBtn;

    public ToolLauncherDisplay() {
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName(RESOURCES.getCSS().launcherPanel());
        this.analysisBtn= new LauncherButton("Analyse your data...", RESOURCES.getCSS().analysis(), this);
        flowPanel.add(new SimplePanel(new InlineLabel("Analysis:")));
        flowPanel.add(this.analysisBtn);
        initWidget(flowPanel);
    }

    @Override
    public void setPresenter(ToolLauncher.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void onClick(ClickEvent event) {
        LauncherButton btn = (LauncherButton) event.getSource();
        if(btn.equals(this.analysisBtn)){
            presenter.toolSelected(PathwayPortalTool.ANALYSIS);
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
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("images/analysis_clicked.png")
        ImageResource analysisClicked();

        @Source("images/analysis_hovered.png")
        ImageResource analysisHovered();

        @Source("images/analysis_normal.png")
        ImageResource analysisNormal();

    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-ToolLauncher")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/tools/launcher/ToolLauncher.css";

        String launcherPanel();

        String analysis();

    }
}
