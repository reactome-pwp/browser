package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.client.details.delegates.InstanceSelectedDelegate;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.PhysicalEntity;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PhysicalEntitySpeciesSelectorPanel extends PhysicalEntityPanel {

    public PhysicalEntitySpeciesSelectorPanel(PhysicalEntity physicalEntity) {
        this(null, physicalEntity, 1);
    }

    public PhysicalEntitySpeciesSelectorPanel(DetailsPanel parentPanel, PhysicalEntity physicalEntity) {
        this(parentPanel, physicalEntity, 1);
    }

    public PhysicalEntitySpeciesSelectorPanel(PhysicalEntity physicalEntity, int num) {
        this(null, physicalEntity, num);
    }

    public PhysicalEntitySpeciesSelectorPanel(DetailsPanel parentPanel, PhysicalEntity physicalEntity, int num) {
        super(parentPanel, physicalEntity, num);
    }

    public void setReceivedData(DatabaseObject data) {
        this.physicalEntity = (PhysicalEntity) data;
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");

        if(!this.physicalEntity.getSpecies().isEmpty()){
            vp.add(new SpeciesPanel(this, this.physicalEntity.getSpecies()));
        }

        if(!this.physicalEntity.getCrossReference().isEmpty()){
            vp.add(getCrossReferenceTree());
        }

        disclosurePanel.setContent(vp);
        setLoaded(true);
    }


    @Override
    public void onClick(ClickEvent event) {
        event.stopPropagation();
        InstanceSelectedDelegate.get().instanceSelected(this.physicalEntity.getSpecies().get(0));
    }


}
