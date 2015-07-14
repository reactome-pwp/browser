package org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class OverviewRow extends Composite {
    private VerticalPanel content;

    public OverviewRow() {
        this.content = new VerticalPanel();
        this.initWidget(this.content);
        this.addStyleName("elv-Details-OverviewRow");
    }

    public void add(String title, Widget widget){
        this.addTitle(title);
        this.addWidget(widget);
    }

    private void addTitle(String title){
        Label lbl = new Label(title);
        lbl.addStyleName("elv-Details-OverviewProperty");
        this.content.add(lbl);
    }

    private void addWidget(Widget widget){
        SimplePanel aux = new SimplePanel();
        aux.getElement().getStyle().setPaddingLeft(5, Style.Unit.PX);
        aux.add(widget);
        this.content.add(aux);
    }
}
