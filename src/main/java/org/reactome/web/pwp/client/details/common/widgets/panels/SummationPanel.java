package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Publication;
import org.reactome.web.pwp.model.classes.Summation;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SummationPanel extends DetailsPanel {
    private Summation summation;
    private VerticalPanel vp;

    public SummationPanel(Summation summation) {
        this(null, summation);
    }

    public SummationPanel(DetailsPanel parentPanel, Summation summation) {
        super(parentPanel);
        this.summation = summation;
        this.vp = new VerticalPanel();
        initialize();
    }

    private void initialize(){
        vp.add(DisclosurePanelFactory.getLoadingMessage());
        vp.addStyleName("elv-Details-OverviewDisclosure-Advanced");
        vp.setWidth("100%");
        initWidget(vp);
        this.summation.load(new DatabaseObjectLoadedHandler() {
            @Override
            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                setReceivedData(databaseObject);
            }

            @Override
            public void onDatabaseObjectError(Throwable trThrowable) {
                vp.clear();
                vp.add(getErrorMessage());
            }
        });
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.summation;
    }

    public void setReceivedData(DatabaseObject data) {
        this.summation = (Summation) data;
        HTMLPanel content = new HTMLPanel(this.summation.getText());
        content.addStyleName("elv-Details-OverviewDisclosure-summation");
        vp.clear();
        vp.add(content);

        if(!this.summation.getLiteratureReference().isEmpty()){
            DisclosurePanel literatureReferences = new DisclosurePanel("Background literature references...");
            literatureReferences.setWidth("100%");
            VerticalPanel aux = new VerticalPanel();
            aux.setWidth("100%");
            for (Publication publication : this.summation.getLiteratureReference()) {
                PublicationPanel pp = new PublicationPanel(this, publication);
                pp.setWidth("99%");
                aux.add(pp);
            }
            literatureReferences.setContent(aux);
            vp.add(literatureReferences);
        }

        setLoaded(true);
    }
}