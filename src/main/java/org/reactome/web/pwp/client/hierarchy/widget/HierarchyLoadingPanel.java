package org.reactome.web.pwp.client.hierarchy.widget;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.SimplePanel;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.model.client.classes.Species;

/**
 * Shows a message depending on the action done over the hierarchy tree
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyLoadingPanel extends SimplePanel {

    public HierarchyLoadingPanel() {
        this.showLoadingInfo(null);
    }

    public void showErrorMessage(String msg){
        this.setMessage(CommonImages.INSTANCE.exclamation(), msg);
    }

    public void showLoadingInfo(Species species){
        ImageResource ir = CommonImages.INSTANCE.loader();
        String msg;
        if(species==null){
            msg = "Please wait until the species data is available...";
        }else{
            msg = "Loading hierarchy for " + species.getDisplayName();
        }
        this.setMessage(ir, msg);
    }

    private void setMessage(ImageResource imageResource, String message){
        clear();

        FlowPanel fp = new FlowPanel();
        fp.add(new Image(imageResource));
        fp.add(new InlineLabel(message));
        this.add(fp);
    }
}
