package org.reactome.web.pwp.client.tools.citation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.tools.launcher.LauncherButton;

import java.util.Date;

public class CitationLauncherDisplay extends PopupPanel implements CitationLauncher.Display, CloseHandler<PopupPanel>,
        ResizeHandler {

    private CitationLauncher.Presenter presenter;
    private Label citation;
    private HorizontalPanel buttonBar;
    private Label buttonBarHeading;

    public CitationLauncherDisplay() {
        super(true, true);
        setAnimationEnabled(true);
        setGlassEnabled(true);
        setAutoHideOnHistoryEventsEnabled(false);
        addStyleName(RESOURCES.getCSS().popupPanel());
        Window.addResizeHandler(this);

        int width = (int) Math.round(Window.getClientWidth() * 0.9);
        int height = (int) Math.round(Window.getClientHeight() * 0.9);
        this.setWidth(width + "px");
        this.setHeight(height + "px");

        FlowPanel mainPanel = new FlowPanel();                         // Main panel
        mainPanel.addStyleName(RESOURCES.getCSS().mainPanel());
        mainPanel.add(setTitlePanel());                                // Title panel with heading & close button

        FlowPanel citationPanel = new FlowPanel();                     // panel that will contain the citation text
        citationPanel.addStyleName(RESOURCES.getCSS().citationPanel());
        citation = new Label();
        citation.addStyleName(RESOURCES.getCSS().citationText());
        citationPanel.add(citation);
        mainPanel.add(citationPanel);

        buttonBarHeading = new Label();
        buttonBarHeading.addStyleName(RESOURCES.getCSS().buttonHeading());
        mainPanel.add(buttonBarHeading);

        buttonBar = new HorizontalPanel();                             // panel with all the export buttons
        FlowPanel buttonPanel = new FlowPanel();
        buttonPanel.addStyleName(RESOURCES.getCSS().buttonPanel());
        buttonPanel.add(buttonBar);
        mainPanel.add(buttonPanel);
        this.addCloseHandler(this);
        this.add(mainPanel);
    }

    @Override
    public void show() {
        super.show();
    }

    public void setCitation(String citation) {
        this.citation.setText(citation);
    }

    @Override
    public void onClose(CloseEvent<PopupPanel> event) {
        presenter.displayClosed();
        buttonBar = new HorizontalPanel();
    }

    @Override
    public void onResize(ResizeEvent event) {
        if (isVisible()) {
            int width = (int) Math.round(RootLayoutPanel.get().getOffsetWidth() * 0.9);
            int height = (int) Math.round(RootLayoutPanel.get().getOffsetHeight() * 0.9);
            this.setWidth(width + "px");
            this.setHeight(height + "px");
        }
    }

    @Override
    public void setPresenter(CitationLauncher.Presenter presenter) {
        this.presenter = presenter;
    }

    // sets the panel title and close button
    private Widget setTitlePanel() {
        FlowPanel header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().header());
        header.addStyleName(RESOURCES.getCSS().unselectable());
        Label title = new Label("Cite Us!");
        title.setStyleName(RESOURCES.getCSS().headerText());
        Button closeBtn = new LauncherButton("Close citation modal", RESOURCES.getCSS().close(), clickEvent -> CitationLauncherDisplay.this.hide());
        header.add(title);
        header.add(closeBtn);
        return header;
    }

    public void setExportBar(String id) {
        this.buttonBarHeading.setText("Download As: ");
        for (ExportType ef : ExportType.values()) {
            this.buttonBar.add(getExportButton(ef.getIcon(), ef.getTitle(), ef.getUrl() + "&id=" + id
                            + "&dateAccessed=" + DateTimeFormat.getFormat("E MMM dd yyyy").format(new Date()),
                    "reactome_citation_" + id + ef.getExt()));
        }
    }

    public FlowPanel getExportButton(ImageResource icon, String title, String url, String filename) {

        FlowPanel button = new FlowPanel();
        button.addStyleName(RESOURCES.getCSS().exportItem());
        button.setTitle(title);

        FlowPanel image = new FlowPanel();
        image.add(new Image(icon));

        Anchor anchor = new Anchor(SafeHtmlUtils.fromTrustedString(image.toString()), url, "_blank");
        anchor.getElement().setAttribute("rel", "noindex,nofollow");
        anchor.getElement().setAttribute("download", filename);

        button.add(anchor);

        return button;
    }

    public static CitationLauncherDisplay.Resources RESOURCES;

    static {
        RESOURCES = GWT.create(CitationLauncherDisplay.Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(CitationLauncherDisplay.ResourceCSS.CSS)
        CitationLauncherDisplay.ResourceCSS getCSS();

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
    @CssResource.ImportedWithPrefix("pwp-CitationLauncher")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/tools/citation/CitationLauncher.css";

        String popupPanel();

        String mainPanel();

        String citationPanel();

        String header();

        String headerText();

        String citationText();

        String close();

        String unselectable();

        String buttonPanel();

        String exportItem();

        String buttonHeading();
    }
}
