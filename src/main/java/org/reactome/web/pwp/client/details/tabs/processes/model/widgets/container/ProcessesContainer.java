package org.reactome.web.pwp.client.details.tabs.processes.model.widgets.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class ProcessesContainer extends VerticalPanel {

    protected ProcessesContainer() {
        this.setWidth("100%");
    }

    protected void showWaitingMessage(){
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading data, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.clear();
        this.add(message);
    }

    protected void cleanUp(){
        if(this.getChildren().size()==0){
            if(GWT.isScript()){
                this.getParent().getParent().removeFromParent();
            }else{
                this.add(new HTMLPanel("N/A"));
            }
        }
    }
}
