package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.FunctionalStatusType;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FunctionalStatusTypePanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private FunctionalStatusType functionalStatusType;
    private DisclosurePanel disclosurePanel;

    @SuppressWarnings("UnusedDeclaration")
    public FunctionalStatusTypePanel(FunctionalStatusType functionalStatusType) {
        this(null, functionalStatusType);
    }

    public FunctionalStatusTypePanel(DetailsPanel parentPanel, FunctionalStatusType functionalStatusType) {
        super(parentPanel);
        this.functionalStatusType = functionalStatusType;
        initialize();
    }

    private void initialize(){
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.functionalStatusType.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.functionalStatusType;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.functionalStatusType.load(new ContentClientHandler.ObjectLoaded() {
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
        this.functionalStatusType = (FunctionalStatusType) data;

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");

        if(this.functionalStatusType.getDefinition()!=null){
            vp.add(getDefinitionPanel(this.functionalStatusType.getDefinition()));
        }
        if(!this.functionalStatusType.getName().isEmpty()){
            vp.add(getSynonymsPanel(this.functionalStatusType.getName()));
        }
        if(vp.getWidgetCount()==0){
            vp.add(new HTMLPanel("There is no data available."));
        }

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
