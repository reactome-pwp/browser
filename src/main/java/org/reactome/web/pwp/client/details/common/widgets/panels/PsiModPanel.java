package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.PsiMod;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PsiModPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private PsiMod psiMod;
    private DisclosurePanel disclosurePanel;

    @SuppressWarnings("UnusedDeclaration")
    public PsiModPanel(PsiMod psiMod) {
        this(null, psiMod);
    }

    public PsiModPanel(DetailsPanel parentPanel, PsiMod psiMod) {
        super(parentPanel);
        this.psiMod = psiMod;
        initialize();
    }

    private void initialize(){
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.psiMod.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.psiMod;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.psiMod.load(new ContentClientHandler.ObjectLoaded() {
                @Override
                public void onObjectLoaded(DatabaseObject databaseObject) {
                    setReceivedData(databaseObject);
                }

                @Override
                public void onContentClientException(Type type, String message) {
                    disclosurePanel.setContent(getErrorMessage());
                }

                @Override
                public void onContentClientError(ContentClientError error) {
                    disclosurePanel.setContent(getErrorMessage());
                }
            });
    }

    public void setReceivedData(DatabaseObject data) {
        this.psiMod = (PsiMod) data;
        StringBuilder sb = new StringBuilder();
        for (String s : this.psiMod.getName()) {
            sb.append(s);
            sb.append(" ");
        }

        FlexTable flexTable = new FlexTable();
        flexTable.setWidth("98%");
        flexTable.getColumnFormatter().setWidth(0, "75px");
        flexTable.setWidget(0, 0, new Label("Name"));
        flexTable.setWidget(0, 1, new Label(sb.toString().trim()));

        flexTable.setWidget(1, 0, new Label("Definition"));
        flexTable.setWidget(1, 1, new Label(this.psiMod.getDefinition()));

        this.disclosurePanel.setContent(flexTable);

        setLoaded(true);
    }
}
