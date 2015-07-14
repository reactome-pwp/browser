package org.reactome.web.pwp.client.details.tabs.structures.widgets;

import com.google.gwt.user.client.ui.HTMLPanel;

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
        this.container.add(new HTMLPanel("Object does not contains structures associated"));
    }
}
