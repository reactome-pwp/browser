package org.reactome.web.pwp.client.details.tabs.processes.model.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.model.classes.EntityWithAccessionedSequence;
import org.reactome.web.pwp.model.classes.PhysicalEntity;
import org.reactome.web.pwp.client.details.tabs.processes.model.widgets.container.*;
import org.reactome.web.pwp.client.details.tabs.processes.model.widgets.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.processes.model.widgets.factory.TableRowFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PhysicalEntityTable extends ProcessesTable {
    private PhysicalEntity physicalEntity;

    public PhysicalEntityTable(PhysicalEntity physicalEntity) {
        this.physicalEntity = physicalEntity;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case IN_PATHWAYS:
                return TableRowFactory.getProcessesRow(title, new InvolvedPathwaysContainer(this.physicalEntity));
            case IN_REACTIONS_AS_INPUT:
                return TableRowFactory.getProcessesRow(title, new ReactionsAsInputContainer(this.physicalEntity));
            case IN_REACTIONS_AS_OUTPUT:
                return TableRowFactory.getProcessesRow(title, new ReactionsAsOutputContainer(this.physicalEntity));
            case IN_MODIFIED_RESIDUE:
                if(this.physicalEntity instanceof EntityWithAccessionedSequence){
                    EntityWithAccessionedSequence ewas = (EntityWithAccessionedSequence) this.physicalEntity;
                    if(!ewas.getHasModifiedResidue().isEmpty()){
                        AbstractModifiedResiduesContainer c = new AbstractModifiedResiduesContainer(ewas);
                        return TableRowFactory.getProcessesRow(title, c);
                    }
                }
                return null;
            case IN_COMPLEX_AS_COMPONENT:
                return TableRowFactory.getProcessesRow(title, new ComplexesContainer(this.physicalEntity));
            case IN_ENTITY_SET_AS_COMPONENT:
                return TableRowFactory.getProcessesRow(title, new EntitySetContainer(this.physicalEntity));
            case OTHER_FORMS_FOR_EWAS:
                if(this.physicalEntity instanceof EntityWithAccessionedSequence){
                    EntityWithAccessionedSequence ewas = (EntityWithAccessionedSequence) this.physicalEntity;
                    OtherFormsForEwasContainer c =new OtherFormsForEwasContainer(ewas);
                    return TableRowFactory.getProcessesRow(title, c);
                }else{
                    return null;
                }
            default:
                return null;
        }
    }
}
