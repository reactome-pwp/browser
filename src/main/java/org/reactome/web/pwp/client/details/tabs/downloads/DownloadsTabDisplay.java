package org.reactome.web.pwp.client.details.tabs.downloads;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.help.HelpPopupImage;
import org.reactome.web.pwp.client.details.common.help.InstanceTypeExplanation;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.details.tabs.downloads.widgets.DownloadType;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DownloadsTabDisplay extends ResizeComposite implements DownloadsTab.Display {

    DownloadsTab.Presenter presenter;

    private String dbName = null;
    private AnalysisStatus analysisStatus = null;
    private String selected;
    private String flag;

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
//        this.showWaitingMessage();

        //To continue ahead the dbName is needed. If it is null, the presenter should still
        //be trying to retrieve that from the db
//        if(this.dbName==null){
//            (new Timer() {
//                @Override
//                public void run() {
//                    showInstanceDetailsIfExists(pathway, databaseObject);
//                }
//            }).schedule(250);
//            return;
//        }

        DockLayoutPanel aux = new DockLayoutPanel(Style.Unit.PX);

        FlowPanel titlePanel = new FlowPanel();
        titlePanel.add(getTitle(databaseObject));
        aux.addNorth(titlePanel, 33);

        //Molecules Download
        FlowPanel flowPanel = new FlowPanel();
        for (DownloadType downloadType : DownloadType.values()) {
            Anchor dp = getDownloadAnchor(this.dbName, downloadType, databaseObject, this.analysisStatus, this.selected, this.flag);
            dp.setStyleName(RESOURCES.getCSS().downloadItem());
            flowPanel.add(dp);
        }

        Widget explanation = getExplanation();
        flowPanel.add(explanation);

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

    private Anchor getDownloadAnchor(final String dbName,
                                     final DownloadType type,
                                     final DatabaseObject databaseObject,
                                     final AnalysisStatus status,
                                     final String selected,
                                     final String flag) {
        SafeHtml image = SafeHtmlUtils.fromSafeConstant(new Image(type.getIcon()).toString());
        Anchor rtn = new Anchor(image, type.getUrl(dbName, databaseObject, status, selected, flag), "_blank");
        rtn.addStyleName("elv-Download-Item");
        rtn.setTitle("View/download in " + type.getTooltip() + " format");
        return rtn;
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

    private Widget getExplanation(){
        FlowPanel explanation = new FlowPanel();
        explanation.add(new Image(RESOURCES.info()));
        explanation.add(new Label("The download options above are for the selected pathway, " +
                "not individual events or entities selected in it."));
        explanation.setStyleName(RESOURCES.getCSS().explanationPanel());
        return explanation;
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

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("widgets/images/info.png")
        ImageResource info();

    }

    @CssResource.ImportedWithPrefix("diagram-DownloadsTabDisplay")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/details/tabs/downloads/DownloadsTabDisplay.css";

        String explanationPanel();

        String downloadItem();
    }
}
