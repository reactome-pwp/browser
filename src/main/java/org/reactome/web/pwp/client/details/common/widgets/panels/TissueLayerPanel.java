package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.Anatomy;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.List;

public class TissueLayerPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {

    private Anatomy tissueLayer;
    private DisclosurePanel disclosurePanel;

    public TissueLayerPanel(Anatomy tissueLayer) {
        this(null, tissueLayer);
    }

    public TissueLayerPanel(DetailsPanel parentPanel, Anatomy tissueLayer) {
        super(parentPanel);
        this.tissueLayer = tissueLayer;
        initialize();
    }

    private void initialize() {
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.tissueLayer.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> openEvent) {
        if(!isLoaded())
            this.tissueLayer.load(new ContentClientHandler.ObjectLoaded() {
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

    private void setReceivedData(DatabaseObject data) {

        this.tissueLayer = (Anatomy) data;

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");

        vp.add(getDefinitionPanel(this.tissueLayer.getDefinition()));

        if(!this.tissueLayer.getSynonym().isEmpty())
            vp.add(getSynonymsPanel(this.tissueLayer.getSynonym()));

        this.disclosurePanel.setContent(vp);
        setLoaded(true);
    }


    @Override
    public DatabaseObject getDatabaseObject() {
        return this.tissueLayer;
    }

    private Widget getDefinitionPanel(String definition){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label title = new Label("Definition:");
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(title);

        SimplePanel sp = new SimplePanel();
        sp.add(new HTML(definition));
        sp.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(sp);

        Console.error(" tissue defination panel ");

        return vp;
    }

    private Widget getSynonymsPanel(List<String> names){
        HorizontalPanel hp = new HorizontalPanel();
        hp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label title = new Label("Synonyms:");
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        hp.add(title);

        StringBuilder synonyms = new StringBuilder();
        for (String name : names) {
            synonyms.append(name);
            synonyms.append(", ");
        }
        synonyms.delete(synonyms.length()-2, synonyms.length()-1);
        hp.add(new Label(synonyms.toString()));

        return hp;
    }
}
