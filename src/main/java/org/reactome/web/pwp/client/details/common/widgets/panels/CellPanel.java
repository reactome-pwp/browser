package org.reactome.web.pwp.client.details.common.widgets.panels;


import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.model.client.classes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellPanel extends DetailsPanel implements TransparentPanel {

    private Cell cell;

    public CellPanel(Cell cell) {
        this(null, cell);
    }

    public CellPanel(DetailsPanel parentPanel, Cell cell) {
        super(parentPanel);
        this.cell = cell;
        setLoaded(true);    //EntityWithAccessionedSequence will always be passed as a fully loaded object
        initialize();
    }

    private void initialize() {

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");

        if (this.cell.getOrgan() != null) {
            vp.add(getOrganPanel(this.cell.getOrgan()));
        }
        if (this.cell.getTissue() != null) {
            vp.add(getTissuePanel(this.cell.getTissue()));
        }

        if (this.cell.getTissueLayer() != null) {
            vp.add(getTissueLayerPanel(this.cell.getTissue()));
        }

        if (this.cell.getProteinMarker() != null && !this.cell.getProteinMarker().isEmpty()) {
            vp.add(getProteinMarkersPanel("Protein markers:", this.cell));
        }

        if (this.cell.getRnaMarker() != null && !this.cell.getRnaMarker().isEmpty()) {
            vp.add(getRnaMarkersPanel("RNA markers:", this.cell.getRnaMarker()));
        }

        /**
         *  Do not display marker reference to avoid duplicating data, add the Literature References: to protein marker
         */
//        if(this.cell.getMarkerReference() != null && !this.cell.getMarkerReference().isEmpty()){
//            vp.add(getMarkerReferencePanel("Marker Reference:", this.cell.getMarkerReference()));
//        }

        initWidget(vp);
    }


    @Override
    public DatabaseObject getDatabaseObject() {
        return this.cell;
    }

    private Widget getOrganPanel(Anatomy organ) {
        VerticalPanel vp = new VerticalPanel();
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        vp.setWidth("99%");
        vp.add(new Label("Organ:"));
        //We associate our parentPanel as the parent of the next panel because this panel is a kind of auxiliary panel
        Widget pPanel = new OrganPanel(this, organ);
        pPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(pPanel);

        return vp;
    }

    private Widget getTissuePanel(Anatomy tissue) {
        VerticalPanel vp = new VerticalPanel();
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        vp.setWidth("99%");
        vp.add(new Label("Tissue:"));
        //We associate our parentPanel as the parent of the next panel because this panel is a kind of auxiliary panel
        Widget pPanel = new TissuePanel(this, tissue);
        pPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(pPanel);

        return vp;
    }

    private Widget getTissueLayerPanel(Anatomy tissueLayer) {
        VerticalPanel vp = new VerticalPanel();
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        vp.setWidth("99%");
        vp.add(new Label("Tissue layer:"));
        //We associate our parentPanel as the parent of the next panel because this panel is a kind of auxiliary panel
        Widget pPanel = new TissueLayerPanel(this, tissueLayer);
        pPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(pPanel);

        return vp;
    }


    private Widget getProteinMarkersPanel(String title, Cell cell) {

        Map<Long, List<Publication>> markerAndPublications = new HashMap<>();
        if (!cell.getMarkerReference().isEmpty()) {
            markerAndPublications = getMarkerAndPublicationFromMarkerRefs(cell.getMarkerReference());
        }

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label label = new Label(title);
        Style titleStyle = label.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(label);

        Map<EntityWithAccessionedSequence, Integer> map = new HashMap<EntityWithAccessionedSequence, Integer>();
        for (EntityWithAccessionedSequence ewas : cell.getProteinMarker()) {
            int num = 1;
            if (map.containsKey(ewas)) {
                num = map.get(ewas) + 1;
            }
            map.put(ewas, num);
        }

        for (EntityWithAccessionedSequence entity : map.keySet()) {
            DetailsPanel p = new PhysicalEntityPanel(this, markerAndPublications, entity, map.get(entity));
            p.setWidth("99%");
            p.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(p);
        }
        return vp;
    }

    private Widget getRnaMarkersPanel(String title, List<EntityWithAccessionedSequence> rnaMarkers ) {

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label label = new Label(title);
        Style titleStyle = label.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(label);

        Map<EntityWithAccessionedSequence, Integer> map = new HashMap<EntityWithAccessionedSequence, Integer>();
        for (EntityWithAccessionedSequence ewas : rnaMarkers) {
            int num = 1;
            if (map.containsKey(ewas)) {
                num = map.get(ewas) + 1;
            }
            map.put(ewas, num);
        }

        for (EntityWithAccessionedSequence entity : map.keySet()) {
            DetailsPanel p = new PhysicalEntityPanel(this, entity, map.get(entity));
            p.setWidth("99%");
            p.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(p);
        }
        return vp;
    }


    private Widget getMarkerReferencePanel(String title, List<MarkerReference> markerReferences) {
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label label = new Label(title);
        Style titleStyle = label.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(label);

        Map<MarkerReference, Integer> map = new HashMap<>();
        for (MarkerReference markerReference : markerReferences) {
            int num = 1;
            if (map.containsKey(markerReference)) {
                num = map.get(markerReference) + 1;
            }
            map.put(markerReference, num);
        }

        for (MarkerReference entity : map.keySet()) {
            DetailsPanel p = new MarkerReferencePanel(this, entity, map.get(entity));
            p.setWidth("99%");
            p.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(p);
        }

        return vp;
    }

    private Map<Long, List<Publication>> getMarkerAndPublicationFromMarkerRefs(List<MarkerReference> markerReferences) {
        Map<Long, List<Publication>> literatureReferenceMap = new HashMap<>();
        for (MarkerReference markerReference : markerReferences) {
            if (!markerReference.getMarker().isEmpty()) {
                for (EntityWithAccessionedSequence ewas : markerReference.getMarker()) {
                    literatureReferenceMap.put(ewas.getDbId(), markerReference.getLiteratureReference());
                }
            }
        }
        return literatureReferenceMap;
    }
}