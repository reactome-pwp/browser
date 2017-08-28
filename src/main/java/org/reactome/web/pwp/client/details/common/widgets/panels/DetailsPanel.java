package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public abstract class DetailsPanel extends Composite {
    private boolean isLoaded = false;
    protected DetailsPanel parentPanel;

    public DetailsPanel(DetailsPanel parentPanel) {
        this.parentPanel = parentPanel;
    }

    public abstract DatabaseObject getDatabaseObject();

    protected Widget getErrorMessage(String msg){
        HTMLPanel message = new HTMLPanel(msg);
        message.addStyleName("elv-Details-OverviewDisclosure");
        message.addStyleName("elv-Details-OverviewDisclosure-empty");
        return message;
    }

    protected int getLevel(){
        if(parentPanel==null) return 1;
        int value = (this instanceof TransparentPanel)?0:1;
        return parentPanel.getLevel() + value;
    }

    protected int getLevel(DetailsPanel panel){
        @SuppressWarnings("NonJREEmulationClassesInClientCode")
        int aux = this.getClass().equals(panel.getClass())?1:0;
        if(parentPanel==null) return aux;
        return parentPanel.getLevel(panel) + aux;
    }

    protected DetailsPanel getParentPanel() {
        return parentPanel;
    }

    protected boolean isPanelInParentPanels(DetailsPanel panel){
        return this.equals(panel) || (getParentPanel() != null && getParentPanel().isPanelInParentPanels(panel));
    }

    protected Widget getErrorMessage(){
        FlowPanel fp = new FlowPanel();
        fp.add(new Image(CommonImages.INSTANCE.exclamation()));
        fp.add(new InlineLabel("Data could no be loaded"));
        return fp;
    }

    protected boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }


    @Override
    protected void initWidget(Widget widget) {
        super.initWidget(widget);

        if((this instanceof TransparentPanel)) return;

        addStyleName("elv-Details-OverviewDisclosure");
        if(getLevel()%2==0){
            addStyleName("elv-Details-OverviewDisclosure-even");
        }else{
            addStyleName("elv-Details-OverviewDisclosure-odd");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailsPanel that = (DetailsPanel) o;

        //noinspection RedundantIfStatement
        if (getDatabaseObject() != null ? !getDatabaseObject().equals(that.getDatabaseObject()) : that.getDatabaseObject() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getDatabaseObject() != null ? getDatabaseObject().hashCode() : 0;
    }
}