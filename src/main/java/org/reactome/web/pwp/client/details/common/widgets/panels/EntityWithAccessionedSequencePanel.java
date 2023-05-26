package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.model.client.classes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntityWithAccessionedSequencePanel extends DetailsPanel implements TransparentPanel {
    private EntityWithAccessionedSequence ewas;

    /**
     *  Add Literature References from MarkerReference to ProteinMarkers to avoid duplicating data in front end
     *  this map uses dbId as key and literature reference as value, map to the same dbId in protein markers to assign
     *  literature Reference to it
     */
    private  Map<Long, List<Publication>> markerRefs = new HashMap<>();

    @SuppressWarnings("UnusedDeclaration")
    public EntityWithAccessionedSequencePanel(EntityWithAccessionedSequence ewas) {
        this(null, ewas);
    }

    public EntityWithAccessionedSequencePanel(DetailsPanel parentPanel, EntityWithAccessionedSequence ewas, Map<Long, List<Publication>> markerRefs) {
        super(parentPanel);
        this.ewas = ewas;
        this.markerRefs = markerRefs;
        setLoaded(true);    //EntityWithAccessionedSequence will always be passed as a fully loaded object
        initialize();
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

        if (markerRefs != null && !markerRefs.isEmpty()) {
            if (markerRefs.containsKey(this.ewas.getDbId())) {
                vp.add(getLiteratureReferencePanel("Literature References:", markerRefs.get(this.ewas.getDbId())));
            }
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

        List<TranslationalModification> tms = new ArrayList<>();
        List<GeneticallyModifiedResidue> gms = new ArrayList<>();
        List<TranscriptionalModification> tcm = new ArrayList<>();
        for (AbstractModifiedResidue residue : modifiedResidueList) {
            if (residue instanceof GeneticallyModifiedResidue) gms.add((GeneticallyModifiedResidue) residue);
            else if (residue instanceof TranscriptionalModification) tcm.add((TranscriptionalModification)residue);
            else tms.add((TranslationalModification) residue);
        }

        if (!tms.isEmpty()) {
            vp.add(new Label(PropertyType.MODIFICATION.getTitle() + ":"));
            for (AbstractModifiedResidue modifiedResidue : tms) {
                Widget mPanel = new AbstractModifiedResiduePanel(this, modifiedResidue);
                mPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
                vp.add(mPanel);
            }
        }
        if (!gms.isEmpty()) {
            vp.add(new Label(PropertyType.MUTATION.getTitle() + ":"));
            for (AbstractModifiedResidue modifiedResidue : gms) {
                Widget mPanel = new AbstractModifiedResiduePanel(this, modifiedResidue);
                mPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
                vp.add(mPanel);
            }
        }
        if (!tcm.isEmpty()) {
            vp.add(new Label(PropertyType.TRANSCRIPTIONAL_MODIFICATION.getTitle() + ":"));
            for (AbstractModifiedResidue modifiedResidue : tcm) {
                Widget mPanel = new AbstractModifiedResiduePanel(this, modifiedResidue);
                mPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
                vp.add(mPanel);
            }
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

    private Widget getLiteratureReferencePanel(String title, List<Publication> LiteratureReference) {

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("99%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label label = new Label(title);
        Style titleStyle = label.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(label);

        for (Publication publication : LiteratureReference) {
            DetailsPanel pp = new PublicationPanel(this, publication);
            pp.setWidth("100%");
            pp.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(pp);
        }
        return vp;
    }
}
