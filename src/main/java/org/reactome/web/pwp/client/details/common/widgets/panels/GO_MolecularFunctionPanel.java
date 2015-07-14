package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.GO_MolecularFunction;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GO_MolecularFunctionPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    GO_MolecularFunction goMolecularFunction;
    private DisclosurePanel disclosurePanel;

    @SuppressWarnings("UnusedDeclaration")
    public GO_MolecularFunctionPanel(GO_MolecularFunction goMolecularFunction) {
        this(null, goMolecularFunction);
    }

    public GO_MolecularFunctionPanel(DetailsPanel parentPanel, GO_MolecularFunction goMolecularFunction) {
        super(parentPanel);
        this.goMolecularFunction = goMolecularFunction;
        initialize();
    }

    private void initialize(){
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.goMolecularFunction.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.goMolecularFunction;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.goMolecularFunction.load(new DatabaseObjectLoadedHandler() {
                @Override
                public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                    setReceivedData(databaseObject);
                }

                @Override
                public void onDatabaseObjectError(Throwable trThrowable) {
                    disclosurePanel.setContent(getErrorMessage());
                }
            });
    }

    public void setReceivedData(DatabaseObject data) {
        this.goMolecularFunction = (GO_MolecularFunction) data;

        FlexTable flexTable = new FlexTable();
        flexTable.setWidth("98%");
        flexTable.addStyleName("elv-Details-OverviewDisclosure-content");
        flexTable.getColumnFormatter().setWidth(0, "75px");

        String accession = this.goMolecularFunction.getAccession();
        flexTable.setWidget(0, 0, new Label("Accession"));
        Anchor link = new Anchor("GO:"+accession, "http://www.ebi.ac.uk/QuickGO/GTerm?id=GO:"+accession);
        link.setTarget("_blank");
        link.setTitle("Go to QuickGO for GO:" + accession);
        flexTable.setWidget(0, 1, link);

        flexTable.setWidget(1, 0, new Label("Definition"));
        flexTable.setWidget(1, 1, new Label(this.goMolecularFunction.getDefinition()));

        this.disclosurePanel.setContent(flexTable);
        setLoaded(true);
    }
}
