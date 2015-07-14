package org.reactome.web.pwp.client.details.tabs.processes.model.widgets.container;

import org.reactome.web.pwp.client.details.common.widgets.panels.AbstractModifiedResiduePanel;
import org.reactome.web.pwp.model.classes.AbstractModifiedResidue;
import org.reactome.web.pwp.model.classes.EntityWithAccessionedSequence;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AbstractModifiedResiduesContainer extends ProcessesContainer {

    public AbstractModifiedResiduesContainer(EntityWithAccessionedSequence physicalEntity) {
        for (AbstractModifiedResidue modifiedResidue : physicalEntity.getHasModifiedResidue()) {
            this.add(new AbstractModifiedResiduePanel(modifiedResidue));
        }
    }
}
