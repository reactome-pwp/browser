package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.client.details.delegates.MoleculeSelectedListener;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Molecule;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.PhysicalToReferenceEntityMap;
import org.reactome.web.pwp.model.client.classes.DatabaseIdentifier;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.web.pwp.model.client.factory.SchemaClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculePanel extends DetailsPanel implements OpenHandler<DisclosurePanel>, ClickHandler {
    //private Molecule referenceEntity;
    private Molecule molecule;
    private DisclosurePanel disclosurePanel;
    private String title;
    Set<PhysicalToReferenceEntityMap> physicalEntities;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MoleculePanel(Molecule molecule, Set<PhysicalToReferenceEntityMap> physicalEntities) {
        this(null, molecule, physicalEntities);
    }

    public MoleculePanel(DetailsPanel parentPanel, Molecule molecule, Set<PhysicalToReferenceEntityMap> physicalEntities) {
        super(parentPanel);
        this.molecule = molecule;
        this.physicalEntities = physicalEntities;
        initialize();
    }

    private void initialize(){
        FlowPanel overview = new FlowPanel();
        overview.setTitle("Show further information about external references");
        this.setTitle(molecule.getDbId().toString());

        //Name of reference.
        List<String> refNames = this.molecule.getName();
        String refName;
        if(refNames == null || refNames.size() == 0){
            refName = this.molecule.getDisplayName();
        }else{
            refName = refNames.get(0);
        }
        refName = refName + " (" + molecule.getOccurrenceInPathway() + "x)";
        TextPanel name = new TextPanel(refName);
        name.setTitle("refDbID, Name, Occurrences in Pathway");

        //Link to external reference database.
        String identifier  = this.molecule.getIdentifier();
        //String string = this.referenceEntity.getReferenceDatabase().getUrl(); => NULL
        Anchor ref = new Anchor(identifier, this.molecule.getUrl(), "_blank");
        ref.setTitle("Link to main external reference DB");
        ref.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Window.open(molecule.getUrl(), "_blank", "");
            }
        });

        overview.add(ref); //First adding ref because length of name differs enormously!!!
        ref.asWidget().getElement().getStyle().setFloat(Style.Float.LEFT);
        ref.asWidget().getElement().getStyle().setColor("blue");
        ref.asWidget().getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        ref.addStyleName("elv-Details-Reference-MoleculesRow");

        ref.getElement().appendChild(new Image(CommonImages.INSTANCE.externalLink()).getElement());

        /*Changes for disease flag
        if(molecule.isDisease()){
            overview.add(new Image(ReactomeImages.INSTANCE.isDisease()));
        }*/

        overview.add(name);
        name.addStyleName("elv-Details-Name-MoleculesRow");

        overview.setHeight("99%");

        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(overview, this);
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.molecule;
    }

    /**
     * If further information about external references for a molecule is required it has to be received from
     * the RESTful and will be done by DataRequiredListener/Handler.
     * @param event OpenEvent
     */
    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded()){
            this.molecule.load(new ContentClientHandler.ObjectLoaded() {
                @Override
                public void onObjectLoaded(DatabaseObject databaseObject) {
                    setReceivedMoleculeData((Molecule) databaseObject);
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
    }

    /**
     * Once further information about external references for a molecule have been received from the RESTful
     * this data has to be set in the few.
     * @param data molecule with additional attributes set.
     */
    public void setReceivedMoleculeData(Molecule data){
        this.molecule = this.molecule.addData(data);

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");

        //Building up the tree for External cross-references.
        if(!this.molecule.getCrossReference().isEmpty()){
            Tree referencesTree = new Tree();
            TreeItem references = new TreeItem(SafeHtmlUtils.fromString("External cross-references"));
            for (DatabaseIdentifier databaseIdentifier : this.molecule.getCrossReference()) {
                DatabaseIdentifierPanel dbIdPanel = new DatabaseIdentifierPanel(databaseIdentifier);
                TreeItem reference = dbIdPanel.asTreeItem();
                references.addItem(reference);
                reference.setState(true, false);
            }
            references.setState(true);
            referencesTree.clear();
            referencesTree.addItem(references);
            vp.add(referencesTree);
        }

        if(vp.getWidgetCount()==0){
            vp.add(getErrorMessage("There is no more information available."));
        }

        this.disclosurePanel.setContent(vp);

        setLoaded(true);
    }

    /**
     * Once the icon of a Molecule is clicked, all the PhysicalEntities need to be found and the first one has to be
     * selected in the diagram. All the entities are forwarded to MoleculeSelectedListner to manage circling through
     * all the occurrences.
     * @param clickEvent ClickEvent
     */
    @Override
    public void onClick(ClickEvent clickEvent) {
        List<PhysicalToReferenceEntityMap> select = new ArrayList<>();
        List<PhysicalToReferenceEntityMap> highlight = new ArrayList<>();
        for(PhysicalToReferenceEntityMap phyEntity : physicalEntities){

            if(phyEntity.getSchemaClass() != SchemaClass.COMPLEX && phyEntity.getSchemaClass() != SchemaClass.CANDIDATE_SET
                    && phyEntity.getSchemaClass() != SchemaClass.DEFINED_SET && phyEntity.getSchemaClass() != SchemaClass.ENTITY_SET
                    && phyEntity.getSchemaClass() != SchemaClass.OPEN_SET){

                    select.add(0, phyEntity);

            }else{
                highlight.add(phyEntity);
            }

        }

        select.addAll(highlight);
        clickEvent.stopPropagation();
        MoleculeSelectedListener.getMoleculeSelectedListener().moleculeSelected(select);
    }
}
