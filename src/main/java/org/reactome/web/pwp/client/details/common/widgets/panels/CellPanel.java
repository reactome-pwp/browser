package org.reactome.web.pwp.client.details.common.widgets.panels;


import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.MarkerUtils;
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

        if(this.cell.getCellType() != null){
            vp.add(getCellTypePanel("Cell type:", this.cell.getCellType()));
        }

        if (this.cell.getOrgan() != null) {
            vp.add(getAnatomyPanel("Organ:",this.cell.getOrgan()));
        }
        if (this.cell.getTissue() != null) {
            vp.add(getAnatomyPanel("Tissue:",this.cell.getTissue()));
        }

        if (this.cell.getTissueLayer() != null) {
            vp.add(getAnatomyPanel("Tissue layer:",this.cell.getTissue()));
        }

        if (this.cell.getProteinMarker() != null && !this.cell.getProteinMarker().isEmpty()) {

            Map<Long, List<Publication>> markersAndPublications = new HashMap<>();
            if (!this.cell.getMarkerReference().isEmpty()) {
                markersAndPublications = MarkerUtils.getMarkerPublicationsFromMarkerRefs(cell.getMarkerReference());
            }
            vp.add(getMarkersPanel("Protein markers:", markersAndPublications, this.cell.getProteinMarker()));
        }

        if (this.cell.getRnaMarker() != null && !this.cell.getRnaMarker().isEmpty()) {

            Map<Long, List<Publication>> markersAndPublications = new HashMap<>();
            if (!this.cell.getMarkerReference().isEmpty()) {
                markersAndPublications = MarkerUtils.getMarkerPublicationsFromMarkerRefs(cell.getMarkerReference());
            }

            vp.add(getMarkersPanel("RNA markers:", markersAndPublications, this.cell.getRnaMarker()));
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

    private Widget getCellTypePanel(String title, List<CellType> cellTypes){

        VerticalPanel vp = new VerticalPanel();
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        vp.setWidth("99%");
        vp.add(new Label(title));

        Map<CellType, Integer> map = new HashMap<>();
        for (CellType cellType : cellTypes) {
            int num = 1;
            if (map.containsKey(cellType)) {
                num = map.get(cellType) + 1;
            }
            map.put(cellType, num);
        }

        for (CellType entity : map.keySet()) {
            DetailsPanel p = new ExternalOntologyPanel(this, entity);
            p.setWidth("100%");
            p.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(p);
        }
        return vp;
    }

    private Widget getAnatomyPanel(String title, Anatomy anatomy){
        VerticalPanel vp = new VerticalPanel();
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        vp.setWidth("99%");
        vp.add(new Label(title));
        //We associate our parentPanel as the parent of the next panel because this panel is a kind of auxiliary panel
        Widget pPanel = new ExternalOntologyPanel(this, anatomy);
        pPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(pPanel);

        return vp;
    }

    private Widget getMarkersPanel(String title, Map<Long, List<Publication>> markersPublication, List<EntityWithAccessionedSequence> markers ){

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label label = new Label(title);
        Style titleStyle = label.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(label);

        Map<EntityWithAccessionedSequence, Integer> map = new HashMap<EntityWithAccessionedSequence, Integer>();
        for (EntityWithAccessionedSequence ewas : markers) {
            int num = 1;
            if (map.containsKey(ewas)) {
                num = map.get(ewas) + 1;
            }
            map.put(ewas, num);
        }

        for (EntityWithAccessionedSequence entity : map.keySet()) {
            DetailsPanel p = new PhysicalEntityPanel(this, markersPublication, entity, map.get(entity));
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
}