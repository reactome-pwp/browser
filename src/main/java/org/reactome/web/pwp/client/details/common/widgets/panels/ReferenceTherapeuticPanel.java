package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.model.client.classes.DatabaseIdentifier;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.ReferenceTherapeutic;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.Collections;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReferenceTherapeuticPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private ReferenceTherapeutic referenceTherapeutic;
    private DisclosurePanel disclosurePanel;

    public ReferenceTherapeuticPanel(ReferenceTherapeutic referenceTherapeutic) {
        this(null, referenceTherapeutic);
    }

    public ReferenceTherapeuticPanel(DetailsPanel parentPanel, ReferenceTherapeutic referenceTherapeutic) {
        super(parentPanel);
        this.referenceTherapeutic = referenceTherapeutic;
        initialize();
    }

    private void initialize(){
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.referenceTherapeutic.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.referenceTherapeutic;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.referenceTherapeutic.load(new ContentClientHandler.ObjectLoaded() {
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
        this.referenceTherapeutic = (ReferenceTherapeutic) data;

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");

        if(this.referenceTherapeutic.getAbbreviation()!=null) vp.add(getSingleValuePanel("Abbreviation", this.referenceTherapeutic.getAbbreviation()));

        if(this.referenceTherapeutic.getApproved()!=null) vp.add(getSingleValuePanel("Approved", this.referenceTherapeutic.getApproved()?"Yes":"No"));

        if(this.referenceTherapeutic.getApprovalSource()!=null) vp.add(getMultiValuePanel("Approval Source", this.referenceTherapeutic.getApprovalSource()));

        if(this.referenceTherapeutic.getType()!=null) vp.add(getSingleValuePanel("Type", this.referenceTherapeutic.getType()));

        List<String> names = this.referenceTherapeutic.getName();
        if(!names.isEmpty()) vp.add(getMultiValuePanel("Names", names ));

        TreeItem references = new TreeItem(SafeHtmlUtils.fromString("External cross-references"));

        DatabaseIdentifierPanel dbIdPanel = new DatabaseIdentifierPanel(referenceTherapeutic);
        TreeItem reference = dbIdPanel.asTreeItem();
        reference.setState(true, false);
        references.addItem(reference);

        if(!this.referenceTherapeutic.getCrossReference().isEmpty()){
            Collections.sort(referenceTherapeutic.getCrossReference());
            for (DatabaseIdentifier databaseIdentifier : this.referenceTherapeutic.getCrossReference()) {
                dbIdPanel = new DatabaseIdentifierPanel(databaseIdentifier);
                reference = dbIdPanel.asTreeItem();
                reference.setState(true, false);
                references.addItem(reference);
            }
        }

        Tree referencesTree = new Tree();
        referencesTree.clear();
        referencesTree.addItem(references);
        vp.add(referencesTree);

        if(vp.getWidgetCount()==0){
            vp.add(getErrorMessage("No more information available"));
        }

        this.disclosurePanel.setContent(vp);

        setLoaded(true);
    }

    private Widget getSingleValuePanel(String label, String value){
        HorizontalPanel hp = new HorizontalPanel();
        hp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label title = new Label(label + ":");
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        hp.add(title);
        hp.add(new Label(value));

        return hp;
    }

    private Widget getMultiValuePanel(String label, List<String> list){
        StringBuilder names = new StringBuilder();
        for (String name : list) {
            names.append(name);
            names.append(", ");
        }
        try{
            names.delete(names.length()-2, names.length()-1);
        }catch (StringIndexOutOfBoundsException e){
            //ToDo: Look into new Error Handling
            Console.error(getClass() + e.getMessage());
        }
        return getSingleValuePanel(label, names.toString());
    }
}
