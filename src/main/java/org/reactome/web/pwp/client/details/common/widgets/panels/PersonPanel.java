package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseIdentifier;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Person;
import org.reactome.web.pwp.model.client.classes.Publication;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PersonPanel extends DetailsPanel implements OpenHandler<DisclosurePanel>, TransparentPanel {
    private Person person;
    private DisclosurePanel disclosurePanel;

    @SuppressWarnings("UnusedDeclaration")
    public PersonPanel(Person person) {
        this(null, person);
    }

    public PersonPanel(DetailsPanel parentPanel, Person person) {
        super(parentPanel);
        this.person = person;
        initialize();
    }

    private void initialize(){
        disclosurePanel = DisclosurePanelFactory.getDisclosurePanel(person.getDisplayName());
        disclosurePanel.addOpenHandler(this);
        disclosurePanel.setWidth("99%");
        initWidget(disclosurePanel);
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded()){
            this.person.load(new ContentClientHandler.ObjectLoaded() {
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
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return person;
    }

    public void setReceivedData(DatabaseObject data) {
        this.person = (Person) data;
        ContentClient.loadPersonPublications(this.person, new ContentClientHandler.ObjectLoaded<Person>() {
            @Override
            public void onObjectLoaded(Person databaseObject) {
                setReceivedReferences(person.getPublications());
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


    public void setReceivedReferences(List<Publication> publications) {
        setLoaded(true);

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");

        vp.add(getPositionBasedTitle());
        for (Publication publication : publications) {
            PublicationPanel lrp = new PublicationPanel(this, publication);
            lrp.setWidth("99%");
            if(!isPanelInParentPanels(lrp))
                vp.add(lrp);
        }

        VerticalPanel content = new VerticalPanel();
        content.setWidth("100%");
        for (DatabaseIdentifier databaseIdentifier : person.getCrossReference()) {
            content.add(getDatabaseIdentifier(databaseIdentifier));
        }
        content.add((vp.getWidgetCount()>1)? vp : getNoReferencesMessage());
        disclosurePanel.setContent(content);
    }

    private Widget getNoReferencesMessage(){
        HTMLPanel message;
        if(getLevel(this)==1)
            message = new HTMLPanel("No other references");
        else
            message = new HTMLPanel("No more references for this author in Reactome database");
        message.addStyleName("elv-Details-OverviewDisclosure");
        message.addStyleName("elv-Details-OverviewDisclosure-empty");
        return message;
    }

    private Widget getPositionBasedTitle(){
        /* TITLE SELECTOR based in the position of this Person Panel */
        String title;
        if(getParentPanel() instanceof PublicationPanel)
            title = "Other author Publication(s) in Reactome database:";
        else
        if(getLevel(this)==1)
            title = "Author Publication(s) in Reactome database:";
        else
            title = "Other author Publication(s) in Reactome database:";

        return new HTMLPanel(title);
    }

    //We do NOT use the DatabaseIdentifierPanel here because we want a different look and feel
    public Widget getDatabaseIdentifier(DatabaseIdentifier databaseIdentifier){
        String[] aux = databaseIdentifier.getDisplayName().split(":");
        HorizontalPanel hp = new HorizontalPanel();
        if(aux[0].equals("ORCID")){
            Image orcid = new Image(CommonImages.INSTANCE.orcid());
            orcid.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
            orcid.setTitle("ORCID identifier");
            hp.add(orcid);
        }
        hp.add(new Label(aux[0] + ":"));

        Anchor a = new Anchor(aux[1], databaseIdentifier.getUrl(), "_blank");
        a.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        hp.add(a);
        hp.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
        return hp;
    }
}
