package org.reactome.web.pwp.client.details.tabs.analysis.widgets.downloads;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Report;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAAnalysisDownloadItem extends FocusPanel implements ClickHandler {

    private Report report;

    public GSAAnalysisDownloadItem(Report report) {
        this.report = report;
        setStyleName(RESOURCES.getCSS().item());

        Image icon = new Image(AnalysisDownloadType.GSA_REPORT.getIcon());
        icon.setStyleName(RESOURCES.getCSS().icon());

        FlowPanel leftPanel = new FlowPanel();
        leftPanel.setStyleName(RESOURCES.getCSS().leftPanel());
        leftPanel.add(icon);

        Label title = new Label(report.getName());
        title.setStyleName(RESOURCES.getCSS().title());
        String format = report.getUrl().substring(report.getUrl().lastIndexOf(".") + 1).toUpperCase();
        HTMLPanel info = new HTMLPanel(AnalysisDownloadType.GSA_REPORT.getInfo().getText().replaceAll("###FORMAT###", format));

        info.setStyleName(RESOURCES.getCSS().info());

        FlowPanel rightPanel = new FlowPanel();
        rightPanel.setStyleName(RESOURCES.getCSS().rightPanel());
        rightPanel.add(title);
        rightPanel.add(info);

        FlowPanel main = new FlowPanel();
        main.add(leftPanel);
        main.add(rightPanel);
        add(main);

        addClickHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        String link = report.getUrl();
        Window.open(link, "_blank", "");
    }

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    @CssResource.ImportedWithPrefix("pwp-AnalysisDownloadItem")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/downloads/AnalysisDownloadItem.css";

        String item();

        String leftPanel();

        String rightPanel();

        String icon();

        String title();

        String info();

    }
}
