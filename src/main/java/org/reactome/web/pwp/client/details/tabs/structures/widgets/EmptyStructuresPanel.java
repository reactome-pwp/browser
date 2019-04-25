package org.reactome.web.pwp.client.details.tabs.structures.widgets;

import com.google.gwt.user.client.ui.HTMLPanel;
import org.reactome.web.pwp.client.details.tabs.structures.StructuresTabDisplay;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EmptyStructuresPanel extends StructuresPanel<Object> {

    public EmptyStructuresPanel() {
        this.setEmpty();
    }

    @Override
    public void add(Object element) {
        //Nothing here
    }

    @Override
    public void setEmpty() {
        this.container.clear();
        HTMLPanel panel = new HTMLPanel("Object does not contain associated structures");
        panel.setStyleName(StructuresTabDisplay.RESOURCES.css().message());
        this.container.add(panel);
    }
}
