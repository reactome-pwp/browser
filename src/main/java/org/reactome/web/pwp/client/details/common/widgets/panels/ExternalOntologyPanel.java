package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.ExternalOntology;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExternalOntologyPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private ExternalOntology externalOntology;
    private DisclosurePanel disclosurePanel;

    public ExternalOntologyPanel(ExternalOntology disease) {
        this(null, disease);
    }

    public ExternalOntologyPanel(DetailsPanel parentPanel, ExternalOntology externalOntology) {
        super(parentPanel);
        this.externalOntology = externalOntology;
        this.initialize();
    }

    private void initialize(){
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.externalOntology.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.externalOntology;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.externalOntology.load(new ContentClientHandler.ObjectLoaded() {
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
        this.externalOntology = (ExternalOntology) data;

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");

        vp.add(getDefinitionPanel(this.externalOntology.getDefinition()));
//        vp.add(getIdentifierPanel(this.disease.getIdentifier()));

        if(!this.externalOntology.getInstanceOf().isEmpty())
            vp.add(getInstanceOfPanel(this.externalOntology.getInstanceOf()));

        if(!this.externalOntology.getSynonym().isEmpty())
            vp.add(getSynonymsPanel(this.externalOntology.getSynonym()));

        this.disclosurePanel.setContent(vp);
        setLoaded(true);
    }

    private Widget getDefinitionPanel(String definition){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label title = new Label("Definition:");
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(title);

        SimplePanel sp = new SimplePanel();
        sp.add(new HTML(definition));
        sp.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(sp);

        return vp;
    }

//    private Widget getIdentifierPanel(String identifier){
//        HorizontalPanel hp = new HorizontalPanel();
//        hp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
//
//        Label title = new Label("Identifier:");
//        Style titleStyle = title.getElement().getStyle();
//        titleStyle.setFontWeight(Style.FontWeight.BOLD);
//        titleStyle.setMarginRight(5, Style.Unit.PX);
//        hp.add(title);
//
//        hp.add(new Label(identifier));
//
//        return hp;
//    }

    private Widget getInstanceOfPanel(List<ExternalOntology> instances){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label title = new Label("Instance of:");
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(title);

        for (ExternalOntology instance : instances) {
            DetailsPanel p = new ExternalOntologyPanel(this, instance);
            p.setWidth("99%");
            p.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(p);
        }

        return vp;
    }

    private Widget getSynonymsPanel(List<String> names){
        HorizontalPanel hp = new HorizontalPanel();
        hp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label title = new Label("Synonyms:");
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        hp.add(title);

        StringBuilder synonyms = new StringBuilder();
        for (String name : names) {
            synonyms.append(name);
            synonyms.append(", ");
        }
        synonyms.delete(synonyms.length()-2, synonyms.length()-1);
        hp.add(new Label(synonyms.toString()));

        return hp;
    }
}
