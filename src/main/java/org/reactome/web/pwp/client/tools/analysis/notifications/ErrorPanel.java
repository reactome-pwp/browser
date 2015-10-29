package org.reactome.web.pwp.client.tools.analysis.notifications;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisError;
import org.reactome.web.pwp.client.tools.launcher.LauncherButton;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ErrorPanel extends FlowPanel implements ClickHandler {

    private InlineLabel errorTitle;
    private Label errorMsg;

    public ErrorPanel(){
        setStyleName(RESOURCES.getCSS().errorPanel());
        FlowPanel header = new FlowPanel();

        Button closeBtn = new LauncherButton("Close notification", RESOURCES.getCSS().close(), this);
        header.add(closeBtn);

        errorTitle = new InlineLabel();
        errorTitle.setStyleName(RESOURCES.getCSS().errorTitle());
        header.add(errorTitle);


        errorMsg = new Label();
        errorMsg.addStyleName(RESOURCES.getCSS().errorMsg());

        add(header);
        add(errorMsg);
        makeVisible(false);
    }


    public void makeVisible(boolean visible){
        if(visible){
            this.setVisible(true);
        }else{
            this.setVisible(false);
        }
    }

    public void setErrorMessage(AnalysisError error){
        if(error!=null) {
            String title = "There are some issues with your sample - " + error.getReason() + " [" + error.getCode() + "]";
            StringBuilder sb = new StringBuilder();
            List<String> msgList = error.getMessages();
            if(msgList!=null) {
                for (int i = 0; i < msgList.size(); i++) {
                    sb.append("\t- " + error.getMessages().get(i) + "\n");
                }
            }
            setErrorMessage(title, sb.toString());
        }
    }

    public void setErrorMessage(String title, String message){
        this.errorTitle.setText(title);
        this.errorMsg.setText(message);
        makeVisible(true);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        makeVisible(false);
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("../images/close_red_clicked.png")
        ImageResource closeRedClicked();

        @Source("../images/close_red_hovered.png")
        ImageResource closeRedHovered();

        @Source("../images/close_red_normal.png")
        ImageResource closeRedNormal();

    }

    @CssResource.ImportedWithPrefix("pwp-ErrorPanel")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/tools/analysis/notifications/ErrorPanel.css";

        String close();

        String errorPanel();

        String errorTitle();

        String errorMsg();
    }
}
