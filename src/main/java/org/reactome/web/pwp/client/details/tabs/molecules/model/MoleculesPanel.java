package org.reactome.web.pwp.client.details.tabs.molecules.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.help.HelpPopup;
import org.reactome.web.pwp.client.details.common.help.HelpPopupImage;
import org.reactome.web.pwp.client.details.common.help.InstanceTypeExplanation;
import org.reactome.web.pwp.client.details.common.widgets.button.CustomButton;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosureImages;
import org.reactome.web.pwp.client.details.tabs.molecules.MoleculesTab;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.pwp.client.details.tabs.molecules.widget.MoleculesDownloadPanel;
import org.reactome.web.pwp.client.details.tabs.molecules.widget.MoleculesViewPanel;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.PhysicalEntity;
import org.reactome.web.pwp.model.client.classes.Species;

import java.util.List;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesPanel extends DockLayoutPanel implements MouseOverHandler, MouseOutHandler {

    private Result result;
    private final CustomButton downloadBtn = new CustomButton(CommonImages.INSTANCE.downloadFile(), "Download");
    private final CustomButton moleculeBtn = new CustomButton(CommonImages.INSTANCE.back(), "Molecule View");

    private HelpPopup popup;
    private DockLayoutPanel swapPanel;
    private final MoleculesViewPanel view;
    private final MoleculesDownloadPanel downloads;
    final HorizontalPanel buttonBar;
    private HorizontalPanel loadingPanel = new HorizontalPanel();

    public MoleculesPanel(final Result result, DatabaseObject databaseObject, MoleculesTab.Presenter presenter) {
        super(Style.Unit.PX);
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName("elv-Details-Tab");

        this.result = result;
        this.swapPanel = new DockLayoutPanel(Style.Unit.PX);
        this.view = new MoleculesViewPanel(result);
        this.downloads = new MoleculesDownloadPanel(result, presenter);

        //Creating TopBar with a "self made" ToggleButton for switching between Molecule and Download View.
        HorizontalPanel topBar = new HorizontalPanel();
        topBar.getElement().getStyle().setWidth(100, Style.Unit.PCT);

        HorizontalPanel leftBar = new HorizontalPanel();
        leftBar.add(getTitle(databaseObject));
        leftBar.add(getSpecies(databaseObject));

        buttonBar = new HorizontalPanel();

        //Setting two different messages for ToggleBtn
        downloadBtn.setTitle("Go to Download-View");
        moleculeBtn.setTitle("Go back to Molecules-View");

        //ClickHandler for DownloadBtn
        downloadBtn.addClickHandler(event -> {
            swapPanel.removeFromParent();
            downloads.initialise(result);
            swapPanel = downloads;

            add(swapPanel);
            buttonBar.clear();
            buttonBar.add(moleculeBtn);
        });

        //ClickHandler for MoleculeBtn
        moleculeBtn.addClickHandler(event -> {
            swapPanel.removeFromParent();
            view.update(result);
            swapPanel = view;

            add(swapPanel);
            buttonBar.clear();
            buttonBar.add(downloadBtn);
        });

        //Setting same style for both buttons
        downloadBtn.setStyleName("elv-Molecules-Button");
        moleculeBtn.setStyleName("elv-Molecules-Button");
        buttonBar.add(downloadBtn);
        buttonBar.setStyleName("elv-Molecules-ButtonBar");

        leftBar.add(buttonBar);
        buttonBar.getElement().getStyle().setFloat(Style.Float.LEFT);

        leftBar.add(loadingPanel);
        loadingPanel.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        topBar.add(leftBar);

        Widget info = getInfo();
        topBar.add(info);
        info.getElement().getStyle().setFloat(Style.Float.RIGHT);
        info.getElement().getStyle().setMarginRight(5, Style.Unit.PX);

        this.addNorth(topBar, 35);
        this.swapPanel = this.view;
        this.add(swapPanel);
    }

    /**
     * Get title for current position in pathway.
     * @param databaseObject currently selected entity
     * @return Widget titlePanel
     */
    private Widget getTitle(DatabaseObject databaseObject) {
        HorizontalPanel titlePanel = new HorizontalPanel();
        titlePanel.setStyleName("elv-Details-Title");
        try{
            ImageResource img = databaseObject.getImageResource();
            String helpTitle = databaseObject.getSchemaClass().name;
            HTMLPanel helpContent = new HTMLPanel(InstanceTypeExplanation.getExplanation(databaseObject.getSchemaClass()));
            titlePanel.add(new HelpPopupImage(img, helpTitle, helpContent));
        }catch (Exception e){
            Console.error(getClass() + ": " + e.getMessage());
            //ToDo: Enough?
        }
        HTMLPanel title = new HTMLPanel(databaseObject.getDisplayName());
        title.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        titlePanel.add(title);

        return titlePanel;
    }

    /**
     * Get currently selected species.
     * @param databaseObject currently selected entity
     * @return Widget speciesPanel
     */
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

    /**
     * Create and get infoPanel with a short instruction on how to use the Molecule Tab
     * @return Widget infoPanel
     */
    private Widget getInfo() {
        FocusPanel infoPanel = new FocusPanel();
        infoPanel.setStyleName("elv-Molecules-InfoPanel");
        HorizontalPanel content = new HorizontalPanel();
        try{
            Image img = new Image(CommonImages.INSTANCE.information());
            String helpTitle = "Info";
            HTMLPanel helpContent = new HTMLPanel(
                    "The molecules tab shows you all the molecules of a complete pathway diagram.\n" +
                    "Molecules are grouped in Chemical Compounds, Proteins, Sequences and Others.\n" +
                    "The molecules of a selected object appear highlighted in the molecules lists;\n" +
                    "a molecule selected in the list will be highlighted in the diagram.\n" +
                    "For each molecule you can see a symbol, a link to the main reference DB, a name and the number of\n" +
                    "occurrences in the pathway. Clicking on the symbol several times will allow you to circle through\n" +
                    "all its occurrences in the diagram.\n" +
                    "Expanding by clicking on the '+' will provide you with further external links.\n" +
                    "Lists can be downloaded. Just click on the button in the top right\n" +
                    "corner, select the fields and types you are interested in and click 'Start Download'.");

            content.add(img);
            popup = new HelpPopup(helpTitle, helpContent);
            infoPanel.addMouseOverHandler(this);
            infoPanel.addMouseOutHandler(this);
            infoPanel.getElement().getStyle().setProperty("cursor", "help");
        }catch (Exception e){
//            e.printStackTrace();
            Console.error(getClass() + ": " + e.getMessage());
            //ToDo: Enough?
        }
        HTMLPanel title = new HTMLPanel("Info");
        title.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        content.add(title);

        infoPanel.add(content.asWidget());

        return infoPanel;
    }

    /**
     * Gets a panel with loading message and symbol.
     * @return Widget
     */
    private static Widget getLoadingMessage(String msg){
        HorizontalPanel hp = new HorizontalPanel();


        if(msg == null || msg.isEmpty()){
            hp.add(new Image(DisclosureImages.INSTANCE.getLoadingImage()));
            hp.add(new HTMLPanel("Please wait while the data for selection is retrieved..."));
        }else{
            hp.add(new Image(CommonImages.INSTANCE.exclamation()));
            hp.add(new HTMLPanel(msg));
        }

        hp.setSpacing(5);

        return hp;
    }

    /**
     * Sets loading Message when the Diagram has to select an entity triggered by clicking on a Molecule's button
     */
    public void setLoadingPanel(String msg){
        this.loadingPanel.add(getLoadingMessage(msg));
    }

    /**
     * Removes the Message that appears after a Molecule's button was clicked
     */
    public void clearLoadingPanel(){
        this.loadingPanel.clear();
    }

    /**
     * Get number of loaded molecules.
     * @return int numberOfLoadedMolecules
     */
    public Integer getNumberOfLoadedMolecules() {
        if(result == null){
            return 0;
        }
        return result.getNumberOfMolecules();
    }

    /**
     * Get number of highlighted molecules.
     * @return int numberOfHighlightedMolecules
     */
    public Integer getNumberOfHighlightedMolecules() {
        return result.getNumberOfHighlightedMolecules();
    }

    /**
     * Update avoids loading if Pathway-with-Diagram stays the same.
     * @param result updated version of result
     */
    public void update(Result result) {
        this.result = result;
        this.view.update(result);
        this.downloads.update(result);
    }

    /**
     *OnMouseOver lets infoPopup appear.
     * @param event MouseOverEvent
     */
    @Override
    public void onMouseOver(MouseOverEvent event) {
        popup.setPositionAndShow(event);
    }

    /**
     * OnMouseOut makes infoPopup disappear.
     * @param event MouseOutEvent
     */
    @Override
    public void onMouseOut(MouseOutEvent event) {
        popup.hide(true);
    }

    /**
     * Used if DownloadTabs requires MoleculesDownload
     */
    public void moleculesDownloadRequired() {
        swapPanel.removeFromParent();
        downloads.initialise(result);
        swapPanel = downloads;

        add(swapPanel);
        buttonBar.clear();
        buttonBar.add(moleculeBtn);
    }
}