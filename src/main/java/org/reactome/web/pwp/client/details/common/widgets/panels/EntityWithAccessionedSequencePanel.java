package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.model.client.classes.AbstractModifiedResidue;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.EntityWithAccessionedSequence;
import org.reactome.web.pwp.model.client.classes.ReferenceSequence;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntityWithAccessionedSequencePanel extends DetailsPanel implements TransparentPanel {
    private EntityWithAccessionedSequence ewas;

    @SuppressWarnings("UnusedDeclaration")
    public EntityWithAccessionedSequencePanel(EntityWithAccessionedSequence ewas) {
        this(null, ewas);
    }

    public EntityWithAccessionedSequencePanel(DetailsPanel parentPanel, EntityWithAccessionedSequence ewas) {
        super(parentPanel);
        this.ewas = ewas;
        setLoaded(true);    //EntityWithAccessionedSequence will always be passed as a fully loaded object
        initialize();
    }

    private void initialize(){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        if(this.ewas.getStartCoordinate()!=null && this.ewas.getEndCoordinate()!=null){
            vp.add(getCoordinatesPanel(this.ewas.getStartCoordinate(), this.ewas.getEndCoordinate()));
        }
        vp.add(getReferenceSequencePanel(this.ewas.getReferenceEntity()));
        if(this.ewas.getHasModifiedResidue()!=null && !this.ewas.getHasModifiedResidue().isEmpty()){
            vp.add(getModifiedResiduePanel(this.ewas.getHasModifiedResidue()));
        }
        initWidget(vp);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.ewas;
    }

    private Widget getModifiedResiduePanel(List<AbstractModifiedResidue> modifiedResidueList){
        VerticalPanel vp = new VerticalPanel();
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        vp.setWidth("99%");

        vp.add(new Label("Post-translational modification:"));
        for (AbstractModifiedResidue modifiedResidue : modifiedResidueList) {
            Widget mPanel = new AbstractModifiedResiduePanel(this, modifiedResidue);
            mPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(mPanel);
        }

        return vp;
    }

    private Widget getReferenceSequencePanel(ReferenceSequence referenceSequence){
        VerticalPanel vp = new VerticalPanel();
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        vp.setWidth("99%");

        vp.add(new Label("Reference Entity:"));
        //We associate our parentPanel as the parent of the next panel because this panel is a kind of auxiliary panel
        Widget pPanel = new ReferenceEntityPanel(this, referenceSequence);
        pPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(pPanel);

        return vp;
    }

    private Widget getCoordinatesPanel(int start, int end){
        HorizontalPanel panel = new HorizontalPanel();
        panel.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label title = new Label("Coordinates in the Reference Sequence:");
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        panel.add(title);

        StringBuilder coordinates = new StringBuilder();
        coordinates.append(start);
        coordinates.append(" .. ");
        coordinates.append(end);
        panel.add(new Label(coordinates.toString()));

        return panel;
    }
}
