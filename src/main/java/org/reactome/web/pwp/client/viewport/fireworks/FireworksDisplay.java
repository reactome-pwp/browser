package org.reactome.web.pwp.client.viewport.fireworks;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.client.FireworksViewer;
import org.reactome.web.fireworks.events.NodeHoverEvent;
import org.reactome.web.fireworks.events.NodeOpenedEvent;
import org.reactome.web.fireworks.events.NodeSelectedEvent;
import org.reactome.web.fireworks.events.ProfileChangedEvent;
import org.reactome.web.fireworks.handlers.*;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosureImages;
import org.reactome.web.pwp.model.client.classes.Pathway;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksDisplay extends DockLayoutPanel implements Fireworks.Display, AnalysisResetHandler,
        NodeHoverHandler, NodeSelectedHandler, NodeSelectedResetHandler, NodeHoverResetHandler, NodeOpenedHandler,
        ProfileChangedHandler, NodeFlaggedResetHandler {

    private FireworksViewer fireworks;
    private Fireworks.Presenter presenter;
    private List<HandlerRegistration> handlers;

    private AnalysisStatus analysisStatus = new AnalysisStatus();

    private Pathway toHighlight;
    private Pathway toSelect;
    private Pathway toOpen;
    private String flag;

    public FireworksDisplay() {
        super(Style.Unit.PX);
        this.add(getLoadingMessage());
        this.handlers = new LinkedList<>();
    }

    @Override
    public void loadSpeciesFireworks(String speciesJson) {
        this.removeHandlers(); //Needed to allow the garbage collection to get rid of previous instances of fireworks
//        FireworksFactory.EVENT_BUS_VERBOSE = true;
        this.fireworks = FireworksFactory.createFireworksViewer(speciesJson);
        handlers.add(this.fireworks.addAnalysisResetHandler(this));
        handlers.add(this.fireworks.addNodeFlaggedResetHandler(this));
        handlers.add(this.fireworks.addNodeHoverHandler(this));
        handlers.add(this.fireworks.addNodeOpenedHandler(this));
        handlers.add(this.fireworks.addNodeSelectedHandler(this));
        handlers.add(this.fireworks.addNodeSelectedResetHandler(this));
        handlers.add(this.fireworks.addNodeHoverResetHandler(this));
        handlers.add(this.fireworks.addProfileChangedHandler(this));
        this.clear();
        this.add(this.fireworks);
        //We need the fireworks to be rendered before applying the carried actions
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                applyCarriedActions();
            }
        });
    }

    @Override
    public void flag(String flag) {
        if (this.fireworks != null) {
            this.fireworks.flagItems(flag);
        }
        this.flag = flag;
    }

    @Override
    public void setPresenter(Fireworks.Presenter presenter) {
        this.presenter = presenter;
    }

    private void applyCarriedActions(){
        //ORDER IS IMPORTANT IN THE FOLLOWING CONDITIONS
        if(this.toOpen!=null){
            this.fireworks.openPathway(this.toOpen.getDbId());
            this.toOpen = null;
            this.toSelect = null;
            this.toHighlight = null;
        }
        if(this.toSelect!=null){
            this.fireworks.selectNode(this.toSelect.getDbId());
            this.toSelect = null;
        }
        if(this.toHighlight!=null){
            this.fireworks.highlightNode(this.toHighlight.getDbId());
            this.toHighlight = null;
        }
        if(this.flag!=null){
            this.fireworks.flagItems(this.flag);
            this.flag = null;
        }
        //Please note there is no checking to analysisStatus because reset is an option :)
        this.fireworks.setAnalysisToken(this.analysisStatus.getToken(), this.analysisStatus.getResource());
    }

    @Override
    public void highlightPathway(Pathway pathway) {
        if(pathway==null) return;
        if(this.fireworks!=null) {
            this.fireworks.highlightNode(pathway.getDbId());
        }
        this.toHighlight = pathway;
    }

    @Override
    public void resetHighlight() {
        if(this.fireworks!=null) {
            this.fireworks.resetHighlight();
        }
        this.toHighlight = null;
    }

    @Override
    public void openPathway(Pathway pathway) {
        if(this.fireworks!=null) {
            this.fireworks.openPathway(pathway.getDbId());
        }
        this.toOpen = pathway;
    }

    @Override
    public void setAnalysisToken(AnalysisStatus analysisStatus) {
        if(!Objects.equals(this.analysisStatus, analysisStatus)) {
            this.analysisStatus = analysisStatus;
            if(this.fireworks!=null) {
                if (analysisStatus.isEmpty()) {
                    fireworks.resetAnalysis();
                } else {
                    fireworks.setAnalysisToken(analysisStatus.getToken(), analysisStatus.getResource());
                }
            }
        }
    }

    @Override
    public void selectPathway(Pathway pathway) {
        if(pathway==null){
            if(this.fireworks!=null) {
                this.fireworks.resetSelection();
                this.fireworks.showAll();
            }
            return;
        }

        if(this.fireworks!=null) {
            this.fireworks.selectNode(pathway.getDbId());
        }
        this.toSelect = pathway;
    }

    /**
     * Gets rid of current possible existing handlers to the fireworks object, so the garbage collector will
     * clean the memory and this class wont be listening to previous deleted instances of Fireworks
     */
    private void removeHandlers(){
        for (HandlerRegistration handler : handlers) {
            handler.removeHandler();
        }
        handlers.clear();
    }

    @Override
    public void resetSelection() {
        if(fireworks!=null) {
            this.fireworks.resetSelection();
        }
    }

    @Override
    public void resetView() {
        this.removeHandlers(); //Needed to allow the garbage collection to get rid of previous instances of fireworks
        this.fireworks = null;
        this.clear();
        this.add(getLoadingMessage());
    }

    @Override
    public void onAnalysisReset() {
        this.presenter.resetAnalysis();
    }

    @Override
    public void onNodeFlaggedReset() {
        this.presenter.resetFlag();
    }

    @Override
    public void onNodeHover(NodeHoverEvent event) {
        this.presenter.highlightPathway(event.getNode().getDbId());
    }

    @Override
    public void onNodeOpened(NodeOpenedEvent event) {
        this.presenter.showPathwayDiagram(event.getNode().getDbId());
    }

    @Override
    public void onNodeSelected(NodeSelectedEvent event) {
        this.presenter.selectPathway(event.getNode().getDbId());
    }

    @Override
    public void onNodeSelectionReset() {
        this.presenter.resetPathwaySelection();
    }

    @Override
    public void onNodeHoverReset() {
        this.presenter.resetPathwayHighlighting();
    }

    @Override
    public void onProfileChanged(ProfileChangedEvent event) {
        this.presenter.profileChanged(event.getProfile().getName());
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(this.fireworks!=null) {
            this.fireworks.setVisible(visible);
        }
    }

    /**
     * Getting a panel with loading message and symbol.
     * @return Widget
     */
    private Widget getLoadingMessage(){
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Image(DisclosureImages.INSTANCE.getLoadingImage()));
        hp.add(new HTMLPanel("Loading pathways overview graph..."));
        hp.setSpacing(5);

        return hp;
    }
}
