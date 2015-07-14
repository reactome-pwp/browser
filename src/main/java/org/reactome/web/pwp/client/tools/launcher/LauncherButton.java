package org.reactome.web.pwp.client.tools.launcher;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class LauncherButton extends Button {

    public LauncherButton(String title, String style, ClickHandler handler) {
        setStyleName(style);
        addClickHandler(handler);
        setTitle(title);
    }
}
