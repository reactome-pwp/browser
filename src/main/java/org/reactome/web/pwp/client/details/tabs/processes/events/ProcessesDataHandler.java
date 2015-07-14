package org.reactome.web.pwp.client.details.tabs.processes.events;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.details.tabs.processes.model.widgets.container.*;
import org.reactome.web.pwp.model.classes.EntityWithAccessionedSequence;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.classes.PhysicalEntity;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ProcessesDataHandler extends EventHandler {

    void onComplexesRequired(ComplexesContainer container, PhysicalEntity physicalEntity);

    void onEntitySetRequired(EntitySetContainer container, PhysicalEntity physicalEntity);

    void onPathwaysForEntitiesRequired(InvolvedPathwaysContainer container, PhysicalEntity physicalEntity);

    void onPathwaysForEventsRequired(InvolvedPathwaysContainer container, Event event);

    void onReactionsWhereInputRequired(ReactionsAsInputContainer container, PhysicalEntity physicalEntity);

    void onReactionsWhereOutputRequired(ReactionsAsOutputContainer container, PhysicalEntity physicalEntity);

    void onOtherFormsOfEWASRequired(OtherFormsForEwasContainer container, EntityWithAccessionedSequence ewas);
}
