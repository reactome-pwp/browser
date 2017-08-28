package org.reactome.web.pwp.client.details.tabs.structures.widgets;

import com.google.gwt.user.client.ui.HTMLPanel;
import org.reactome.web.pwp.client.details.tabs.structures.events.StructureLoadedEvent;
import org.reactome.web.pwp.model.client.classes.ReferenceMolecule;
import org.reactome.web.pwp.model.client.classes.SimpleEntity;
import uk.ac.ebi.pwp.widgets.chebi.client.ChEBIViewer;
import uk.ac.ebi.pwp.widgets.chebi.events.ChEBIChemicalLoadedEvent;
import uk.ac.ebi.pwp.widgets.chebi.handlers.ChEBIChemicalLoadedHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ChEBIStructuresPanel extends StructuresPanel<SimpleEntity> implements ChEBIChemicalLoadedHandler {
    private Map<SimpleEntity, ChEBIViewer> eventViewerMap = new HashMap<SimpleEntity, ChEBIViewer>();

    @Override
    public void add(SimpleEntity element) {
        if (eventViewerMap.keySet().isEmpty()) {
            this.container.clear();
        }

        int chebiRefs = 0;
        if (!this.eventViewerMap.keySet().contains(element)) {
            ReferenceMolecule referenceMolecule = element.getReferenceEntity();
            String ref = referenceMolecule.getDisplayName();
            String chEBI = ref.substring(ref.indexOf("[") + 1, ref.indexOf("]"));
            String[] aux = chEBI.split(":");
            if (aux[0].toLowerCase().equals("chebi")) {
                ChEBIViewer chEBIViewer = new ChEBIViewer(aux[1]);
                chEBIViewer.addChEBIChemicalLoadedHandler(this);
                this.eventViewerMap.put(element, chEBIViewer);
                this.structuresRequired++;
                this.container.add(chEBIViewer);
                chebiRefs++;
            }
        }

        if (chebiRefs == 0) {
            this.setEmpty();
        }
    }

    @Override
    public void setEmpty() {
        this.container.clear();
        this.container.add(new HTMLPanel("This entity does not contain chEBI structures to be shown"));
    }

    @Override
    public void onChEBIChemicalLoaded(ChEBIChemicalLoadedEvent event) {
        this.structuresLoaded++;
        fireEvent(new StructureLoadedEvent());
    }
}
