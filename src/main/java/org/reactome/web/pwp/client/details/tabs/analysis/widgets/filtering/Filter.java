package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;

import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species.Species;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Filter {

    public enum Type {
        BY_SIZE     ("Size", "#89bf53"),
        BY_SPECIES  ("Species", "#f5b945"), //"#46ceac"
        BY_DISEASE  ("Disease", "#f96c51");

        private String displayName;
        private String chipColour;

        Type(String displayName, String chipColour) {
            this.displayName = displayName;
            this.chipColour = chipColour;
        }

        public String displayName() {
            return displayName;
        }

        public String chipColour() {
            return chipColour;
        }
    }

    private int sizeMin, sizeMax;
    private List<Species> species;
    private boolean includeDisease;

    private Set<Type> appliedFilters;

    public Filter() {
        appliedFilters = new HashSet<>();
    }

    public boolean isActive() {
        return appliedFilters.size() > 0;
    }

    public void setSize(int sizeMin, int sizeMax) {
        this.sizeMin = sizeMin;
        this.sizeMax = sizeMax;
        appliedFilters.add(Type.BY_SIZE);
    }

    public void setSpecies(List<Species> species) {
        this.species = new ArrayList<>(species);
        appliedFilters.add(Type.BY_SPECIES);
    }

    public void setDisease(boolean includeDisease) {
        this.includeDisease = includeDisease;
        appliedFilters.add(Type.BY_DISEASE);
    }

    public void removeFilter(Type type) {
        appliedFilters.remove(type);
    }

    public int getSizeMin() {
        return sizeMin;
    }

    public int getSizeMax() {
        return sizeMax;
    }

    public List<Species> getSpecies() {
        return species;
    }

    public boolean isIncludeDisease() {
        return includeDisease;
    }

    public Set<Type> getAppliedFilters() {
        return appliedFilters;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "appliedFilters=" + appliedFilters +
                '}';
    }
}
