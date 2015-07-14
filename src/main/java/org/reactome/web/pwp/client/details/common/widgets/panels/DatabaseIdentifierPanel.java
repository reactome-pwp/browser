package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TreeItem;
import org.reactome.web.pwp.model.classes.DatabaseIdentifier;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DatabaseIdentifierPanel extends DetailsPanel {
    private DatabaseIdentifier databaseIdentifier;
    private TreeItem treeItem;

    public DatabaseIdentifierPanel(DatabaseIdentifier databaseIdentifier) {
        this(null, databaseIdentifier);
    }

    public DatabaseIdentifierPanel(DetailsPanel parentPanel, final DatabaseIdentifier databaseIdentifier) {
        super(parentPanel);
        this.databaseIdentifier = databaseIdentifier;
        initialize();
    }

    private void initialize(){
        //NOTE: Here we call twice to getReference because two different panels has to be created
        //they will be placed in different places depending on the usage of this panel
        this.treeItem = new TreeItem(getReference(this.databaseIdentifier));
        initWidget(getReference(this.databaseIdentifier));
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.databaseIdentifier;
    }

    public TreeItem asTreeItem() {
        return this.treeItem;
    }

    private HTMLPanel getReference(DatabaseIdentifier databaseIdentifier){
        //noinspection NonJREEmulationClassesInClientCode
        String[] names = databaseIdentifier.getDisplayName().split(":");
        String reference = names[0];
        String id = names[1]; // databaseIdentifier.getIdentifier();
        String url = databaseIdentifier.getUrl();

        StringBuilder builder = new StringBuilder(reference);
        builder.append("&nbsp;&nbsp;>>&nbsp;&nbsp;<span class=\"");
        //noinspection NonJREEmulationClassesInClientCode
        builder.append(reference.replaceAll(" ",""));
        builder.append("\">");
        builder.append(reference.substring(0, 1));
        builder.append("</span>");
        builder.append("&nbsp;&nbsp;[<a title=\"Go to ");
        builder.append(reference);
        builder.append(": ");
        builder.append(id);
        builder.append("\" target=\"_blank\" href=\"");
        builder.append(url);
        builder.append("\">");
        builder.append(id);
        builder.append("</a>]");

        return new HTMLPanel(builder.toString());
    }
}
