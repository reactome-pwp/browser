package org.reactome.web.pwp.client.details.tabs.processes.model.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.client.details.tabs.processes.model.widgets.container.InvolvedPathwaysContainer;
import org.reactome.web.pwp.client.details.tabs.processes.model.widgets.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.processes.model.widgets.factory.TableRowFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EventTable extends ProcessesTable {
    private Event event;

    public EventTable(Event event) {
        this.event = event;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case IN_PATHWAYS:
                return TableRowFactory.getProcessesRow(title, new InvolvedPathwaysContainer(this.event));
            default:
                return null;
        }
    }
}
