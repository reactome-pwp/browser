package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.LiteratureReference;
import org.reactome.web.pwp.model.client.classes.Person;
import org.reactome.web.pwp.model.client.classes.Publication;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PublicationPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private Publication publication;
    private DisclosurePanel disclosurePanel;

    public PublicationPanel(Publication publication){
        this(null, publication);
    }

    public PublicationPanel(DetailsPanel parentPanel, Publication publication) {
        super(parentPanel);
        this.publication = publication;
        initialize();
    }

    private void initialize(){
        disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(publication.getDisplayName());
        disclosurePanel.addOpenHandler(this);
        initWidget(disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return publication;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.publication.load(new ContentClientHandler.ObjectLoaded() {
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
        setLoaded(true);
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setPaddingRight(5, Style.Unit.PX);

        this.publication = (Publication) data;
        if(this.publication instanceof LiteratureReference)
            vp.add(getLiteratureReferenceDetails((LiteratureReference) publication));
        vp.add(getPublicationAuthors(publication));

        disclosurePanel.setContent(vp);
    }

    private Widget getLiteratureReferenceDetails(LiteratureReference literatureReference){
        StringBuilder sb = new StringBuilder();
        sb.append(literatureReference.getJournal());
        sb.append(": ");
        sb.append(literatureReference.getYear());
        sb.append(" ");
        sb.append(literatureReference.getVolume());
        sb.append("; ");
        sb.append(literatureReference.getPages());
        sb.append(". ");

        Label details = new Label(sb.toString());
        Anchor link = new Anchor("Pubmed", "http://www.ncbi.nlm.nih.gov/pubmed/" + literatureReference.getPubMedIdentifier() + "?dopt=Abstract");
        link.setTarget("_blank");
        link.setTitle("Go to PUBMED: " + literatureReference.getPubMedIdentifier());
        link.getElement().getStyle().setMarginLeft(1, Style.Unit.EM);

        HorizontalPanel detailsPanel = new HorizontalPanel();
        detailsPanel.add(details);
        detailsPanel.add(link);
        detailsPanel.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
        return detailsPanel;
    }

    private Widget getPublicationAuthors(Publication data){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");

        for (Person person : data.getAuthors()) {
            if(getLevel(this)==3){  //Only three levels allowed :)
                DetailsPanel p = new TextPanel(this, person.getDisplayName());
                p.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
                p.getElement().getStyle().setMarginLeft(1, Style.Unit.EM);
                vp.add(p);
            }else{
                vp.add(new PersonPanel(this, person));
            }
        }

        HTMLPanel authors = new HTMLPanel("Author(s):");
        authors.add(vp);

        return authors;
    }
}
