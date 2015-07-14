package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.InstanceEdit;
import org.reactome.web.pwp.model.classes.Person;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InstanceEditPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private InstanceEdit instanceEdit;
    private DisclosurePanel disclosurePanel;

    public InstanceEditPanel(InstanceEdit instanceEdit) {
        this(null, instanceEdit);
    }

    public InstanceEditPanel(DetailsPanel parentPanel, InstanceEdit instanceEdit) {
        super(parentPanel);
        this.instanceEdit = instanceEdit;
        initialize();
    }

    private void initialize(){
        disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(instanceEdit.getDisplayName());
        disclosurePanel.addOpenHandler(this);
        initWidget(disclosurePanel);
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.instanceEdit.load(new DatabaseObjectLoadedHandler() {
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

    @Override
    public DatabaseObject getDatabaseObject() {
        return instanceEdit;
    }

    public void setReceivedData(DatabaseObject data) {
        setLoaded(true);
        InstanceEdit instanceEdit = (InstanceEdit) data;
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-content");
        vp.setWidth("100%");
        vp.getElement().getStyle().setPaddingRight(5, Style.Unit.PX);
        for (Person person : instanceEdit.getAuthor()) {
            vp.add(new PersonPanel(this, person));
        }
        disclosurePanel.setContent(vp);
    }

}
