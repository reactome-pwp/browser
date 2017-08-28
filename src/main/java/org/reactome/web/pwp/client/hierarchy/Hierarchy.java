package org.reactome.web.pwp.client.hierarchy;


import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.classes.Species;
import org.reactome.web.pwp.model.client.util.Path;

import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Hierarchy {

    interface Presenter extends BrowserModule.Presenter {
        void eventHovered(Pathway pathway, Event event, Path path);

        void eventHoveredReset();

        void eventSelected(Pathway pathway, Event event, Path path);

        void hierarchyChanged(Species species);

        void openDiagram(Pathway pathway);

        void pathwayExpanded(Pathway pathway);

        void retrieveData(Species species);

    }

    interface Display extends BrowserModule.Display {
        void clearAnalysisResult();

        void expandPathway(Path path, Pathway pathway);

        Set<Pathway> getLoadedPathways();

        Set<Pathway> getPathwaysWithLoadedReactions();

        void highlightHitReactions(Set<Long> reactionsHit);

        void setPresenter(Presenter presenter);

        void show(Species species);

        void showAnalysisResult(List<PathwaySummary> pathwaySummaries);

        void select(Event event, Path path);

        void setData(Species species, List<? extends Event> tlps);
    }
}
