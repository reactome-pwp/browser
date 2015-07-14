package org.reactome.web.pwp.client.details.tabs.processes.model.widgets.container;

import org.reactome.web.pwp.client.details.common.widgets.panels.EventPanel;
import org.reactome.web.pwp.model.classes.PhysicalEntity;
import org.reactome.web.pwp.model.classes.ReactionLikeEvent;
import org.reactome.web.pwp.client.details.tabs.processes.events.ProcessesDataListener;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReactionsAsOutputContainer extends ProcessesContainer {

    public ReactionsAsOutputContainer(PhysicalEntity physicalEntity) {
        this.showWaitingMessage();
        ProcessesDataListener.getProcessesDataListener().onReactionsWhereOutputRequired(this, physicalEntity);
    }

    public void onReactionsRetrieved(List<ReactionLikeEvent> reactionList) {
        this.clear();
        for (ReactionLikeEvent reactionLikeEvent : reactionList) {
            this.add(new EventPanel(reactionLikeEvent));
        }
        this.cleanUp();
    }
}
