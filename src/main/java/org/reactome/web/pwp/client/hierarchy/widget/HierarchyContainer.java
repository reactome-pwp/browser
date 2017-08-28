package org.reactome.web.pwp.client.hierarchy.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyTreeSpeciesNotFoundException;
import org.reactome.web.pwp.model.client.classes.Species;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the loaded events hierarchies.
 * Keeps the state of every tree and improves the web application response time avoiding unnecessary reloads
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyContainer extends DeckPanel {
    private int index = 0;

    private HierarchyLoadingPanel hierarchyLoadingPanel;

    //keep track of the trees indexes in the DeckPanel (because a ScrollPanel wraps them)
    private Map<HierarchyTree, Integer> hierarchyTreeIndex = new HashMap<>();
    private Map<Species, HierarchyTree> speciesHierarchyTreeMap = new HashMap<>();

    public HierarchyContainer() {
        setStyleName(RESOURCES.getCSS().hierarchyContent());

        this.hierarchyLoadingPanel = new HierarchyLoadingPanel();
        add(this.hierarchyLoadingPanel);
        showWidget(index);
    }

    public void addHierarchyTree(Species species, HierarchyTree hierarchyTree){
        int index;
        if(isHierarchyTreeLoaded(species)){
            HierarchyTree aux = this.speciesHierarchyTreeMap.get(species);
            index = this.hierarchyTreeIndex.get(aux);
            //if are NOT the same -> remove the old one and insert the new in the same position
            if(aux!=hierarchyTree){
                remove(index);
                insert(hierarchyTree, index);
            }
        }else{
            index = ++this.index;
            ScrollPanel scrollPanel = new ScrollPanel(hierarchyTree);
            scrollPanel.getElement().getStyle().setProperty("borderRadius","0 15px 0 0");
            add(scrollPanel);
        }
        this.hierarchyTreeIndex.put(hierarchyTree, index);
        this.speciesHierarchyTreeMap.put(species, hierarchyTree);
        showWidget(index);
    }

    public boolean isHierarchyTreeLoaded(Species species){
        return this.speciesHierarchyTreeMap.containsKey(species);
    }

    public HierarchyTree getHierarchyTree(Species species) throws HierarchyTreeSpeciesNotFoundException {
        if(this.speciesHierarchyTreeMap.containsKey(species)){
            return this.speciesHierarchyTreeMap.get(species);
        }else{
            throw new HierarchyTreeSpeciesNotFoundException();
        }
    }

    public void showHierarchyTree(Species species) throws HierarchyTreeSpeciesNotFoundException {
        HierarchyTree ht = getHierarchyTree(species);
        int index = this.hierarchyTreeIndex.get(ht);
        showWidget(index);
    }

    public void showLoadingPanel(Species species){
        this.hierarchyLoadingPanel.showLoadingInfo(species);
        this.getWidgetIndex(this.hierarchyLoadingPanel);
        this.showWidget(0);
    }


    public static final Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("diagram-ObjectInfoPanel")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/hierarchy/widget/HierarchyContainerPanel.css";

        String hierarchyContent();
    }
}
