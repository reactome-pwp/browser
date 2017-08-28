package org.reactome.web.pwp.client.details.tabs.structures.widgets;

import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.client.details.tabs.structures.events.StructureLoadedEvent;
import org.reactome.web.pwp.model.client.classes.*;
import uk.ac.ebi.pwp.widgets.chebi.client.ChEBIViewer;
import uk.ac.ebi.pwp.widgets.chebi.events.ChEBIChemicalLoadedEvent;
import uk.ac.ebi.pwp.widgets.chebi.handlers.ChEBIChemicalLoadedHandler;
import uk.ac.ebi.pwp.widgets.pdb.events.PdbStructureLoadedEvent;
import uk.ac.ebi.pwp.widgets.pdb.handlers.PdbStructureLoadedHandler;
import uk.ac.ebi.pwp.widgets.pdb.ui.PDBViewer;
import uk.ac.ebi.pwp.widgets.rhea.client.RheaViewer;
import uk.ac.ebi.pwp.widgets.rhea.events.ReactionStructureLoadedEvent;
import uk.ac.ebi.pwp.widgets.rhea.handlers.ReactionStructureLoadedHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class RheaStructuresPanel extends StructuresPanel<Event> implements ReactionStructureLoadedHandler, PdbStructureLoadedHandler, ChEBIChemicalLoadedHandler {
    private Map<Event, RheaViewer> eventViewerMap = new HashMap<>();

    private VerticalPanel rheaPanels = new VerticalPanel();
    private VerticalPanel pdbPanels = new VerticalPanel();
    private VerticalPanel chebiPanels = new VerticalPanel();

    public RheaStructuresPanel() {
        this.container.clear();
        rheaPanels.setWidth("100%");
        this.container.add(rheaPanels);

        pdbPanels.setWidth("100%");
        this.container.add(pdbPanels);

        chebiPanels.setWidth("100%");
        this.container.add(chebiPanels);
    }

    @Override
    public void add(Event element) {

        int rheaRefs = 0;
        if(!this.eventViewerMap.keySet().contains(element)){
            for (DatabaseIdentifier databaseIdentifier : element.getCrossReference()) {
                String[] aux = databaseIdentifier.getDisplayName().split(":");
                if(aux[0].toLowerCase().equals("rhea")){
                    RheaViewer rheaPanel = new RheaViewer(aux[1]);
                    rheaPanel.addStructureLoadedHandler(this);
                    this.eventViewerMap.put(element, rheaPanel);
                    this.structuresRequired++;
                    this.rheaPanels.add(rheaPanel);
                    rheaRefs++;
                }
            }
        }

        if(rheaRefs==0){
            this.setEmpty();
        }
    }

    public void add(ReferenceEntity ref){
        if(ref instanceof ReferenceSequence){
            PDBViewer viewer = new PDBViewer(ref.getIdentifier(), ref.getDisplayName());
            this.structuresRequired++;
            viewer.addStructureLoadedHandler(this);
            this.pdbPanels.add(viewer);
        }else if(ref instanceof ReferenceMolecule){
            ChEBIViewer viewer = new ChEBIViewer(ref.getIdentifier());
            this.structuresRequired++;
            viewer.addChEBIChemicalLoadedHandler(this);
            this.chebiPanels.add(viewer);

        }
    }

    @Override
    public void onReactionStructureLoaded(ReactionStructureLoadedEvent event) {
        this.structuresLoaded++;
        fireEvent(new StructureLoadedEvent());
    }

    @Override
    public void setEmpty(){
        this.rheaPanels.clear();
//        HTMLPanel emptyMessage = new HTMLPanel("This event does not contain Rhea structures to be shown");
//        this.rheaPanels.add(emptyMessage);
    }

    @Override
    public void onPdbStructureLoaded(PdbStructureLoadedEvent pdbStructureLoadedEvent) {
        this.structuresLoaded++;
        fireEvent(new StructureLoadedEvent());
    }

    @Override
    public void onChEBIChemicalLoaded(ChEBIChemicalLoadedEvent event) {
        this.structuresLoaded++;
        fireEvent(new StructureLoadedEvent());
    }
}