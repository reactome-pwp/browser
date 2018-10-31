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
import org.reactome.web.pwp.client.AppConfig;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.PathwayPortalTool;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ToolLauncherDisplay extends Composite implements ToolLauncher.Display, ClickHandler /*, CloseHandler<PopupPanel>*/ {

    private ToolLauncher.Presenter presenter;
    private IconButton analysisBtn;

    private final static String TOOLTIP = "Analyse your data...";
    private final static String TOOLTIP_WARNING = "The AnalysisService and the ContentService are running with different database versions.";
    private final static String TOOLTIP_ERROR = "Unable to connect to the server.";


    public ToolLauncherDisplay() {
        this.analysisBtn = new IconButton("", RESOURCES.analysisIcon());
        this.analysisBtn.setTitle("Analyse your data...");
        this.analysisBtn.setStyleName(RESOURCES.getCSS().analysisBtn());
        this.analysisBtn.addClickHandler(this);

        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName(RESOURCES.getCSS().launcherPanel());
        flowPanel.add(new SimplePanel(new InlineLabel("Analysis:")));
        flowPanel.add(this.analysisBtn);
        //The analysis tools are not available for the curation sites
        flowPanel.setVisible(!AppConfig.getIsCurator());
        initWidget(flowPanel);
    }

    @Override
    public void setPresenter(ToolLauncher.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setStatus(ToolLauncher.ToolStatus status) {
        switch (status) {
            case ACTIVE:
                analysisBtn.clearOverlayIcon();
                this.analysisBtn.setTitle(TOOLTIP);
                break;
            case WARNING:
                analysisBtn.setOverlayIcon(CommonImages.INSTANCE.warning());
                this.analysisBtn.setTitle(TOOLTIP_WARNING);
                break;
            case ERROR:
                analysisBtn.setOverlayIcon(CommonImages.INSTANCE.error());
                this.analysisBtn.setTitle(TOOLTIP_ERROR);
                break;
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        IconButton btn = (IconButton) event.getSource();
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

        @Source("images/analysis.png")
        ImageResource analysisIcon();

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

        String analysisBtn();

    }
}
