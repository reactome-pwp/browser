package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.GO_CellularComponent;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GO_CellularComponentPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private GO_CellularComponent goCellularComponent;
    private DisclosurePanel disclosurePanel;

    public GO_CellularComponentPanel(GO_CellularComponent goCellularComponent) {
        this(null, goCellularComponent);
    }

    public GO_CellularComponentPanel(DetailsPanel parentPanel, GO_CellularComponent goCellularComponent) {
        super(parentPanel);
        this.goCellularComponent = goCellularComponent;
        initialize();
    }

    private void initialize(){
        //At the beginning only the displayName is placed into the disclosure panel, later on the species will be added
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.goCellularComponent.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return goCellularComponent;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.goCellularComponent.load(new DatabaseObjectLoadedHandler() {
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
        this.goCellularComponent = (GO_CellularComponent) data;
        String accession = goCellularComponent .getAccession();

        FlexTable flexTable = new FlexTable();
        flexTable.setWidth("98%");
        flexTable.addStyleName("elv-Details-OverviewDisclosure-content");
        flexTable.getColumnFormatter().setWidth(0, "75px");

        flexTable.setWidget(0, 0, new Label("Accession"));
        Anchor link = new Anchor("GO:"+accession, "http://www.ebi.ac.uk/QuickGO/GTerm?id=GO:"+accession);
        link.setTarget("_blank");
        link.setTitle("Go to QuickGO for GO:" + accession);
        flexTable.setWidget(0, 1, link);

        flexTable.setWidget(1, 0, new Label("Definition"));
        flexTable.setWidget(1, 1, new Label(this.goCellularComponent.getDefinition()));

        this.disclosurePanel.setContent(flexTable);
        setLoaded(true);
    }
}
