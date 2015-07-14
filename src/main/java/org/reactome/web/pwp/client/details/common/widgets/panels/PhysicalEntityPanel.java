package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.client.details.delegates.InstanceSelectedDelegate;
import org.reactome.web.pwp.model.classes.*;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PhysicalEntityPanel extends DetailsPanel implements OpenHandler<DisclosurePanel>, ClickHandler {
    private PhysicalEntity physicalEntity;
    private DisclosurePanel disclosurePanel;

    public PhysicalEntityPanel(PhysicalEntity physicalEntity) {
        this(null, physicalEntity, 1);
    }

    public PhysicalEntityPanel(DetailsPanel parentPanel, PhysicalEntity physicalEntity) {
        this(parentPanel, physicalEntity, 1);
    }

    public PhysicalEntityPanel(PhysicalEntity physicalEntity, int num) {
        this(null, physicalEntity, num);
    }

    public PhysicalEntityPanel(DetailsPanel parentPanel, PhysicalEntity physicalEntity, int num) {
        super(parentPanel);
        this.physicalEntity = physicalEntity;
        initialize(num);
    }

    private void initialize(int num){
        String displayName = physicalEntity.getDisplayName();
        if(num>1)
            displayName = num + " x " + displayName;
        disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(displayName, this);
        disclosurePanel.addOpenHandler(this);
        initWidget(disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.physicalEntity;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            this.physicalEntity.load(new DatabaseObjectLoadedHandler() {
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
        this.physicalEntity = (PhysicalEntity) data;
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-content");
        vp.setWidth("98%");

        if(!this.physicalEntity.getSpecies().isEmpty()){
            vp.add(new SpeciesPanel(this, this.physicalEntity.getSpecies()));
        }

        vp.add(getSynonymsPanel(this.physicalEntity.getName()));

        if(this.physicalEntity instanceof Complex){
            Complex complex = (Complex) this.physicalEntity;
            vp.add(getHasComponentsPanel("Has components:", complex.getHasComponent()));
        }

        if(this.physicalEntity instanceof EntitySet) {
            EntitySet entitySet = (EntitySet) this.physicalEntity;
            vp.add(getHasComponentsPanel("Has members:", entitySet.getHasMember()));
        }

        if(this.physicalEntity instanceof CandidateSet){
            CandidateSet candidateSet = (CandidateSet) this.physicalEntity;
            if(!candidateSet.getHasCandidate().isEmpty()){
                vp.add(getHasComponentsPanel("Has candidates:", candidateSet.getHasCandidate()));
            }
        }

        if(this.physicalEntity instanceof EntityWithAccessionedSequence){
            EntityWithAccessionedSequence ewas = (EntityWithAccessionedSequence) this.physicalEntity;
            vp.add(new EntityWithAccessionedSequencePanel(this, ewas));
        }

        if(!this.physicalEntity.getCrossReference().isEmpty()){
            vp.add(getCrossReferenceTree(this.physicalEntity.getCrossReference()));
        }

        disclosurePanel.setContent(vp);
        setLoaded(true);
    }

    @Override
    public void onClick(ClickEvent event) {
        event.stopPropagation();
        InstanceSelectedDelegate.get().instanceSelected(this.physicalEntity);
    }

    private Widget getCrossReferenceTree(List<DatabaseIdentifier> identifiers){
        Tree referencesTree = new Tree();
        TreeItem references = new TreeItem(SafeHtmlUtils.fromString("External cross-references"));
        for (DatabaseIdentifier databaseIdentifier : identifiers) {
            DatabaseIdentifierPanel dbIdPanel = new DatabaseIdentifierPanel(this, databaseIdentifier);
            TreeItem reference = dbIdPanel.asTreeItem();
            reference.setState(true, false);
            references.addItem(reference);
        }
        referencesTree.clear();
        referencesTree.addItem(references);

        return referencesTree;
    }

    private Widget getHasComponentsPanel(String title, List<PhysicalEntity> components){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label label = new Label(title);
        Style titleStyle = label.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(label);

        Map<PhysicalEntity, Integer> map = new HashMap<PhysicalEntity, Integer>();
        for (PhysicalEntity physicalEntity : components) {
            int num = 1;
            if(map.containsKey(physicalEntity)){
                num = map.get(physicalEntity) + 1;
            }
            map.put(physicalEntity, num);
        }

        for (PhysicalEntity entity : map.keySet()) {
            DetailsPanel p = new PhysicalEntityPanel(this, entity, map.get(entity));
            p.setWidth("99%");
            p.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(p);
        }

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
