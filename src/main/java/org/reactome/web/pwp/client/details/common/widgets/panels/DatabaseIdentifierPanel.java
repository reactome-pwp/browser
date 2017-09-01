package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
import org.reactome.web.pwp.model.client.classes.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DatabaseIdentifierPanel extends DetailsPanel {

    private String reference;
    private String id;
    private String url;

    private TreeItem treeItem;

    public DatabaseIdentifierPanel(DatabaseIdentifier databaseIdentifier) {
        this(null, databaseIdentifier);
    }

    public DatabaseIdentifierPanel(DetailsPanel parentPanel, final DatabaseIdentifier databaseIdentifier) {
        super(parentPanel);
        String[] names = databaseIdentifier.getDisplayName().split(":");
        this.reference = names[0];
        this.id = names[1];
        this.url = databaseIdentifier.getUrl();
        initialize();
    }

    public DatabaseIdentifierPanel(PhysicalEntity pe) {
        this(null, pe);
    }

    public DatabaseIdentifierPanel(DetailsPanel parentPanel, PhysicalEntity pe) {
        super(parentPanel);
        ReferenceEntity re = null;
        if(pe instanceof EntityWithAccessionedSequence){
            EntityWithAccessionedSequence ewas = pe.cast();
            re =  ewas.getReferenceEntity();
        } else if (pe instanceof SimpleEntity){
            SimpleEntity se = pe.cast();
            re = se.getReferenceEntity();
        }

        if (re != null){
            this.reference = re.getDatabaseName();
            this.id = re.getIdentifier();
            this.url = re.getUrl();
            initialize();
        } else {
            initWidget(new Label("No reference entity"));
        }
    }

    public DatabaseIdentifierPanel(ReferenceEntity re) {
        this(null, re);
    }

    public DatabaseIdentifierPanel(DetailsPanel parentPanel, ReferenceEntity re) {
        super(parentPanel);
        this.reference = re.getDatabaseName();
        this.id = re.getIdentifier();
        this.url = re.getUrl();
        initialize();
    }

    private void initialize(){
        //NOTE: Here we call twice to getReference because two different panels has to be created
        //they will be placed in different places depending on the usage of this panel
        this.treeItem = new TreeItem(getReference());
        initWidget(getReference());
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return null;
    }

    public TreeItem asTreeItem() {
        return this.treeItem;
    }

    private HTMLPanel getReference(){
        //noinspection NonJREEmulationClassesInClientCode
        StringBuilder builder = new StringBuilder();
        builder.append("-&nbsp;<a title=\"Go to ");
        builder.append(reference);
        builder.append(":");
        builder.append(id);
        builder.append("\" target=\"_blank\" href=\"");
        builder.append(url);
        builder.append("\">");
        builder.append(reference);
        builder.append(":");
        builder.append(id);
        builder.append("</a>");

        return new HTMLPanel(builder.toString());
    }
}
