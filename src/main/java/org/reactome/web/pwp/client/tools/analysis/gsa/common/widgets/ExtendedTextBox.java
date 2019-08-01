package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ExtendedTextBox extends TextBox implements KeyDownHandler {

    public ExtendedTextBox() {
        super();
        this.addKeyDownHandler(this);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            event.stopPropagation();
            event.preventDefault();
            this.setFocus(false);
        }
    }
}
