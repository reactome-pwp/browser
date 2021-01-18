package org.reactome.web.pwp.client.manager.state;

import com.google.gwt.http.client.URL;
import org.reactome.web.analysis.client.filter.ResultFilter;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.PathwayPortalTool;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.manager.state.token.Token;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.classes.Species;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.web.pwp.model.client.util.Path;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class State {

    public interface StateLoadedHandler {
        void onStateLoaded(State state);
    }

    private Species species;

    /**
     * event can either be a Pathway or a ReactionLikeEvent (these are linkable as default parameters)
     */
    private Event event;

    /**
     * selected can either be a Pathway, a PhysicalEntity or a Species object
     */
    private DatabaseObject selected;

    private Path path;

    private DetailsTabType detailsTab;

    private PathwayPortalTool tool;

    private AnalysisStatus analysisStatus = new AnalysisStatus();

    private String flag;
    private boolean flagIncludeInteractors = false;
    private Map<String, String> extraParams; //to enable extensions of Reactome project to add extra url parameters

    public State(Token token, final StateLoadedHandler handler) {
        List<String> toLoad = token.getToLoad();
        final Map<StateKey, String> parameters = token.getParameters();
        this.extraParams = token.getExtraParams();
        ContentClient.query(toLoad, new ContentClientHandler.ObjectMapLoaded() {
            private String token;
            private String filter;

            @Override
            public void onObjectMapLoaded(Map<String, ? extends DatabaseObject> map) {
                for (StateKey key : parameters.keySet()) {
                    try {
                        String identifier = parameters.get(key);
                        switch (key) {
                            case SPECIES:
                                species = (Species) map.get(identifier);
                                break;
                            case PATHWAY:
                                event = (Event) map.get(identifier);
                                break;
                            case SELECTED:
                                selected = map.get(identifier);
                                break;
                            case PATH:
                                path = new Path(StateHelper.getEvents(identifier.split(","), map));
                                break;
                            case DETAILS_TAB:
                                detailsTab = DetailsTabType.getByCode(identifier);
                                break;
                            case TOOL:
                                tool = PathwayPortalTool.getByCode(identifier);
                                break;
                            case ANALYSIS:
                                token = identifier;
                                break;
                            case FILTER:
                                filter = identifier;
                                break;
                            case FLAG:
                                flag = identifier;
                                break;
                            case FLAG_INT:
                                flagIncludeInteractors = true;
                                break;
                            default:
                                System.err.println(getClass().getSimpleName() + " >> " + key + " not treated!");
                        }

                    } catch (ClassCastException e) {
                        handler.onStateLoaded(null);
                        return;
                    }
                }
                setAnalysisParameters(token, filter);
                path = getPrunedPath(); //Very important!
                doConsistencyCheck(handler);
            }

            @Override
            public void onContentClientException(Type type, String message) {
                Console.error(message);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                Console.error(error.getReason());
            }
        });
    }

    public State(State state) {
        this.species = state.species;
        this.event = state.event;
        this.selected = state.selected;
        this.path = state.path;
        this.detailsTab = state.detailsTab;
        this.tool = state.tool;
        this.analysisStatus = state.analysisStatus;
        this.flag = state.flag;
        this.flagIncludeInteractors = state.flagIncludeInteractors;
        this.extraParams = state.extraParams;
    }

    void doConsistencyCheck(final StateLoadedHandler handler) {
        if (event != null) {
            if ((event instanceof Pathway) && ((Pathway) event).getHasDiagram()) {
                handler.onStateLoaded(this);
            } else {
                StateHelper.getPathwayWithDiagram(event, path, new StateHelper.PathwayWithDiagramHandler() {
                    @Override
                    public void setPathwayWithDiagram(Pathway pathway, Path path) {
                        if (State.this.selected == null) {
                            State.this.selected = State.this.event;
                        }
                        State.this.event = pathway;
                        State.this.path = path;
                        State.this.path = State.this.getPrunedPath(); //Very important!
                        handler.onStateLoaded(State.this);
                    }

                    @Override
                    public void onPathwayWithDiagramRetrievalError(String errorMessage) {
                        Console.error("on Pathway with diagram retrieval error: " + errorMessage);
                    }
                });
            }
        } else {
            handler.onStateLoaded(this);
        }
    }

    public Species getSpecies() {
        return event != null && event.getSpecies() != null && !event.getSpecies().isEmpty() ? event.getSpecies().get(0) : species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Pathway getPathway() {
        return (Pathway) event;
    }

    public void setEvent(Event event) {
        if (event == null) {
            this.species = getSpecies();
            this.selected = null;
            this.path = null;
        }
        this.event = event;
    }

    public DatabaseObject getSelected() {
        return selected;
    }

    public void setSelected(DatabaseObject selected) {
        this.selected = selected;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public DatabaseObject getTarget() {
        if (this.selected != null) return this.selected;
        return this.event;
    }

    public DetailsTabType getDetailsTab() {
        if (detailsTab == null) return DetailsTabType.getDefault();
        return detailsTab;
    }

    void setDetailsTab(DetailsTabType detailsTab) {
        this.detailsTab = detailsTab;
    }

    public PathwayPortalTool getTool() {
        return tool == null ? PathwayPortalTool.NONE : tool;
    }

    public void setTool(PathwayPortalTool tool) {
        this.tool = tool;
    }

    public AnalysisStatus getAnalysisStatus(){
        return this.analysisStatus;
    }

    public void setAnalysisStatus(AnalysisStatus analysisStatus) {
        this.analysisStatus = analysisStatus;
    }

    void setAnalysisParameters(String analysisToken, String filter) {
        //IMPORTANT! Do no use setToken! ALWAYS create a new object here
        this.analysisStatus = new AnalysisStatus(analysisToken);
        this.analysisStatus.setFilter(filter);
    }

    void setAnalysisParameters(String analysisToken, ResultFilter filter) {
        //IMPORTANT! Do no use setToken! ALWAYS create a new object here
        this.analysisStatus = new AnalysisStatus(analysisToken);
        this.analysisStatus.setResultFilter(filter);
    }

    void resetAnalysisParameters(){
        //IMPORTANT! Do no use setToken! ALWAYS create a new object here
        this.analysisStatus = new AnalysisStatus(null);
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean getFlagIncludeInteractors() {
        return flagIncludeInteractors;
    }

    public void setFlagIncludeInteractors(boolean flagIncludeInteractors) {
        this.flagIncludeInteractors = flagIncludeInteractors;
    }

    @Override
    public String toString() {
        StringBuilder token = new StringBuilder("/");
        boolean addDelimiter = false;
        if (event == null && species != null && !species.getDbId().equals(Token.DEFAULT_SPECIES_ID)) {
            token.append(StateKey.SPECIES.getDefaultKey());
            token.append("=");
            token.append(species.getReactomeIdentifier());
            addDelimiter = true;
        }
        if (event != null) {
            if (addDelimiter) token.append(Token.DELIMITER);
            if (!Token.isSingleIdentifier(event.getReactomeIdentifier())) {
                token.append(StateKey.PATHWAY.getDefaultKey());
                token.append("=");
            }
            token.append(event.getReactomeIdentifier());
            addDelimiter = true;
        }
        if (selected != null) {
            if (addDelimiter) token.append(Token.DELIMITER);
            token.append(StateKey.SELECTED.getDefaultKey());
            token.append("=");
            token.append(selected.getReactomeIdentifier());
            addDelimiter = true;
        }
        if (path != null && !path.isEmpty()) {
            if (addDelimiter) token.append(Token.DELIMITER);
            token.append(StateKey.PATH.getDefaultKey());
            token.append("=");
            for (Event event : path) {
                token.append(event.getReactomeIdentifier());
                token.append(",");
            }
            token.deleteCharAt(token.length() - 1);
            addDelimiter = true;
        }
        if (flag != null) {
            if (addDelimiter) token.append(Token.DELIMITER);
            token.append(StateKey.FLAG.getDefaultKey());
            token.append("=");
            token.append(flag);
            addDelimiter = true;
        }
        if (flagIncludeInteractors) {
            if (addDelimiter) token.append(Token.DELIMITER);
            token.append(StateKey.FLAG_INT.getDefaultKey());
            addDelimiter = true;
        }
        if (detailsTab != null && !detailsTab.equals(DetailsTabType.getDefault())) {
            if (addDelimiter) token.append(Token.DELIMITER);
            token.append(StateKey.DETAILS_TAB.getDefaultKey());
            token.append("=");
            token.append(detailsTab.getCode());
            addDelimiter = true;
        }
        if (tool != null && !tool.equals(PathwayPortalTool.getDefault())) {
            if (addDelimiter) token.append(Token.DELIMITER);
            token.append(StateKey.TOOL.getDefaultKey());
            token.append("=").append(tool.getCode());
            addDelimiter = true;
        }
        if (analysisStatus != null && !analysisStatus.isEmpty()) {
            if (addDelimiter) token.append(Token.DELIMITER);
            token.append(StateKey.ANALYSIS.getDefaultKey());
            token.append("=");
            token.append(URL.encodeQueryString(analysisStatus.getToken()));
            if (analysisStatus.getFilter() != null) {
                token.append(Token.DELIMITER);
                token.append(StateKey.FILTER.getDefaultKey());
                token.append("=");
                token.append(analysisStatus.getFilter());
            }
//            addDelimiter=true;
        }
        if(extraParams != null) {
        	extraParams.forEach((k,v) -> {
        		token.append("&");
        		token.append(k);
        		token.append("=");
        		token.append(v);
        	});
        }
        return token.toString();
    }

    /**
     * Returns the path until the loaded diagram (without including it)
     * @return the path until the loaded diagram (without including it)
     */
    private Path getPrunedPath() {
        if (this.path == null) return null;
        List<Event> prunedPath = new LinkedList<>();
        for (Event event : this.path) {
            if (event.equals(this.event)) {
                break;
            }
            prunedPath.add(event);
        }
        return new Path(prunedPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;
        return state.toString().equals(toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
