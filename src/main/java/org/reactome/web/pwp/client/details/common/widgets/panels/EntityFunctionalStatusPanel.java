package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.EntityFunctionalStatus;
import org.reactome.web.pwp.model.client.classes.FunctionalStatus;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntityFunctionalStatusPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private EntityFunctionalStatus entityFunctionalStatus;
    private DisclosurePanel disclosurePanel;

    public EntityFunctionalStatusPanel(EntityFunctionalStatus entityFunctionalStatus) {
        this(null, entityFunctionalStatus);
    }

    public EntityFunctionalStatusPanel(DetailsPanel parentPanel, EntityFunctionalStatus entityFunctionalStatus) {
        super(parentPanel);
        this.entityFunctionalStatus = entityFunctionalStatus;
        initialize();
    }

    private void initialize(){
        String name = this.entityFunctionalStatus.getDisplayName().replaceAll("_", " ");
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(name);
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.entityFunctionalStatus;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.entityFunctionalStatus.load(new ContentClientHandler.ObjectLoaded() {
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
        this.entityFunctionalStatus = (EntityFunctionalStatus) data;

        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-Advanced");
        vp.setWidth("99%");

        if(this.entityFunctionalStatus.getPhysicalEntity()!=null){
            vp.add(new Label("Physical entity:"));
            PhysicalEntityPanel aux = new PhysicalEntityPanel(this, this.entityFunctionalStatus.getPhysicalEntity());
            aux.setWidth("98%");
            aux.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(aux);
        }

        if(!this.entityFunctionalStatus.getFunctionalStatus().isEmpty()){
            vp.add(new Label("Functional Status:"));
            for (FunctionalStatus functionalStatus : this.entityFunctionalStatus.getFunctionalStatus()) {
                FunctionalStatusPanel aux = new FunctionalStatusPanel(this, functionalStatus);
                aux.setWidth("98%");
                aux.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
                vp.add(aux);
            }
        }

        this.disclosurePanel.setContent(vp);
        setLoaded(true);
    }
}
