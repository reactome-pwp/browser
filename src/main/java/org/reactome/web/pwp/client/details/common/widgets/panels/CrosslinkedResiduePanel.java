package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.CrosslinkedResidue;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CrosslinkedResiduePanel extends DetailsPanel {
    private CrosslinkedResidue crosslinkedResidue;
    private HorizontalPanel contentPanel;

    @SuppressWarnings("UnusedDeclaration")
    public CrosslinkedResiduePanel(CrosslinkedResidue crosslinkedResidue) {
        this(null, crosslinkedResidue);
    }

    public CrosslinkedResiduePanel(DetailsPanel parentPanel, CrosslinkedResidue crosslinkedResidue) {
        super(parentPanel);
        this.crosslinkedResidue = crosslinkedResidue;
        initialize();
    }

    private void initialize(){
        this.contentPanel = new HorizontalPanel();
        this.contentPanel.add(DisclosurePanelFactory.getLoadingMessage());
        this.contentPanel.setWidth("99%");
        initWidget(this.contentPanel);
        this.crosslinkedResidue.load(new ContentClientHandler.ObjectLoaded() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                setReceivedData(databaseObject);
            }

            @Override
            public void onContentClientException(Type type, String message) {
                contentPanel.clear();
                contentPanel.add(getErrorMessage());
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                contentPanel.clear();
                contentPanel.add(getErrorMessage());
            }
        });
    }

    public void setReceivedData(DatabaseObject data) {
        this.crosslinkedResidue = (CrosslinkedResidue) data;

        FlexTable flexTable = new FlexTable();
        flexTable.setWidth("98%");
        flexTable.getColumnFormatter().setWidth(0, "75px");
        flexTable.setCellPadding(0);
        flexTable.setCellSpacing(0);

        flexTable.setWidget(0, 0, new Label("Second Coordinate"));
        flexTable.setWidget(0, 1, new Label(this.crosslinkedResidue.getSecondCoordinate().toString()));

        this.contentPanel.clear();
        this.contentPanel.add(flexTable);

        setLoaded(true);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.crosslinkedResidue;
    }
}
