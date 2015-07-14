package org.reactome.web.pwp.client.manager.state;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.*;
import org.reactome.web.pwp.client.common.handlers.*;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.manager.state.token.Token;
import org.reactome.web.pwp.client.manager.state.token.TokenMalformedException;
import org.reactome.web.pwp.client.manager.title.TitleManager;
import org.reactome.web.pwp.client.tools.analysis.event.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisCompletedHandler;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.util.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StateManager implements BrowserModule.Manager, ValueChangeHandler<String>,
        State.StateLoadedHandler, StateChangedHandler, DatabaseObjectSelectedHandler, DetailsTabChangedHandler,
        AnalysisCompletedHandler, AnalysisResetHandler, ToolSelectedHandler {

    private EventBus eventBus;

    private State currentState;

    public StateManager(EventBus eventBus) {
        this.eventBus = eventBus;
        new TitleManager(eventBus);
        History.addValueChangeHandler(this);

        //The orthology manager can also fire state changed events
        this.eventBus.addHandler(StateChangedEvent.TYPE, this);
        this.eventBus.addHandler(DatabaseObjectSelectedEvent.TYPE, this);
        this.eventBus.addHandler(DetailsTabChangedEvent.TYPE, this);
        this.eventBus.addHandler(ToolSelectedEvent.TYPE, this);
        this.eventBus.addHandler(AnalysisCompletedEvent.TYPE, this);
        this.eventBus.addHandler(AnalysisResetEvent.TYPE, this);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String value = event.getValue();
        try {
            Token token = new Token(value);
            if (token.isWellFormed()) {
                new State(token, this);
            } else {
                this.tokenError(value);
            }
        } catch (TokenMalformedException e) {
            this.tokenError(value);
        }
    }

    @Override
    public void onAnalysisCompleted(AnalysisCompletedEvent event) {
        State desiredState = new State(this.currentState);
        desiredState.setDetailsTab(DetailsTabType.ANALYSIS);
        desiredState.setAnalysisToken(event.getAnalysisResult().getSummary().getToken());
        this.eventBus.fireEventFromSource(new StateChangedEvent(desiredState), this);
    }

    @Override
    public void onAnalysisReset() {
        State desiredState = new State(this.currentState);
        desiredState.setAnalysisToken(null);
        this.eventBus.fireEventFromSource(new StateChangedEvent(desiredState), this);
    }

    @Override
    public void onDatabaseObjectSelected(DatabaseObjectSelectedEvent event) {
        Selection newSelection = event.getSelection();
        Selection currentSelection = new Selection(currentState);
        if(!currentSelection.equals(newSelection)){
            Pathway diagram = newSelection.getDiagram();
            this.currentState.setPathway(diagram);

            DatabaseObject object = newSelection.getDatabaseObject();
            this.currentState.setSelected(object);

            Path path = newSelection.getPath();
            this.currentState.setPath(path);

            this.eventBus.fireEventFromSource(new StateChangedEvent(this.currentState), this);
            History.newItem(this.currentState.toString(), false);
        }
    }

    @Override
    public void onDetailsTabChanged(DetailsTabChangedEvent event) {
        State desiredState = new State(this.currentState);
        desiredState.setDetailsTab(event.getDetailsTab());
        this.eventBus.fireEventFromSource(new StateChangedEvent(desiredState), this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();
        if (!this.currentState.equals(state)) {
            this.currentState = state;
            History.newItem(state.toString(), false);
        }
    }

    @Override
    public void onStateLoaded(State state) {
        if (state == null) {
            this.eventBus.fireEventFromSource(new ErrorMessageEvent("The data in the URL can not be fit into a state"), this);
        } else if (!state.equals(this.currentState)) {
            this.currentState = state;
            this.eventBus.fireEventFromSource(new StateChangedEvent(state), this);
        }
    }

    @Override
    public void onToolSelected(ToolSelectedEvent event) {
        State desiredState = new State(this.currentState);
        desiredState.setTool(event.getTool());
        this.eventBus.fireEventFromSource(new StateChangedEvent(desiredState), this);
//        this.currentState.setTool(event.getTool());
//        History.newItem(this.currentState.toString(), false);
    }

    private void tokenError(String token) {
        eventBus.fireEventFromSource(new TokenErrorEvent(token), this);
        try {
            new State(new Token(""), this); //Forcing to load the initial state at least
        } catch (TokenMalformedException e) {/*Nothing here*/}
    }
}
