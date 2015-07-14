package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Regulation;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;

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

    private void initialize(){
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
        if(!isLoaded())
            this.regulation.load(new DatabaseObjectLoadedHandler() {
                @Override
                public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                    setReceivedData(databaseObject);
                }

                @Override
                public void onDatabaseObjectError(Throwable trThrowable) {
                    disclosurePanel.setContent(getErrorMessage());
                }
            });
    }

    public void setReceivedData(DatabaseObject data) {
        this.regulation = (Regulation) data;

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");
        vp.addStyleName("elv-Details-OverviewDisclosure-content");

        vp.add(getNamesPanel(this.regulation.getName()));

        this.disclosurePanel.setContent(vp);
        setLoaded(true);
    }

    private Widget getNamesPanel(List<String> list){
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
        names.delete(names.length()-2, names.length()-1);
        hp.add(new Label(names.toString()));

        return hp;
    }
}
