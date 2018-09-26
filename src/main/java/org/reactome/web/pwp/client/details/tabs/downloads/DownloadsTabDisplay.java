package org.reactome.web.pwp.client.details.tabs.downloads;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.help.HelpPopupImage;
import org.reactome.web.pwp.client.details.common.help.InstanceTypeExplanation;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.details.tabs.downloads.widgets.DownloadGroupPanel;
import org.reactome.web.pwp.client.details.tabs.downloads.widgets.DownloadItem;
import org.reactome.web.pwp.client.details.tabs.downloads.widgets.DownloadType;
import org.reactome.web.pwp.client.details.tabs.downloads.widgets.DownloadURLBuilder;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DownloadsTabDisplay extends ResizeComposite implements DownloadsTab.Display {

    DownloadsTab.Presenter presenter;

    private String dbName = null;
    private AnalysisStatus analysisStatus = null;
    private String selected;
    private String flag;
    private String diagramProfile;
    private String analysisProfile;


    private DockLayoutPanel container;
    private DetailsTabTitle title;

    public DownloadsTabDisplay() {
        this.title =  this.getDetailTabType().getTitle();
        this.container = new DockLayoutPanel(Style.Unit.PX);
        this.container.addStyleName("elv-Download-Tab");
        initWidget(this.container);
    }

    @Override
    public DetailsTabType getDetailTabType() {
        return DetailsTabType.DOWNLOADS;
    }

    @Override
    public Widget getTitleContainer() {
        return title;
    }

    @Override
    public void setInitialState() {
        container.clear();
        this.container.add(getDetailTabType().getInitialStatePanel());
    }

    @Override
    public void setPresenter(DownloadsTab.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDetails(DatabaseObject databaseObject) {
        DockLayoutPanel aux = new DockLayoutPanel(Style.Unit.PX);

        FlowPanel titlePanel = new FlowPanel();
        titlePanel.add(getTitle(databaseObject));
        aux.addNorth(titlePanel, 33);

        DownloadGroupPanel formatGroup = new DownloadGroupPanel(
                "Pathway Format",
                RESOURCES.formatInfo().getText());
        DownloadGroupPanel diagramGroup = new DownloadGroupPanel(
                "Pathway Diagram",
                RESOURCES.diagramInfo().getText());

        for (DownloadType downloadType : DownloadType.values()) {
            List<String> urls  = new DownloadURLBuilder(downloadType, dbName, databaseObject, analysisStatus)
                                                                    .setDiagramProfile(diagramProfile)
                                                                    .setAnalysisProfile(analysisProfile)
                                                                    .setSelected(selected)
                                                                    .setFlagged(flag)
                                                                    .generateUrlList();

            DownloadItem dp = new DownloadItem(downloadType, urls);
            if (downloadType.getGroup() == DownloadType.Group.FORMAT) {
                formatGroup.insert(dp);
            } else if (downloadType.getGroup() == DownloadType.Group.DIAGRAM) {
                diagramGroup.insert(dp);
            }
        }

        FlowPanel flowPanel = new FlowPanel();
        flowPanel.add(formatGroup);
        flowPanel.add(diagramGroup);

        ScrollPanel sp = new ScrollPanel(flowPanel);
        sp.setStyleName("elv-Download-ItemsPanel");
        aux.add(sp);

        this.container.clear();
        this.container.add(aux);
    }

    @Override
    public void showLoadingMessage() {
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading download panel, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.container.clear();
        this.container.add(message);
    }

    private Widget getTitle(DatabaseObject databaseObject){
        HorizontalPanel titlePanel = new HorizontalPanel();
        titlePanel.setStyleName("elv-Download-Title");
        try{
            ImageResource img = databaseObject.getImageResource();
            String helpTitle = databaseObject.getSchemaClass().name;
            HTMLPanel helpContent = new HTMLPanel(InstanceTypeExplanation.getExplanation(databaseObject.getSchemaClass()));
            titlePanel.add(new HelpPopupImage(img, helpTitle, helpContent));
        }catch (Exception e){
            Console.error(e.getMessage());
            //ToDo: Look into new Error Handling
        }
        HTMLPanel title = new HTMLPanel(databaseObject.getDisplayName());
        title.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        titlePanel.add(title);

        return titlePanel;
    }

    @Override
    public void showErrorMessage(String message){
        HorizontalPanel panel = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.warning());
        panel.add(loader);

        Label label = new Label(message);
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        panel.add(label);

        this.container.clear();
        this.container.add(panel);
    }

    @Override
    public void setAnalysisStatus(AnalysisStatus status) {
        this.analysisStatus = status;
    }

    @Override
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public void setSelected(DatabaseObject selected) {
        this.selected = selected == null ?  null : selected.getStId();
    }

    @Override
    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public void setDiagramProfile(String profile) {
        this.diagramProfile = profile;
    }

    @Override
    public void setAnalysisProfile(String profile) {
        this.analysisProfile = profile;
    }

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
    }

    public interface Resources extends ClientBundle {

        @Source("widgets/images/info.png")
        ImageResource info();

        @Source("widgets/text/formatInfo.txt")
        TextResource formatInfo();

        @Source("widgets/text/diagramInfo.txt")
        TextResource diagramInfo();
    }
}
