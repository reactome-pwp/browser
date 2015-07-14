package org.reactome.web.pwp.client.tools.analysis;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import org.reactome.web.pwp.client.tools.launcher.LauncherButton;
import org.reactome.web.pwp.client.tools.analysis.event.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.tools.analysis.event.AnalysisErrorEvent;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisCompletedHandler;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisErrorEventHandler;
import org.reactome.web.pwp.client.tools.analysis.submitters.AnalysisStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.submitters.FileSubmitter;
import org.reactome.web.pwp.client.tools.analysis.submitters.PostSubmitter;
import org.reactome.web.pwp.client.tools.analysis.submitters.SpeciesSubmitter;
import org.reactome.web.pwp.model.classes.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisLauncherDisplay extends PopupPanel implements AnalysisLauncher.Display, ResizeHandler, AnalysisCompletedHandler, AnalysisErrorEventHandler, ClickHandler, CloseHandler<PopupPanel> {

    private AnalysisLauncher.Presenter presenter;

    private SpeciesSubmitter speciesSubmitter;

    public AnalysisLauncherDisplay() {
        super(true, true);
        this.setAnimationEnabled(true);
        this.setGlassEnabled(true);
        this.setAutoHideOnHistoryEventsEnabled(false);
        this.addStyleName(RESOURCES.getCSS().popupPanel());
        Window.addResizeHandler(this);

        int width = (int) Math.round(Window.getClientWidth() * 0.9);
        int height = (int) Math.round(Window.getClientHeight() * 0.9);
        this.setWidth(width + "px");
        this.setHeight(height + "px");

        FlowPanel vp = new FlowPanel();
        vp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisContainer());

        vp.add(new LauncherButton("close", RESOURCES.getCSS().close(), this));

        PostSubmitter postSubmitter = new PostSubmitter();
        postSubmitter.addAnalysisCompletedEventHandler(this);
        postSubmitter.addAnalysisErrorEventHandler(this);

        FileSubmitter fileSubmitter = new FileSubmitter(postSubmitter);
        fileSubmitter.addAnalysisCompletedEventHandler(this);
        fileSubmitter.addAnalysisErrorEventHandler(this);
        vp.add(fileSubmitter);

        this.speciesSubmitter = new SpeciesSubmitter();
        this.speciesSubmitter.addAnalysisCompletedEventHandler(this);
        this.speciesSubmitter.addAnalysisErrorEventHandler(this);
        vp.add(this.speciesSubmitter);

        ScrollPanel container = new ScrollPanel();
        container.setStyleName(RESOURCES.getCSS().analysisPanel());
        container.getElement().setAttribute("min-width", "600px");
        container.add(vp);

        this.addCloseHandler(this);

        this.add(container);
    }

    @Override
    public void onAnalysisCompleted(AnalysisCompletedEvent event) {
        presenter.analysisCompleted(event);
    }

    @Override
    public void onAnalysisError(AnalysisErrorEvent event) {
        presenter.analysisError(event);
    }

    @Override
    public void onClick(ClickEvent event) {
        this.hide();
    }

    @Override
    public void onClose(CloseEvent<PopupPanel> event) {
        presenter.displayClosed();
    }

    @Override
    public void onResize(ResizeEvent event) {
        if(isVisible()){
            int width = (int) Math.round(RootLayoutPanel.get().getOffsetWidth() * 0.9);
            int height = (int) Math.round(RootLayoutPanel.get().getOffsetHeight() * 0.9);
            this.setWidth(width + "px");
            this.setHeight(height + "px");
        }
    }

    @Override
    public void setPresenter(AnalysisLauncher.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setSpeciesList(List<Species> speciesList) {
        this.speciesSubmitter.setSpeciesList(speciesList);
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

        @Source("images/close_clicked.png")
        ImageResource closeClicked();

        @Source("images/close_hovered.png")
        ImageResource closeHovered();

        @Source("images/close_normal.png")
        ImageResource closeNormal();

    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-AnalysisLauncher")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/tools/analysis/AnalysisLauncher.css";

        String popupPanel();

        String analysisPanel();

        String close();
    }
}
