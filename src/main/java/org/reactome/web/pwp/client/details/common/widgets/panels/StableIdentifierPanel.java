package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.user.client.ui.Anchor;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.StableIdentifier;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StableIdentifierPanel extends DetailsPanel implements TransparentPanel {
    private  StableIdentifier stableIdentifier;

    public StableIdentifierPanel(StableIdentifier stableIdentifier) {
        this(null, stableIdentifier);
    }

    public StableIdentifierPanel(DetailsPanel parentPanel, StableIdentifier stableIdentifier) {
        super(parentPanel);
        this.stableIdentifier = stableIdentifier;
        initialize();
    }

    private void initialize(){
        String identifier = stableIdentifier.getDisplayName();
        Anchor link = new Anchor(identifier, "/cgi-bin/control_panel_st_id?ST_ID=" + identifier);
        link.setTarget("_blank");
        link.setTitle("Go to REACTOME control panel for " + identifier);
        initWidget(link);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.stableIdentifier;
    }
}
