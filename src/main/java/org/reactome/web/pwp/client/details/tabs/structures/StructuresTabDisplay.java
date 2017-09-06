package org.reactome.web.pwp.client.details.tabs.structures;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.details.tabs.structures.events.StructureLoadedEvent;
import org.reactome.web.pwp.client.details.tabs.structures.handlers.StructureLoadedHandler;
import org.reactome.web.pwp.client.details.tabs.structures.widgets.*;
import org.reactome.web.pwp.model.client.classes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StructuresTabDisplay extends ResizeComposite implements StructuresTab.Display, StructureLoadedHandler {

    private StructuresTab.Presenter presenter;

    private DockLayoutPanel container;
    private DetailsTabTitle title;

    private Map<DatabaseObject, StructuresPanel> cache = new HashMap<>();
    private StructuresPanel currentPanel;

    public StructuresTabDisplay() {
        this.title = getDetailTabType().getTitle();
        this.container = new DockLayoutPanel(Style.Unit.EM);
        initWidget(this.container);
        setInitialState();
    }

    @Override
    public DetailsTabType getDetailTabType() {
        return DetailsTabType.STRUCTURES;
    }

    @Override
    public Widget getTitleContainer() {
        return this.title;
    }

    @Override
    public void setPresenter(StructuresTab.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStructureLoaded(StructureLoadedEvent structureLoadedEvent) {
        StructuresPanel panel = (StructuresPanel) structureLoadedEvent.getSource();
        if(this.currentPanel==panel){
            this.setTitle(panel);
        }
    }

    @Override
    public void showDetails(DatabaseObject databaseObject) {
        if(cache.containsKey(databaseObject)){
            this.currentPanel = cache.get(databaseObject);
            this.container.clear();
            showStructuresPanel(this.currentPanel);
        }else{
            if(databaseObject instanceof SimpleEntity){
                ChEBIStructuresPanel p = new ChEBIStructuresPanel();
                this.currentPanel = p;
                p.addStructureLoadedHandler(this);
                p.add((SimpleEntity) databaseObject);
            }else if(databaseObject instanceof PhysicalEntity){
                PdbStructurePanel p = new PdbStructurePanel();
                this.currentPanel = p;
                p.addStructureLoadedHandler(this);
                this.presenter.getReferenceSequences(databaseObject);
            } else if(databaseObject instanceof ReactionLikeEvent) {
                RheaStructuresPanel p = new RheaStructuresPanel();
                this.currentPanel = p;
                p.addStructureLoadedHandler(this);
                p.add((Event) databaseObject);
                this.presenter.getReferenceSequences(databaseObject);
            } else {
                this.currentPanel = new EmptyStructuresPanel();
            }
            this.showStructuresPanel(this.currentPanel);
            this.cache.put(databaseObject, this.currentPanel);
        }
    }

    @Override
    public void showReferenceSequences(DatabaseObject databaseObject, List<ReferenceEntity> referenceSequenceList) {
        StructuresPanel panel = this.cache.get(databaseObject);
        if(panel instanceof PdbStructurePanel){
            PdbStructurePanel aux = (PdbStructurePanel) panel;
            if(referenceSequenceList.isEmpty()){
                aux.setEmpty();
                return;
            }
            for (ReferenceEntity ref : referenceSequenceList) {
                if(ref instanceof ReferenceSequence) {
                    aux.add((ReferenceSequence )ref);
                }
            }
        }
        else if(panel instanceof RheaStructuresPanel){
            RheaStructuresPanel aux = (RheaStructuresPanel) panel;
            for (ReferenceEntity ref : referenceSequenceList) {
                aux.add(ref);
            }
        }
    }

    @Override
    public void updateTitle(DatabaseObject databaseObject) {
        if(this.cache.containsKey(databaseObject)){
            this.setTitle(this.cache.get(databaseObject));
        }else{
            this.title.resetCounter();
        }
    }

    @Override
    public void setInitialState() {
        this.container.clear();
        this.container.add(getDetailTabType().getInitialStatePanel());
    }

    @Override
    public void showLoadingMessage() {
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading the data required to show the structures data. Please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.container.clear();
        this.container.add(message);
    }

    @Override
    public void showErrorMessage(String message) {
        HorizontalPanel panel = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.warning());
        panel.add(loader);

        Label label = new Label(message);
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        panel.add(label);

        this.container.clear();
        this.container.add(panel);
    }

    private void showStructuresPanel(StructuresPanel structuresPanel){
        this.container.clear();
        this.container.add(structuresPanel);
        this.setTitle(structuresPanel);
    }

    private void setTitle(StructuresPanel panel){
        int loadedStructures = panel.getNumberOfLoadedStructures();
        int proteinAccessions = panel.getNumberOfRequiredStructures();
        String counter;
        if(loadedStructures==0 && proteinAccessions==0){
            counter = "0";
        }else{
            counter = loadedStructures + "/" + proteinAccessions;
        }
        this.title.setCounter(counter);
    }
}
