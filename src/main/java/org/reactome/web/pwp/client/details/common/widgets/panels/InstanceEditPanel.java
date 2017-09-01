package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.InstanceEdit;
import org.reactome.web.pwp.model.client.classes.Person;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

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
            this.instanceEdit.load(new ContentClientHandler.ObjectLoaded() {
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

    @Override
    public DatabaseObject getDatabaseObject() {
        return instanceEdit;
    }

    public void setReceivedData(DatabaseObject data) {
        setLoaded(true);
        InstanceEdit instanceEdit = (InstanceEdit) data;
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setPaddingRight(5, Style.Unit.PX);
        for (Person person : instanceEdit.getAuthor()) {
            vp.add(new PersonPanel(this, person));
        }
        disclosurePanel.setContent(vp);
    }

}
