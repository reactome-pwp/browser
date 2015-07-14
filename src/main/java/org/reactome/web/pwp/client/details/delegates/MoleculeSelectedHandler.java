package org.reactome.web.pwp.client.details.delegates;

import org.reactome.web.pwp.client.details.tabs.molecules.model.data.PhysicalToReferenceEntityMap;

import java.util.List;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public interface MoleculeSelectedHandler {

    void moleculeSelected(List<PhysicalToReferenceEntityMap> physicalEntityList);
}