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

        if (!this.cell.getProteinMarker().isEmpty()) {
            vp.add(getProteinMarkersPanel("Protein markers:", cell.getProteinMarker()));
        }

        if(!this.cell.getMarkerReference().isEmpty()) {
             vp.add(getMarkerReferencePanel("Markers:", cell.getMarkerReference()));
        }

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


    private Widget getProteinMarkersPanel(String title, List<EntityWithAccessionedSequence> ptoteinMarkers) {
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label label = new Label(title);
        Style titleStyle = label.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(label);

        Map<EntityWithAccessionedSequence, Integer> map = new HashMap<EntityWithAccessionedSequence, Integer>();
        for (EntityWithAccessionedSequence ewas : ptoteinMarkers) {
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

    private Widget getMarkerReferencePanel(String title, List<MarkerReference> markerReference) {
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label label = new Label(title);
        Style titleStyle = label.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(label);

        Map<MarkerReference, Integer> map = new HashMap<MarkerReference, Integer>();
        for (MarkerReference ewas : markerReference) {
            int num = 1;
            if (map.containsKey(ewas)) {
                num = map.get(ewas) + 1;
            }
            map.put(ewas, num);
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