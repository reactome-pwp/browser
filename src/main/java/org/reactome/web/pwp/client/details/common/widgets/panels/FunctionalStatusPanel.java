package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.FunctionalStatus;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FunctionalStatusPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private FunctionalStatus functionalStatus;
    private DisclosurePanel disclosurePanel;

    @SuppressWarnings("UnusedDeclaration")
    public FunctionalStatusPanel(FunctionalStatus functionalStatus) {
        this(null, functionalStatus);
    }

    public FunctionalStatusPanel(DetailsPanel parentPanel, FunctionalStatus functionalStatus) {
        super(parentPanel);
        this.functionalStatus = functionalStatus;
        initialize();
    }

    private void initialize(){
        String name = this.functionalStatus.getDisplayName().replaceAll("_", " ");
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(name);
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.functionalStatus;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.functionalStatus.load(new ContentClientHandler.ObjectLoaded() {
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
        this.functionalStatus = (FunctionalStatus) data;

        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-Advanced");
        vp.setWidth("99%");

        if(this.functionalStatus.getStructuralVariant()!=null){
            vp.add(new Label("Structural Variant:"));
            ExternalOntologyPanel aux = new ExternalOntologyPanel(this, this.functionalStatus.getStructuralVariant());
            aux.setWidth("98%");
            aux.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(aux);
        }

        if(this.functionalStatus.getFunctionalStatusType()!=null){
            vp.add(new Label("Functional status type:"));
            FunctionalStatusTypePanel aux = new FunctionalStatusTypePanel(this, this.functionalStatus.getFunctionalStatusType());
            aux.setWidth("98%");
            aux.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(aux);
        }

        this.disclosurePanel.setContent(vp);
        setLoaded(true);
    }
}
