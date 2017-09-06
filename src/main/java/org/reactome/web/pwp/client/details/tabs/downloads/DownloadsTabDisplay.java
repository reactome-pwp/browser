package org.reactome.web.pwp.client.details.tabs.downloads;

import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.help.HelpPopupImage;
import org.reactome.web.pwp.client.details.common.help.InstanceTypeExplanation;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.details.tabs.downloads.widgets.DownloadPanel;
import org.reactome.web.pwp.client.details.tabs.downloads.widgets.DownloadType;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DownloadsTabDisplay extends ResizeComposite implements DownloadsTab.Display {

    DownloadsTab.Presenter presenter;

    private String dbName = null;
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

        VerticalPanel vp = new VerticalPanel();
        vp.add(getTitle(databaseObject));
        vp.add(getExplanation());
        aux.addNorth(vp, 78);

        FlowPanel flowPanel = new FlowPanel();
        for (DownloadType downloadType : DownloadType.values()) {
            DownloadPanel dp = new DownloadPanel(this.dbName, downloadType, databaseObject);
            dp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
            dp.getElement().getStyle().setMarginRight(30, Style.Unit.PX);
            dp.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
            dp.getElement().getStyle().setMarginBottom(15, Style.Unit.PX);
            flowPanel.add(dp);
        }

        //Molecules Download
//        DownloadMolecule dm = new DownloadMolecule(this.presenter, databaseObject);
//        dm.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
//        dm.getElement().getStyle().setMarginRight(30, Style.Unit.PX);
//        dm.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
//        dm.getElement().getStyle().setMarginBottom(15, Style.Unit.PX);
//        flowPanel.add(dm);

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

    private Widget getExplanation(){
        HTMLPanel explanation = new HTMLPanel("The download options below are for the selected pathway, " +
                "not individual events or entities selected in it.");
        explanation.setStyleName("elv-Download-Explanation");
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
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
