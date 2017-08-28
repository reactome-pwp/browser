package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.user.client.ui.Anchor;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StableIdentifierPanel extends DetailsPanel implements TransparentPanel {
    private  String stableIdentifier;

    public StableIdentifierPanel(String stableIdentifier) {
        this(null, stableIdentifier);
    }

    public StableIdentifierPanel(DetailsPanel parentPanel, String stableIdentifier) {
        super(parentPanel);
        this.stableIdentifier = stableIdentifier;
        initialize();
    }

    private void initialize(){
        Anchor link = new Anchor(stableIdentifier, "/cgi-bin/control_panel_st_id?ST_ID=" + stableIdentifier);
        link.setTarget("_blank");
        link.setTitle("Go to REACTOME control panel for " + stableIdentifier);
        initWidget(link);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return null;
    }
}
