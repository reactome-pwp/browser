package org.reactome.web.pwp.client.details.common.widgets.disclosure;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public abstract class DisclosurePanelFactory {

    public static DisclosurePanel getAdvancedDisclosurePanel(String text){
        return getAdvancedDisclosurePanel(text, null);
    }

    public static DisclosurePanel getAdvancedDisclosurePanel(FlowPanel panel, ClickHandler instanceSelector){
        DisclosurePanel dp = new DisclosurePanel();
        dp.setStyleName("elv-Details-OverviewDisclosure-Advanced");
        dp.setWidth("100%"); //DO NOT CHANGE THIS VALUE

        DisclosureHeader header = new DisclosureHeader(panel, instanceSelector);
        dp.setHeader(header);
        dp.setContent(getLoadingMessage());
        dp.setAnimationEnabled(true);
        dp.addCloseHandler(header);
        dp.addOpenHandler(header);
        return dp;
    }

    public static DisclosurePanel getAdvancedDisclosurePanel(String text, ClickHandler instanceSelector){
        DisclosurePanel dp = new DisclosurePanel();
        dp.setStyleName("elv-Details-OverviewDisclosure-Advanced");
        dp.setWidth("100%"); //DO NOT CHANGE THIS VALUE

        DisclosureHeader header = new DisclosureHeader(text, instanceSelector);
        dp.setHeader(header);
        dp.setContent(getLoadingMessage());
        dp.setAnimationEnabled(true);
        dp.addCloseHandler(header);
        dp.addOpenHandler(header);
        return dp;
    }

    public static DisclosurePanel getDisclosurePanel(String text){
        DisclosurePanel dp = new DisclosurePanel(text);
        dp.setStyleName("elv-Details-OverviewDisclosure");
        dp.setWidth("100%"); //DO NOT CHANGE THIS VALUE

        dp.setAnimationEnabled(true);
        dp.setContent(getLoadingMessage());
        return dp;
    }

    public static Widget getLoadingMessage(){
        return getLoadingMessage("Loading...");
    }

    public static Widget getLoadingMessage(String customMessage){
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Image(DisclosureImages.INSTANCE.getLoadingImage()));
        hp.add(new HTMLPanel(customMessage));
        hp.setSpacing(5);

        return hp;
    }
}
