package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.EntityWithAccessionedSequence;
import org.reactome.web.pwp.model.client.classes.MarkerReference;
import org.reactome.web.pwp.model.client.classes.Publication;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MarkerReferencePanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {

    private MarkerReference markerReference;
    private DisclosurePanel disclosurePanel;


    public MarkerReferencePanel(MarkerReference markerReference, int num) {
        this(null, markerReference, num);
    }

    public MarkerReferencePanel(DetailsPanel parentPanel, MarkerReference markerReference, int num) {
        super(parentPanel);
        this.markerReference = markerReference;
        initialize(num);
    }

    private void initialize(int num) {

        /**
         *  Display protein name only, for example:
         * "KRT19 [cytosol] The type I keratin 19 possesses distinct and context-dependent assembly properties"
         *  We only need KRT19 [cytosol] in the panel
         */
        String displayName = markerReference.getDisplayName().split("(?=])")[0] + "]" + " maker references";

        if (num > 1)
            displayName = num + " x " + displayName;
        disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(displayName);
        disclosurePanel.addOpenHandler(this);
        initWidget(disclosurePanel);
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if (!isLoaded())
            this.markerReference.load(new ContentClientHandler.ObjectLoaded() {
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

    private void setReceivedData(DatabaseObject databaseObject) {
        this.markerReference = (MarkerReference) databaseObject;

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");

        if (this.markerReference.getMarker() != null && !this.markerReference.getMarker().isEmpty()) {
            vp.add(getMarkerReferencePanel("Marker:", this.markerReference.getMarker()));
        }

        if (this.markerReference.getLiteratureReference() != null && !this.markerReference.getLiteratureReference().isEmpty()) {
            vp.add(getLiteratureReferencePanel("Literature References:", this.markerReference.getLiteratureReference()));
        }

        disclosurePanel.setContent(vp);
        setLoaded(true);
    }




    private Widget getMarkerReferencePanel(String title, List<EntityWithAccessionedSequence> markers) {
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");
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
            DetailsPanel p = new PhysicalEntityPanel(this, entity, map.get(entity));
            p.setWidth("99%");
            p.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(p);
        }
        return vp;
    }

    private Widget getLiteratureReferencePanel(String title, List<Publication> LiteratureReference) {

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label label = new Label(title);
        Style titleStyle = label.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(label);

        for (Publication publication : LiteratureReference) {
            DetailsPanel pp = new PublicationPanel(this, publication);
            pp.setWidth("99%");
            pp.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(pp);
        }

        return vp;
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.markerReference;
    }
}
