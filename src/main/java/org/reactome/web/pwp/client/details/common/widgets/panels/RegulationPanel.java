package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.*;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class RegulationPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private Regulation regulation;
    private DisclosurePanel disclosurePanel;

    public RegulationPanel(Regulation regulation) {
        this(null, regulation);
    }

    public RegulationPanel(DetailsPanel parentPanel, Regulation regulation) {
        super(parentPanel);
        this.regulation = regulation;
        initialize();
    }

    private void initialize() {
        String displayName = this.regulation.getDisplayName();
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(displayName);
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return regulation;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if (!isLoaded())
            this.regulation.load(new ContentClientHandler.ObjectLoaded() {
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
        this.regulation = (Regulation) data;

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");

        DatabaseObject regulator = this.regulation.getRegulator();
        if(regulator != null) {
            Widget aux = null;
            if (regulator instanceof PhysicalEntity) {
                aux = new PhysicalEntityPanel(this, (PhysicalEntity) regulator);
            } else if (regulator instanceof Event) {
                aux = new EventPanel(this, (Event) regulator);
            } else if (regulator instanceof CatalystActivity) {
                aux = new CatalystActivityPanel(this, (CatalystActivity) regulator);
            }
            if (aux != null) {
                vp.add(new Label("Regulator: "));
                aux.setWidth("100%");
                aux.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
                vp.add(aux);
            }
        }

        if (this.regulation.getGoBiologicalProcess() != null) {
            vp.add(new Label("Go biological process"));
            vp.add(new GO_BiologicalProcessPanel(this, this.regulation.getGoBiologicalProcess()));
        }

        if (!this.regulation.getName().isEmpty()) {
            vp.add(getNamesPanel(this.regulation.getName()));
        }

        if (!this.regulation.getSummation().isEmpty()) {
            vp.add(new Label("Summation:"));
            for (Summation summation : this.regulation.getSummation()) {
                Widget aux = new SummationPanel(this, summation);
                aux.setWidth("100%");
                aux.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
                vp.add(aux);
            }
        }

        if (!this.regulation.getLiteratureReference().isEmpty()) {
            DisclosurePanel literatureReferences = new DisclosurePanel("Published experimental evidence...");
            literatureReferences.setWidth("100%");
            VerticalPanel aux = new VerticalPanel();
            aux.setWidth("100%");
            for (Publication publication : this.regulation.getLiteratureReference()) {
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

    private Widget getNamesPanel(List<String> list) {
        HorizontalPanel hp = new HorizontalPanel();
        hp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label title = new Label("Names:");
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        hp.add(title);

        StringBuilder names = new StringBuilder();
        for (String name : list) {
            names.append(name);
            names.append(", ");
        }
        names.delete(names.length() - 2, names.length() - 1);
        hp.add(new Label(names.toString()));

        return hp;
    }
}
