package org.reactome.web.pwp.client.details.tabs.molecules;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.utils.LRUCache;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.details.tabs.molecules.model.MoleculesPanel;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesTabDisplay extends ResizeComposite implements MoleculesTab.Display {

    private MoleculesTab.Presenter presenter;

    private DockLayoutPanel container;
    private DetailsTabTitle title;

    private boolean download = false;
    private final LRUCache<Long, MoleculesPanel> panelsLoadedForPathways = new LRUCache<>();

    private MoleculesPanel currentPanel;
    private DatabaseObject toShow;
    private DatabaseObject pathwayDiagram;

    public MoleculesTabDisplay() {
        this.title = getDetailTabType().getTitle();
        this.container = new DockLayoutPanel(Style.Unit.EM);
        initWidget(this.container);
        setInitialState();
    }

    @Override
    public DetailsTabType getDetailTabType() {
        return DetailsTabType.PARTICIPATING_MOLECULES;
    }

    @Override
    public Widget getTitleContainer() {
        return this.title;
    }

    @Override
    public void setInitialState() {
        this.title.resetCounter();

        this.container.clear();
        this.container.add(getDetailTabType().getInitialStatePanel());
    }

    @Override
    public void setPresenter(MoleculesTab.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDetails(Pathway pathway, DatabaseObject databaseObject){
        toShow = databaseObject != null ? databaseObject : pathway;
        pathwayDiagram = pathway;

        if (!this.getPathwayDetailsIfExist(pathway)){
            showWaitingMessage();
            if(toShow==null) return;
            this.presenter.getMoleculesData();
        }else{
            this.presenter.updateMoleculesData();
        }
    }

    @Override
    public void updateDetailsIfLoaded(Pathway pathway, DatabaseObject databaseObject) {
//        toShow = databaseObject != null ? databaseObject : pathway;
//        pathwayDiagram = pathway;
//        if (this.getPathwayDetailsIfExist(pathway)){
//            this.presenter.updateMoleculesData();
//        }else{
//            this.titleContainer.setText(getDetailTabType().getTitle());
//        }
    }

    @Override
    public void showLoadingMessage() {

    }

    /**
     * Refreshing the title of MoleculesTab if number of loaded/highlighted molecules has changed.
     * @param highlightedMolecules Number of highlighted molecules.
     * @param loadedMolecules Number of loaded molecules.
     */
    @Override
    public void refreshTitle(Integer highlightedMolecules, Integer loadedMolecules){
        String aux = null;
        if(loadedMolecules == null){
            aux = "";
        }else if(loadedMolecules==0){
            aux = " (0)";
        }else if(highlightedMolecules > 0){
            if(highlightedMolecules.equals(loadedMolecules)){
                aux = loadedMolecules.toString();
            }else{
                aux = highlightedMolecules + "/" + loadedMolecules;
            }
        }

        if(aux != null){
            this.title.setCounter(aux);
        }
    }

    /**
     * Get pathway details if they have already been loaded.
     * @param pathway that might have been loaded already.
     * @return details exist (true) or don't exist yet (false)
     */
    private boolean getPathwayDetailsIfExist(Pathway pathway){
        if(this.panelsLoadedForPathways.containsKey(pathway.getDbId())){
            this.currentPanel = this.panelsLoadedForPathways.get(pathway.getDbId());
            return true;
        }
        return false;
    }

//    /**
//     * Setter for currentPanel, otherwise nullptr exception for unloaded pathways in case
//     * DownloadTab requires MoleculesDownload.
//     * @param pathway current pathway
//     * @param download true if DownloadView is required
//     */
//    @Override
//    public void setCurrentPanel(Pathway pathway, boolean download){
//        this.download = download;
//        if(currentPanel == null){
//            currentPanel = new MoleculesPanel(null, pathway, this.presenter);
//        }
//    }

    /**
     * Used if DownloadTab requires MoleculesDownload.
     */
    @Override
    public void moleculesDownloadRequired() {
        download = false;
        currentPanel.moleculesDownloadRequired();
    }

    /**
     * Create and set data of a new MoleculesPanel.
     * @param result new result, to be set
     */
    @Override
    public void setMoleculesData(Result result) {
        this.currentPanel = new MoleculesPanel(result, this.toShow, this.presenter);
        showMoleculesPanel(this.currentPanel);
        if(download){
            this.moleculesDownloadRequired();
        }
    }

    /**
     * Set updated data for a MoleculesPanel.
     * @param result updated result
     */
    @Override
    public void updateMoleculesData(Result result) {
        this.currentPanel.update(result);
        showMoleculesPanel(currentPanel);
        if(download){
            this.moleculesDownloadRequired();
        }
    }

    /**
     * Clear current view, show new one, update title of tab and add loaded panel to cache.
     * @param panel new view, to be displayed
     */
    private void showMoleculesPanel(MoleculesPanel panel) {
        this.container.clear(); //in case a different result is currently shown
        this.container.add(panel);

        this.refreshTitle(panel.getNumberOfHighlightedMolecules(), panel.getNumberOfLoadedMolecules());

        this.panelsLoadedForPathways.put(this.pathwayDiagram.getDbId(), this.currentPanel);
    }

    @SuppressWarnings("UnusedDeclaration")
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

    private void showWaitingMessage(){
        this.currentPanel = null;

        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading data for participating molecules, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.container.clear();
        this.container.add(message);
    }

    /**
     * Sets loading Message when the Diagram has to select an entity triggered by clicking on a Molecule's button
     */
    public void setLoadingMsg(String msg){
        currentPanel.clearLoadingPanel();
        currentPanel.setLoadingPanel(msg);
    }

    /**
     * Removes the Message that appears after a Molecule's button was clicked
     */
    public void clearLoadingMsg(){
        currentPanel.clearLoadingPanel();
    }

}
