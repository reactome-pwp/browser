package org.reactome.web.pwp.client.details.tabs.processes.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.tabs.processes.events.ProcessesDataHandler;
import org.reactome.web.pwp.client.details.tabs.processes.events.ProcessesDataListener;
import org.reactome.web.pwp.client.details.tabs.processes.model.ProcessesPanel;
import org.reactome.web.pwp.client.details.tabs.processes.model.widgets.container.*;
import org.reactome.web.pwp.client.details.type.DetailsTabType;
import org.reactome.web.pwp.model.classes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ProcessesViewImpl implements ProcessesView, ProcessesDataHandler{
    private final DetailsTabType TYPE = DetailsTabType.PARTICIPATING_PROCESSES;
    Presenter presenter;

    private DockLayoutPanel tab;
    private HTMLPanel title;


    private Map<Class, ProcessesContainer> containers = new HashMap<Class, ProcessesContainer>();

    private Map<DatabaseObject, ProcessesPanel> processesPanelLoaded = new HashMap<DatabaseObject, ProcessesPanel>();
    private ProcessesPanel currentPanel;

    public ProcessesViewImpl() {
        title = new HTMLPanel(TYPE.getTitle());
        tab = new DockLayoutPanel(Style.Unit.PX);

        //TODO: COMMENT!
        ProcessesDataListener.getProcessesDataListener().setHandler(this);
    }

    @Override
    public Widget asWidget() {
        return tab;
    }

    @Override
    public DetailsTabType getDetailTabType() {
        return TYPE;
    }

    @Override
    public HTMLPanel getTitle() {
        return title;
    }

    @Override
    public void onComplexesRequired(ComplexesContainer container, PhysicalEntity physicalEntity) {
        this.containers.put(ComplexesContainer.class, container);
        this.presenter.getComplexesContaining(physicalEntity);
    }

    @Override
    public void onEntitySetRequired(EntitySetContainer container, PhysicalEntity physicalEntity) {
        this.containers.put(EntitySetContainer.class, container);
        this.presenter.getEntitySetsContaining(physicalEntity);
    }

    @Override
    public void onPathwaysForEntitiesRequired(InvolvedPathwaysContainer container, PhysicalEntity physicalEntity) {
        this.containers.put(InvolvedPathwaysContainer.class, container);
        this.presenter.getPathwaysForEntities(physicalEntity);
    }

    @Override
    public void onPathwaysForEventsRequired(InvolvedPathwaysContainer container, Event event) {
        this.containers.put(InvolvedPathwaysContainer.class, container);
        this.presenter.getPathwaysForEvent(event);
    }

    @Override
    public void onReactionsWhereInputRequired(ReactionsAsInputContainer container, PhysicalEntity physicalEntity) {
        this.containers.put(ReactionsAsInputContainer.class, container);
        this.presenter.getReactionsWhereInput(physicalEntity);
    }

    @Override
    public void onReactionsWhereOutputRequired(ReactionsAsOutputContainer container, PhysicalEntity physicalEntity) {
        this.containers.put(ReactionsAsOutputContainer.class, container);
        this.presenter.getReactionsWhereOutput(physicalEntity);
    }

    @Override
    public void onOtherFormsOfEWASRequired(OtherFormsForEwasContainer container, EntityWithAccessionedSequence ewas) {
        this.containers.put(OtherFormsForEwasContainer.class, container);
        this.presenter.getOtherFormsForEWAS(ewas);
    }

    @Override
    public void setInitialState() {
        this.title.getElement().setInnerHTML(TYPE.getTitle());
        this.tab.clear();
        this.tab.add(new HTMLPanel("A context-sensitive panel for what you have clicked in the diagram or hierarchy. " +
                "If an event is clicked, pathways this event is involved in will be listed. If a molecule is " +
                "clicked, events and states of this molecule will be displayed."));
    }

    @Override
    public void setComplexesForPhysicalEntity(PhysicalEntity physicalEntity, List<Complex> complexList) {
        ComplexesContainer container = (ComplexesContainer) this.containers.remove(ComplexesContainer.class);
        if(container!=null){
            container.onComplexesRetrieved(complexList);
        }
    }

    @Override
    public void setDetailedData(DatabaseObject databaseObject) {
        this.currentPanel = new ProcessesPanel(databaseObject);
        this.processesPanelLoaded.put(databaseObject, this.currentPanel);
        this.showProcessesPanel(this.currentPanel);
    }

    @Override
    public void setEntitySetsForPhysicalEntity(PhysicalEntity physicalEntity, List<EntitySet> entitySetList) {
        EntitySetContainer container = (EntitySetContainer) this.containers.remove(EntitySetContainer.class);
        if(container!=null){
            container.onEntitySetsRetrieved(entitySetList);
        }
    }

    @Override
    public void setOtherFormsForEWAS(EntityWithAccessionedSequence ewas, List<EntityWithAccessionedSequence> ewasList) {
        OtherFormsForEwasContainer container = (OtherFormsForEwasContainer) this.containers.remove(OtherFormsForEwasContainer.class);
        if(container!=null){
            container.onOtherFormsForEWASRetrieved(ewasList);
        }
    }

    @Override
    public void setPathwayForPhysicalEntity(PhysicalEntity physicalEntity, List<Pathway> pathwayList) {
        InvolvedPathwaysContainer container = (InvolvedPathwaysContainer) this.containers.remove(InvolvedPathwaysContainer.class);
        if(container!=null){
            container.onPathwaysRetrieved(pathwayList);
        }
    }

    @Override
    public void setPathwayForEvent(Event event, List<Pathway> pathwayList) {
        InvolvedPathwaysContainer container = (InvolvedPathwaysContainer) this.containers.remove(InvolvedPathwaysContainer.class);
        if(container!=null){
            container.onPathwaysRetrieved(pathwayList);
        }
    }

    @Override
    public void setReactionsWhereInput(PhysicalEntity physicalEntity, List<ReactionLikeEvent> reactionList) {
        ReactionsAsInputContainer container = (ReactionsAsInputContainer) this.containers.remove(ReactionsAsInputContainer.class);
        if(container!=null){
            container.onReactionsRetrieved(reactionList);
        }
    }

    @Override
    public void setReactionsWhereOutput(PhysicalEntity physicalEntity, List<ReactionLikeEvent> reactionList) {
        ReactionsAsOutputContainer container = (ReactionsAsOutputContainer) this.containers.remove(ReactionsAsOutputContainer.class);
        if(container!=null){
            container.onReactionsRetrieved(reactionList);
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        if(!this.showInstanceDetailsIfExists(pathway, databaseObject)){
            this.showWaitingMessage();
            //TODO: Improve this bit making the presenter in charge of the "toShow" object
            DatabaseObject toShow = databaseObject!=null?databaseObject:pathway;
            this.presenter.getDetailedData(toShow);
        }
    }

    @Override
    public boolean showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        DatabaseObject toShow = databaseObject!=null?databaseObject:pathway;
        if(this.processesPanelLoaded.containsKey(toShow)){
            this.currentPanel = this.processesPanelLoaded.get(toShow);
            this.showProcessesPanel(this.currentPanel);
            return true;
        }else{
            this.setInitialState();
            return false;
        }
    }

    private void showProcessesPanel(ProcessesPanel panel){
        //title.getElement().setInnerHTML(TYPE.getTitle() + " (" + ?? +")");
        this.tab.clear();
        this.tab.add(panel);
    }

    private void showWaitingMessage(){
        this.currentPanel = null;

        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading processes data, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.tab.clear();
        this.tab.add(message);
    }

//    private void showErrorMessage(String message){
//        HorizontalPanel panel = new HorizontalPanel();
//        Image loader = new Image(ReactomeImages.INSTANCE.exclamation());
//        panel.add(loader);
//
//        Label label = new Label(message);
//        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
//        panel.add(label);
//
//        this.tab.clear();
//        this.tab.add(panel);
//    }
}
