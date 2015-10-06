package org.reactome.web.pwp.client.manager.state;

import com.google.gwt.http.client.URL;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.PathwayPortalTool;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.manager.state.token.Token;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.classes.Species;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectsCreatedHandler;
import org.reactome.web.pwp.model.util.Path;

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

    private Pathway pathway;

    /**
     * selected can either be a Pathway, a PhysicalEntity or a Species object
     */
    private DatabaseObject selected;

    private Path path;

    private DetailsTabType detailsTab;

    private PathwayPortalTool tool;

    private AnalysisStatus analysisStatus = new AnalysisStatus();

    private String flag;

    public State(Token token, final StateLoadedHandler handler) {
        List<String> toLoad = token.getToLoad();
        final Map<StateKey, String> parameters = token.getParameters();
        DatabaseObjectFactory.get(toLoad, new DatabaseObjectsCreatedHandler() {
            private String token;
            private String resource = "TOTAL";

            @Override
            public void onDatabaseObjectsLoaded(Map<String, DatabaseObject> databaseObjects) {
                for (StateKey key : parameters.keySet()) {
                    try {
                        String identifier = parameters.get(key);
                        switch (key) {
                            case SPECIES:
                                species = (Species) databaseObjects.get(identifier);
                                break;
                            case PATHWAY:
                                pathway = (Pathway) databaseObjects.get(identifier);
                                break;
                            case SELECTED:
                                selected = databaseObjects.get(identifier);
                                break;
                            case PATH:
                                path = new Path(StateHelper.getEvents(identifier.split(","), databaseObjects));
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
                            case RESOURCE:
                                resource = identifier;
                                break;
                            case FLAG:
                                flag = identifier;
                                break;
                            default:
                                System.err.println(getClass().getSimpleName() + " >> " + key + " not treated!");
                        }

                    } catch (ClassCastException e) {
                        handler.onStateLoaded(null);
                        return;
                    }
                }
                setAnalysisParameters(token, resource);
                path = getPrunedPath(); //Very important!
                doConsistencyCheck(handler);
            }

            @Override
            public void onDatabaseObjectError(Throwable exception) {
                //TODO: Treat the exception
                exception.printStackTrace();
            }
        });
    }

    public State(State state) {
        this.species = state.species;
        this.pathway = state.pathway;
        this.selected = state.selected;
        this.path = state.path;
        this.detailsTab = state.detailsTab;
        this.tool = state.tool;
        this.analysisStatus = state.analysisStatus;
        this.flag = state.flag;
    }

    public void doConsistencyCheck(final StateLoadedHandler handler){
        if(this.pathway!=null){
            if(pathway.getHasDiagram()){
                handler.onStateLoaded(this);
            }else{
                StateHelper.getPathwayWithDiagram(pathway, path, new StateHelper.PathwayWithDiagramHandler() {
                    @Override
                    public void setPathwayWithDiagram(Pathway pathway, Path path) {
                        if(State.this.selected==null){
                            State.this.selected = State.this.pathway;
                        }
                        State.this.pathway = pathway;
                        State.this.path = path;
                        State.this.path = State.this.getPrunedPath(); //Very important!
                        handler.onStateLoaded(State.this);
                    }

                    @Override
                    public void onPathwayWithDiagramRetrievalError(Throwable throwable) {
                        Console.error("on Pathway with diagram retrieval error: " + throwable.getMessage());
                    }
                });
            }
        }else {
            handler.onStateLoaded(this);
        }
    }

    public Species getSpecies() {
        return pathway != null && pathway.getSpecies() != null && !pathway.getSpecies().isEmpty() ? pathway.getSpecies().get(0) : species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Pathway getPathway() {
        return pathway;
    }

    public void setPathway(Pathway pathway) {
        if (pathway == null) {
            this.species = getSpecies();
            this.selected = null;
            this.path = null;
        }
        this.pathway = pathway;
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
        return this.pathway;
    }

    public DetailsTabType getDetailsTab() {
        if (detailsTab == null) return DetailsTabType.getDefault();
        return detailsTab;
    }

    public void setDetailsTab(DetailsTabType detailsTab) {
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

    public void setAnalysisParameters(String analysisToken, String resource) {
        //IMPORTANT! Do no use setToken! ALWAYS create a new object here
        this.analysisStatus = new AnalysisStatus(analysisToken, resource);
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        StringBuilder token = new StringBuilder("/");
        boolean addDelimiter = false;
        if (pathway == null && species != null && !species.getDbId().equals(Token.DEFAULT_SPECIES_ID)) {
            token.append(StateKey.SPECIES.getDefaultKey());
            token.append("=");
            token.append(species.getIdentifier());
            addDelimiter = true;
        }
        if (pathway != null) {
            if (addDelimiter) token.append(Token.DELIMITER);
            if (!Token.isSingleIdentifier(pathway.getIdentifier())) {
                token.append(StateKey.PATHWAY.getDefaultKey());
                token.append("=");
            }
            token.append(pathway.getIdentifier());
            addDelimiter = true;
        }
        if (selected != null) {
            if (addDelimiter) token.append(Token.DELIMITER);
            token.append(StateKey.SELECTED.getDefaultKey());
            token.append("=");
            token.append(selected.getIdentifier());
            addDelimiter = true;
        }
        if (path != null && !path.isEmpty()) {
            if (addDelimiter) token.append(Token.DELIMITER);
            token.append(StateKey.PATH.getDefaultKey());
            token.append("=");
            for (Event event : path) {
                token.append(event.getIdentifier());
                token.append(",");
            }
            token.deleteCharAt(token.length() - 1);
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
            if(analysisStatus.getResource()!=null && !analysisStatus.getResource().equals("TOTAL")){
                token.append(Token.DELIMITER);
                token.append(StateKey.RESOURCE.getDefaultKey());
                token.append("=");
                token.append(analysisStatus.getResource());
            }
            addDelimiter=true;
        }
        if( flag != null ) {
            if (addDelimiter) token.append(Token.DELIMITER);
            token.append(StateKey.FLAG.getDefaultKey());
            token.append("=");
            token.append(flag);
//            addDelimiter=true;
        }
        return token.toString();
    }

    /**
     * Returns the path until the loaded diagram (without including it)
     *
     * @return the path until the loaded diagram (without including it)
     */
    protected Path getPrunedPath() {
        if (this.path == null) return null;
        List<Event> prunedPath = new LinkedList<>();
        for (Event event : this.path) {
            if (event.equals(this.pathway)) {
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
