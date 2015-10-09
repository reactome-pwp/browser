package org.reactome.web.pwp.client.toppanel.tour;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author Kostas Sidiropoulos (ksidiro@ebi.ac.uk)
 */
public class TourContainer extends DialogBox {

    public TourContainer() {
        setAutoHideEnabled(true);
        setModal(true);
        setAnimationEnabled(true);
        setGlassEnabled(true);
        setAutoHideOnHistoryEventsEnabled(false);
        setText("Pathway Browser Tour");
        int width = Window.getClientWidth() * 2 / 3;
        int height = Window.getClientHeight() * 2 / 3;
        String w, h;
        if (width > height) {
            w = width + "px";
            h = width * 0.5625 + "px";
        } else {
            w = height * 1.7778 + "px";
            h = height + "px";
        }
        String video = "<iframe width=\"" + w + "\" height=\"" + h + "\" src=\"https://www.youtube.com/embed/ZMFwZxINQGE\" frameborder=\"0\" allowfullscreen></iframe>";
        setWidget(new HTMLPanel(SafeHtmlUtils.fromTrustedString(video)));
    }
}
