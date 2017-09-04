package org.reactome.web.pwp.client.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemDoubleClickedEvent;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemMouseOverEvent;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyTreeSpeciesNotFoundException;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemDoubleClickedHandler;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOutHandler;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOverHandler;
import org.reactome.web.pwp.client.hierarchy.widget.HierarchyContainer;
import org.reactome.web.pwp.client.hierarchy.widget.HierarchyItem;
import org.reactome.web.pwp.client.hierarchy.widget.HierarchyTree;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.classes.Species;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.web.pwp.model.client.util.Path;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyDisplay extends Composite implements OpenHandler<TreeItem>, SelectionHandler<TreeItem>,
        Hierarchy.Display, HierarchyItemMouseOverHandler, HierarchyItemMouseOutHandler, HierarchyItemDoubleClickedHandler {

    private Hierarchy.Presenter presenter;

    private HierarchyTree hierarchyTree;
    private HierarchyContainer hierarchyContainer;

    public HierarchyDisplay() {
        DockLayoutPanel container = new DockLayoutPanel(Style.Unit.PX);

        this.hierarchyContainer = new HierarchyContainer();
        this.hierarchyContainer.addStyleName(RESOURCES.getCSS().hierarchyPanel());

        FlowPanel title = new FlowPanel();
        title.add(new Image(RESOURCES.hierarchyIcon()));
        title.add(new InlineLabel("Event Hierarchy:"));
        title.setStyleName(RESOURCES.getCSS().hierarchyPanelTitle());
        container.addNorth(title, 25);
        container.add(this.hierarchyContainer);
        initWidget(container);

        container.getParent().addStyleName(RESOURCES.getCSS().hierarchyContainer());
    }

    @Override
    public void onHierarchyItemDoubleClicked(HierarchyItemDoubleClickedEvent e) {
        Event event = e.getItem().getEvent();
        if(event instanceof Pathway) {
            this.presenter.openDiagram((Pathway) event);
        }
    }

    @Override
    public void onHierarchyItemHoveredReset() {
        this.presenter.eventHoveredReset();
    }

    @Override
    public void onHierarchyItemMouseOver(HierarchyItemMouseOverEvent e) {
        HierarchyItem item = e.getItem();
        if (item == null) return;

        HierarchyItem pwd = item.getParentWithDiagram();
        Pathway pathway = (Pathway) pwd.getEvent();
        Event event = item.getEvent();
        if (event.equals(pathway)) event = null;
        Path path = pwd.getPath();
        this.presenter.eventHovered(pathway, event, path);
    }

    @Override
    public void onOpen(OpenEvent<TreeItem> event) {
        HierarchyItem treeItem = (HierarchyItem) event.getTarget();
        this.onOpen(treeItem);
    }

    @Override
    public void onSelection(SelectionEvent<TreeItem> selectionEvent) {
        HierarchyItem item = (HierarchyItem) selectionEvent.getSelectedItem();
        if (item == null) return;

        this.hierarchyTree.clearHighlights();
        item.highlightPath();

        HierarchyItem pwd = item.getParentWithDiagram();
        if (pwd != null) {
            final Pathway pathway = (Pathway) pwd.getEvent();
            Event aux = item.getEvent();
            final Event event = pathway.equals(aux) ? null : aux;
            final Path path = pwd.getPath();
            //This is needed because the State will be checking the species of the pathway and it needs to be ready
            pathway.load(new ContentClientHandler.ObjectLoaded() {
                @Override
                public void onObjectLoaded(DatabaseObject databaseObject) {
                    presenter.eventSelected((Pathway) databaseObject, event, path);
                }

                @Override
                public void onContentClientException(Type type, String message) {
                    Console.error("There has been an error retrieving data for " + pathway.getDisplayName(), HierarchyDisplay.this);
                }

                @Override
                public void onContentClientError(ContentClientError error) {
                    Console.error("There has been an error retrieving data for " + pathway.getDisplayName(), HierarchyDisplay.this);
                }
            });

        } else {
            Console.error("Events hierarchy: No pathway with diagram found in the path.", this);
        }
    }

    @Override
    public void clearAnalysisResult() {
        if(this.hierarchyTree == null) return;
        for (HierarchyItem item : this.hierarchyTree.getHierarchyItems()) {
            item.clearAnalysisData();
        }
    }

    @Override
    public void expandPathway(Path path, Pathway pathway) {
        if (this.hierarchyTree == null) return;
        HierarchyItem treeItem = this.hierarchyTree.getHierarchyItemByDatabaseObject(path, pathway);
        if (treeItem != null) {
            treeItem.setState(true, false);
            this.onOpen(treeItem);
        } else {
            Console.error(getClass() + " could not find the node for " + pathway, this);
        }
    }

    @Override
    public Set<Pathway> getLoadedPathways() {
        Set<Pathway> pathways = new HashSet<>();
        if(this.hierarchyTree == null) return pathways;
        for (HierarchyItem item : this.hierarchyTree.getHierarchyItems()) {
            if(item.getEvent() instanceof Pathway){
                pathways.add((Pathway) item.getEvent());
            }
        }
        return pathways;
    }

    @Override
    public Set<Pathway> getPathwaysWithLoadedReactions() {
        Set<Pathway> pathways = new HashSet<>();
        if(hierarchyTree==null) return pathways;
        return hierarchyTree.getHierarchyPathwaysWithReactionsLoaded();
    }


    @Override
    public void highlightHitReactions(Set<Long> reactionsHit) {
        if(hierarchyTree!=null){
            hierarchyTree.highlightHitReactions(reactionsHit);
        }
    }

    @Override
    public void setPresenter(Hierarchy.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void show(Species species) {
        try {
            this.hierarchyTree = this.hierarchyContainer.getHierarchyTree(species);
            this.hierarchyContainer.showHierarchyTree(species);
            this.presenter.hierarchyChanged(species);
        } catch (HierarchyTreeSpeciesNotFoundException e) {
            this.hierarchyContainer.showLoadingPanel(species);
            this.presenter.retrieveData(species);
        }
    }

    @Override
    public void showAnalysisResult(List<PathwaySummary> pathwaySummaries) {
        if (hierarchyTree != null) {
            this.hierarchyTree.showAnalysisData(pathwaySummaries);
        }
    }

    @Override
    public void select(Event event, Path path) {
        this.hierarchyTree.clearHighlights();
        if (event != null) {
            HierarchyItem item = this.hierarchyTree.getHierarchyItemByDatabaseObject(path, event);
            if (item != null) {
                this.hierarchyTree.ensureItemVisible(item);
                item.setSelected(true);
                item.highlightPath();
            } else {
                Console.error("Cannot find " + event);
            }
        }
    }


    @Override
    public void setData(Species species, List<? extends Event> tlps) {
        this.hierarchyTree = new HierarchyTree(species);
        this.hierarchyTree.getElement().getStyle().setProperty("borderRadius","0 15px 0 0");
        this.hierarchyTree.setScrollOnSelectEnabled(true);
        this.hierarchyTree.setAnimationEnabled(true);
        this.hierarchyTree.addHierarchyItemDoubleClickedHandler(this);
        this.hierarchyTree.addHierarchyItemMouseOverHandler(this);
        this.hierarchyTree.addHierarchyItemMouseOutHandler(this);
        this.hierarchyTree.addOpenHandler(this);
        this.hierarchyTree.addSelectionHandler(this);
        this.hierarchyContainer.addHierarchyTree(species, this.hierarchyTree);

        try {
            this.hierarchyTree.loadPathwayChildren(null, tlps);
            this.presenter.hierarchyChanged(species);
        } catch (Exception e) {
            Console.error(e.getLocalizedMessage());
        }
    }



    private void onOpen(final HierarchyItem item) {
        final Pathway pathway = (Pathway) item.getEvent();
        if (!item.isChildrenLoaded()) {
            pathway.load(new ContentClientHandler.ObjectLoaded() {
                @Override
                public void onObjectLoaded(DatabaseObject databaseObject) {
                    Pathway pathway = databaseObject.cast();
                    try {
                        hierarchyTree.loadPathwayChildren(item, pathway.getHasEvent());
                        presenter.pathwayExpanded(pathway);
                    } catch (Exception e) {
                        Console.error(e.getLocalizedMessage());
                    }
                }

                @Override
                public void onContentClientException(Type type, String message) {
                    Console.error("Error loading " + pathway, HierarchyDisplay.this);
                }

                @Override
                public void onContentClientError(ContentClientError error) {
                    Console.error("Error loading " + pathway, HierarchyDisplay.this);
                }
            });
        } else {
            this.presenter.pathwayExpanded(pathway);
        }
        item.setState(true, false);
    }
    public static Resources RESOURCES;

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

        @Source("images/hierarchy.png")
        ImageResource hierarchyIcon();

        @Source("images/EHLDPathway.png")
        ImageResource ehldPathway();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-Hierarchy")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/hierarchy/Hierarchy.css";

        String hierarchyPanelTitle();

        String hierarchyContainer();

        String hierarchyPanel();
    }
}
