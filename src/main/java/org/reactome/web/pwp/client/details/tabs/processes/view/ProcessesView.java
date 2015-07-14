package org.reactome.web.pwp.client.details.tabs.processes.view;

import org.reactome.web.pwp.client.details.tabs.DetailsTabView;
import org.reactome.web.pwp.model.classes.*;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ProcessesView extends DetailsTabView<ProcessesView.Presenter> {

    public interface Presenter extends DetailsTabView.Presenter{
        void getComplexesContaining(PhysicalEntity physicalEntity);
        void getDetailedData(DatabaseObject databaseObject);
        void getEntitySetsContaining(PhysicalEntity physicalEntity);
        void getPathwaysForEntities(PhysicalEntity physicalEntity);
        void getPathwaysForEvent(Event event);
        void getReactionsWhereInput(PhysicalEntity physicalEntity);
        void getReactionsWhereOutput(PhysicalEntity physicalEntity);
        void getOtherFormsForEWAS(EntityWithAccessionedSequence ewas);
    }

    void setComplexesForPhysicalEntity(PhysicalEntity physicalEntity, List<Complex> complexList);
    void setDetailedData(DatabaseObject databaseObject);
    void setEntitySetsForPhysicalEntity(PhysicalEntity physicalEntity, List<EntitySet> entitySetList);
    void setOtherFormsForEWAS(EntityWithAccessionedSequence ewas, List<EntityWithAccessionedSequence> ewasList);
    void setPathwayForPhysicalEntity(PhysicalEntity physicalEntity, List<Pathway> pathwayList);
    void setPathwayForEvent(Event event, List<Pathway> pathwayList);
    void setReactionsWhereInput(PhysicalEntity physicalEntity, List<ReactionLikeEvent> reactionList);
    void setReactionsWhereOutput(PhysicalEntity physicalEntity, List<ReactionLikeEvent> reactionList);
}
