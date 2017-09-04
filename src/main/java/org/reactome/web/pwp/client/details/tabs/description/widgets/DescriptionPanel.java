package org.reactome.web.pwp.client.details.tabs.description.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.help.HelpPopupImage;
import org.reactome.web.pwp.client.details.common.help.InstanceTypeExplanation;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.OverviewTableFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.PhysicalEntity;
import org.reactome.web.pwp.model.client.classes.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DescriptionPanel extends DockLayoutPanel {

    public DescriptionPanel(DatabaseObject databaseObject) {
        super(Style.Unit.PX);
        addStyleName("elv-Details-Tab");

        HorizontalPanel topBar = new HorizontalPanel();
        topBar.add(getTitle(databaseObject));
        if (databaseObject.getStId()!=null) {
            topBar.add(getStableId(databaseObject));
        }
        topBar.add(getSpecies(databaseObject));
        addNorth(topBar, 35);

        DockLayoutPanel overview = new DockLayoutPanel(Style.Unit.EM);
        overview.addStyleName("elv-Details-OverviewPanel");

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
//        vp.getElement().getStyle().setPaddingTop(5, Style.Unit.PX);
        vp.getElement().getStyle().setPaddingBottom(25, Style.Unit.PX);

        vp.add(OverviewTableFactory.getOverviewTable(databaseObject));

        ScrollPanel scrollPanel = new ScrollPanel(vp);
        scrollPanel.setWidth("100%");
        overview.add(scrollPanel);

        add(overview);
    }

    private Widget getTitle(DatabaseObject databaseObject){
        HorizontalPanel titlePanel = new HorizontalPanel();
        titlePanel.setStyleName("elv-Details-Title");
        try{
            ImageResource img = databaseObject.getImageResource();
            String helpTitle = databaseObject.getSchemaClass().name;
            HTMLPanel helpContent = new HTMLPanel(InstanceTypeExplanation.getExplanation(databaseObject.getSchemaClass()));
            titlePanel.add(new HelpPopupImage(img, helpTitle, helpContent));
        }catch (Exception e){
            Console.error(getClass() + ": " + e.getMessage());
            //ToDo: Look into new Error Handling
        }
        HTMLPanel title = new HTMLPanel(databaseObject.getDisplayName());
        title.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        title.setTitle(databaseObject.getDisplayName());
        titlePanel.add(title);

        return titlePanel;
    }

    private Widget getSpecies(DatabaseObject databaseObject){
        String species = null;
        if(databaseObject instanceof PhysicalEntity){
            List<Species> speciesList = ((PhysicalEntity) databaseObject).getSpecies();
            if(!speciesList.isEmpty()){
                species = speciesList.get(0).getDisplayName();
            }
        }else if(databaseObject instanceof Event){
            Event event = (Event) databaseObject;
            if(!event.getSpecies().isEmpty()){
                species = event.getSpecies().get(0).getDisplayName();
            }
        }

        HorizontalPanel speciesPanel = new HorizontalPanel();
        if(species!=null){
            speciesPanel.setStyleName("elv-Details-Species");
            speciesPanel.add(new HTMLPanel("Species: " + species));
        }
        return speciesPanel;
    }

    private Widget getStableId(DatabaseObject databaseObject){
        String stId = databaseObject.getStId();
        Anchor link = new Anchor(stId, "/cgi-bin/control_panel_st_id?ST_ID=" + stId);
        link.setTarget("_blank");
        link.setTitle("Go to REACTOME control panel for " + stId);

        HorizontalPanel stIdPanel = new HorizontalPanel();
        stIdPanel.setStyleName("elv-Details-StId");
        stIdPanel.add(new HTMLPanel("Id: "));
        stIdPanel.add(link);
        return stIdPanel;
    }
}
