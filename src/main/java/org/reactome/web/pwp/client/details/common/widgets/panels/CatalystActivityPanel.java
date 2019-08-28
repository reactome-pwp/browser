package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.*;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CatalystActivityPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private CatalystActivity catalystActivity;
    private CatalystActivityReference catalystActivityReference;
    private DisclosurePanel disclosurePanel;

    public CatalystActivityPanel(CatalystActivity catalystActivity, CatalystActivityReference catalystActivityReference) {
        this(null, catalystActivity, catalystActivityReference);
    }

    public CatalystActivityPanel(DetailsPanel parentPanel, CatalystActivity catalystActivity, CatalystActivityReference catalystActivityReference) {
        super(parentPanel);
        this.catalystActivity = catalystActivity;
        this.catalystActivityReference = catalystActivityReference;
        initialize();
    }

    private void initialize() {
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.catalystActivity.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.catalystActivity;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if (!isLoaded())
            this.catalystActivity.load(new ContentClientHandler.ObjectLoaded() {
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
        this.catalystActivity = (CatalystActivity) data;

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("99%");

        vp.add(new Label("Physical Entity:"));
        Widget pPanel = new PhysicalEntityPanel(this, this.catalystActivity.getPhysicalEntity());
        pPanel.setWidth("98%");
        pPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(pPanel);

        if (!catalystActivity.getActiveUnit().isEmpty()) {
            vp.add(new Label("Active Unit:"));
            for (PhysicalEntity dbObject : catalystActivity.getActiveUnit()) {
                Widget caPanel = new PhysicalEntityPanel(dbObject);
                caPanel.setWidth("98%");
                caPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
                vp.add(caPanel);
            }
        }

        vp.add(new Label("Represents GO Molecular Function:"));
        Widget gPanel = new GO_MolecularFunctionPanel(this, this.catalystActivity.getActivity());
        gPanel.setWidth("98%");
        gPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(gPanel);

        if (this.catalystActivityReference != null && !this.catalystActivityReference.getLiteratureReference().isEmpty()) {
            DisclosurePanel literatureReferences = new DisclosurePanel("Published experimental evidence...");
            literatureReferences.setWidth("100%");
            VerticalPanel aux = new VerticalPanel();
            aux.setWidth("100%");
            for(Publication publication : this.catalystActivityReference.getLiteratureReference()) {
                PublicationPanel pp = new PublicationPanel(this, publication);
                pp.setWidth("99%");
                aux.add(pp);
            }
            literatureReferences.setContent(aux);
            vp.add(literatureReferences);
        }

        this.disclosurePanel.setContent(vp);
        setLoaded(true);
    }
}
