package org.reactome.web.pwp.client.details.tabs.processes.model.widgets.container;

import org.reactome.web.pwp.client.details.tabs.processes.events.ProcessesDataListener;
import org.reactome.web.pwp.client.details.common.widgets.panels.PhysicalEntityPanel;
import org.reactome.web.pwp.model.classes.Complex;
import org.reactome.web.pwp.model.classes.PhysicalEntity;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ComplexesContainer extends ProcessesContainer {

    public ComplexesContainer(PhysicalEntity physicalEntity) {
        this.showWaitingMessage();
        ProcessesDataListener.getProcessesDataListener().onComplexesRequired(this, physicalEntity);
    }

    public void onComplexesRetrieved(List<Complex> complexList){
        this.clear();
        for (Complex complex : complexList) {
            this.add(new PhysicalEntityPanel(complex));
        }
        this.cleanUp();
    }
}
