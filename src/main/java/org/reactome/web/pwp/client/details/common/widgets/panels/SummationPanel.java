package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Publication;
import org.reactome.web.pwp.model.client.classes.Summation;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

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
        this.summation.load(new ContentClientHandler.ObjectLoaded() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                setReceivedData(databaseObject);
            }

            @Override
            public void onContentClientException(Type type, String message) {
                vp.clear();
                vp.add(getErrorMessage());
            }

            @Override
            public void onContentClientError(ContentClientError error) {
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