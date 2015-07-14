package org.reactome.web.pwp.client.details.delegates;


import org.reactome.web.pwp.client.details.tabs.molecules.model.data.PhysicalToReferenceEntityMap;

import java.util.List;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculeSelectedListener {

    private static MoleculeSelectedListener moleculeSelectedListener;
    private MoleculeSelectedHandler handler;

    protected MoleculeSelectedListener(){}

    public static MoleculeSelectedListener getMoleculeSelectedListener(){
        if(moleculeSelectedListener==null)
            moleculeSelectedListener = new MoleculeSelectedListener();
        return moleculeSelectedListener;
    }

    public void moleculeSelected(List<PhysicalToReferenceEntityMap> physicalEntityList){
        this.handler.moleculeSelected(physicalEntityList);
    }

    public void setMoleculeSelectedHandler(MoleculeSelectedHandler handler) {
        this.handler = handler;
    }
}