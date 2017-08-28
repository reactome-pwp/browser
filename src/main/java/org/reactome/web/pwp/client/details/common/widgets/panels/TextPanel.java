package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.user.client.ui.HTMLPanel;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TextPanel extends DetailsPanel implements TransparentPanel {

    public TextPanel(String text) {
        this(null, text);
    }

    public TextPanel(DetailsPanel parentPanel, String text) {
        super(parentPanel);
        initWidget(new HTMLPanel(text));
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return null;
    }
}