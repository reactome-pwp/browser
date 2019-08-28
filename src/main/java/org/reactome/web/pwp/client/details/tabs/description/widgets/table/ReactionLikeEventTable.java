package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.ReactionLikeEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReactionLikeEventTable extends EventTable {
    private ReactionLikeEvent reactionLikeEvent;

    public ReactionLikeEventTable(ReactionLikeEvent reactionLikeEvent) {
        super(reactionLikeEvent);
        this.reactionLikeEvent = reactionLikeEvent;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case INPUT:
                return TableRowFactory.getPhysicalEntityRow(title, this.reactionLikeEvent.getInputs());
            case CATALYST:
                return TableRowFactory.getCatalystActivityRow(title, this.reactionLikeEvent.getCatalystActivities(), this.reactionLikeEvent.getCatalystActivityReference());
            case OUTPUT:
                return TableRowFactory.getPhysicalEntityRow(title, this.reactionLikeEvent.getOutputs());
            case ENTITY_FUNCTIONAL_STATUS:
                return TableRowFactory.getEntityFunctionalStatusRow(title, this.reactionLikeEvent.getEntityFunctionalStatus());
            case ENTITY_ON_OTHER_CELL:
                return TableRowFactory.getPhysicalEntityRow(title, this.reactionLikeEvent.getEntityOnOtherCell());
            case NORMAL_REACTION:
                return TableRowFactory.getNormalReactionLikeEventRow(title, this.reactionLikeEvent.getNormalReaction());
            case POSITIVELY_REGULATED:
                return TableRowFactory.getRegulationRow(title, this.reactionLikeEvent.getPositiveRegulations(), this.reactionLikeEvent.getRegulationReference());
            case NEGATIVELY_REGULATED:
                return TableRowFactory.getRegulationRow(title, this.reactionLikeEvent.getNegativeRegulations(), this.reactionLikeEvent.getRegulationReference());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
