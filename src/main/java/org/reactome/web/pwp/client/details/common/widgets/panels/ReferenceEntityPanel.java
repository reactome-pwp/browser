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
import org.reactome.web.pwp.model.client.classes.ReferenceEntity;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.Collections;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReferenceEntityPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private ReferenceEntity referenceEntity;
    private DisclosurePanel disclosurePanel;

    public ReferenceEntityPanel(ReferenceEntity referenceEntity) {
        this(null, referenceEntity);
    }

    public ReferenceEntityPanel(DetailsPanel parentPanel, ReferenceEntity referenceEntity) {
        super(parentPanel);
        this.referenceEntity = referenceEntity;
        initialize();
    }

    private void initialize(){
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.referenceEntity.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.referenceEntity;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.referenceEntity.load(new ContentClientHandler.ObjectLoaded() {
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
        this.referenceEntity = (ReferenceEntity) data;

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");

        List<String> names = this.referenceEntity.getName();
        if(!names.isEmpty()){
            vp.add(getNamesPanel(names));
        }

        TreeItem references = new TreeItem(SafeHtmlUtils.fromString("External cross-references"));

        DatabaseIdentifierPanel dbIdPanel = new DatabaseIdentifierPanel(referenceEntity);
        TreeItem reference = dbIdPanel.asTreeItem();
        reference.setState(true, false);
        references.addItem(reference);

        if(!this.referenceEntity.getCrossReference().isEmpty()){
            Collections.sort(referenceEntity.getCrossReference());
            for (DatabaseIdentifier databaseIdentifier : this.referenceEntity.getCrossReference()) {
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
        try{
            names.delete(names.length()-2, names.length()-1);
        }catch (StringIndexOutOfBoundsException e){
            //ToDo: Look into new Error Handling
            Console.error(getClass() + e.getMessage());
        }
        hp.add(new Label(names.toString()));

        return hp;
    }
}
