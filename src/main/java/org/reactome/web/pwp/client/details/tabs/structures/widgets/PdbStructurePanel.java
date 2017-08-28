package org.reactome.web.pwp.client.details.tabs.structures.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.reactome.web.pwp.client.details.tabs.structures.events.StructureLoadedEvent;
import org.reactome.web.pwp.client.details.tabs.structures.handlers.StructureLoadedHandler;
import org.reactome.web.pwp.model.client.classes.ReferenceSequence;
import uk.ac.ebi.pwp.widgets.pdb.events.PdbStructureLoadedEvent;
import uk.ac.ebi.pwp.widgets.pdb.handlers.PdbStructureLoadedHandler;
import uk.ac.ebi.pwp.widgets.pdb.ui.PDBViewer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PdbStructurePanel extends StructuresPanel<ReferenceSequence> implements PdbStructureLoadedHandler {
    private Map<ReferenceSequence, PDBViewer> proteinAccessionViewerMap = new HashMap<>();

    public void add(ReferenceSequence referenceSequence) {
        if(this.proteinAccessionViewerMap.keySet().isEmpty()){
            this.container.clear();
        }

        if(!this.proteinAccessionViewerMap.keySet().contains(referenceSequence)){
            PDBViewer viewer = new PDBViewer(referenceSequence.getIdentifier(), referenceSequence.getDisplayName());
            viewer.getElement().getStyle().setMarginBottom(15, Style.Unit.PX);
            viewer.addStructureLoadedHandler(this);
            this.proteinAccessionViewerMap.put(referenceSequence, viewer);
            this.container.add(viewer);
            this.structuresRequired++;
        }
    }

    public HandlerRegistration addStructureLoadedHandler(StructureLoadedHandler handler){
        return addHandler(handler, StructureLoadedEvent.TYPE);
    }

    @Override
    public void onPdbStructureLoaded(PdbStructureLoadedEvent pdbStructureLoadedEvent) {
        this.structuresLoaded++;
        fireEvent(new StructureLoadedEvent());
    }

    public void setEmpty() {
        this.container.clear();
        this.container.add(new HTMLPanel("Object does not contains proteins to show structures"));
    }
}