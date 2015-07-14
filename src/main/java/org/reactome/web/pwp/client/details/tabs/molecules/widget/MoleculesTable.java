package org.reactome.web.pwp.client.details.tabs.molecules.widget;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.common.widgets.panels.MoleculePanel;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Molecule;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.PhysicalToReferenceEntityMap;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
class MoleculesTable implements IsWidget  {
    private VerticalPanel vp;
    private final Result result;
    private final HashMap<Long, MoleculePanel> display = new HashMap<>();

    public MoleculesTable(Result result) {
        this.result = result;
        vp = new VerticalPanel();
        vp.setWidth("99%");
    }

    /**
     * Setting Molecules Data.
     * @param molecules to be displayed
     */
    public void setMoleculesData(ArrayList<Molecule> molecules){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("99%");
        for (Molecule molecule : molecules) {
            Set<PhysicalToReferenceEntityMap> phyEntities = getPhysicalEntities(molecule);
            MoleculePanel moleculePanel = new MoleculePanel(molecule, phyEntities);
            if(!molecule.isToHighlight()){
                moleculePanel.addStyleName("elv-Details-MoleculesRow-undoHighlight");
            }
            display.put(molecule.getDbId(), moleculePanel);
            vp.add(moleculePanel);
        }
        this.vp.clear();
        this.vp = vp;
    }

    /**
     * Updating Molecules Data.
     * @param molecules to be displayed
     */
    public void updateMoleculesData(ArrayList<Molecule> molecules){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("99%");
        for (Molecule molecule : molecules) {
            MoleculePanel moleculePanel = display.get(molecule.getDbId());
            if(!molecule.isToHighlight()){
                moleculePanel.addStyleName("elv-Details-MoleculesRow-undoHighlight");
            }else{
                //remove undoHighlight but keep all the other style information
                moleculePanel.removeStyleName("elv-Details-MoleculesRow-undoHighlight");
            }
            vp.add(moleculePanel);
        }
        this.vp.clear();
        this.vp = vp;
    }

    /**
     * Getting all PhysicalEntities in current Pathway to which the referenceEntity of a Molecule refers to.
     * @param molecule for which PhysicalEntities are required.
     * @return Set<PhysicalToReferenceEntityMap>
     */
    private Set<PhysicalToReferenceEntityMap> getPhysicalEntities(Molecule molecule) {
        return this.result.getPhyEntityToRefEntitySet().getKeys(molecule);
    }

    /**
     * Get widget of MoleculesTable.
     * @return Widget vp
     */
    @Override
    public Widget asWidget() {
        return vp;
    }
}