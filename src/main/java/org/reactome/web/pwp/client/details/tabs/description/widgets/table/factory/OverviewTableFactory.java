package org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory;

import org.reactome.web.pwp.client.details.tabs.description.widgets.table.*;
import org.reactome.web.pwp.model.client.classes.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class OverviewTableFactory {

    public static OverviewTable getOverviewTable(DatabaseObject databaseObject){
        OverviewTable aux = _getOverviewTable(databaseObject);
        aux.initialize();
        return aux;
    }

    private static OverviewTable _getOverviewTable(DatabaseObject databaseObject){
        /********************** EVENTS **********************/
        if(databaseObject instanceof BlackBoxEvent){
            return new BlackBoxEventTable((BlackBoxEvent) databaseObject);
        }

        if(databaseObject instanceof Reaction){
            return new ReactionTable((Reaction) databaseObject);
        }

        if(databaseObject instanceof ReactionLikeEvent){
            return new ReactionLikeEventTable((ReactionLikeEvent) databaseObject);
        }

        if(databaseObject instanceof Pathway){
            return new PathwayTable((Pathway) databaseObject);
        }

        if(databaseObject instanceof Event) {
            return new EventTable((Event) databaseObject);
        }

        /********************** ENTITIES **********************/
        if(databaseObject instanceof EntityWithAccessionedSequence){
            return new EWASTable((EntityWithAccessionedSequence) databaseObject);
        }

        if(databaseObject instanceof GenomeEncodedEntity){
            return new GenomeEncodedEntityTable((GenomeEncodedEntity) databaseObject);
        }

        if(databaseObject instanceof Complex){
            return new ComplexTable((Complex) databaseObject);
        }

//        if(databaseObject instanceof OpenSet){
//            return new OpenSetTable((OpenSet) databaseObject);
//        }

        if(databaseObject instanceof CandidateSet){
            return new CandidateSetTable((CandidateSet) databaseObject);
        }

        if(databaseObject instanceof EntitySet){
            return new EntitySetTable((EntitySet) databaseObject);
        }

        if(databaseObject instanceof Polymer){
            return new PolymerTable((Polymer) databaseObject);
        }

        if(databaseObject instanceof SimpleEntity){
            return new SimpleEntityTable((SimpleEntity) databaseObject);
        }

        if(databaseObject instanceof PhysicalEntity){
            return new PhysicalEntityTable((PhysicalEntity) databaseObject);
        }

        /********************** DEFAULT **********************/
        return new DefaultTable(databaseObject);
    }
}

