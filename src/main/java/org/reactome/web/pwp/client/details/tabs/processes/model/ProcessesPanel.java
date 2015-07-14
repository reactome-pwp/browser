package org.reactome.web.pwp.client.details.tabs.processes.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.help.HelpPopupImage;
import org.reactome.web.pwp.client.details.help.InstanceTypeExplanation;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.classes.PhysicalEntity;
import org.reactome.web.pwp.model.classes.Species;
import org.reactome.web.pwp.client.details.tabs.processes.model.widgets.factory.ProcessesTableFactory;


import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ProcessesPanel extends DockLayoutPanel {

    public ProcessesPanel(DatabaseObject databaseObject) {
        super(Style.Unit.PX);
        addStyleName("elv-Details-Tab");

        HorizontalPanel topBar = new HorizontalPanel();
        topBar.add(getTitle(databaseObject));
        topBar.add(getSpecies(databaseObject));
        addNorth(topBar, 35);

        DockLayoutPanel overview = new DockLayoutPanel(Style.Unit.EM);
        overview.addStyleName("elv-Details-OverviewPanel");

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setPaddingTop(5, Style.Unit.PX);
        vp.getElement().getStyle().setPaddingBottom(25, Style.Unit.PX);

        vp.add(ProcessesTableFactory.getOverviewTable(databaseObject));

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
            //ToDo: Look into new Error Handling
            e.printStackTrace();
        }
        HTMLPanel title = new HTMLPanel(databaseObject.getDisplayName());
        title.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
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
            List<Species> speciesList = ((Event) databaseObject).getSpecies();
            if(!speciesList.isEmpty()){
                species = speciesList.get(0).getDisplayName();
            }
        }

        HorizontalPanel speciesPanel = new HorizontalPanel();
        if(species!=null){
            speciesPanel.setStyleName("elv-Details-Species");
            speciesPanel.add(new HTMLPanel("Species: " + species));
        }
        return speciesPanel;
    }
}
