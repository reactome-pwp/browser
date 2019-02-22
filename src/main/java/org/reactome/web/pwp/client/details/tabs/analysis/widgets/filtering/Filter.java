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
        BY_PVALUE   ("p-Value", "#7968a5"), //"#2d88c4"
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
    private double pValue;
    private List<Species> species;
    private boolean includeDisease;

    private Set<Type> appliedFilters;

    public Filter() {
        species = new ArrayList<>(5);
        appliedFilters = new HashSet<>();
        includeDisease = true;
        pValue = 1d;
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

    public void setPValue(double pValue) {
        this.pValue = pValue;
        appliedFilters.add(Type.BY_PVALUE);
    }

    public void removeFilter(Type type) {
        appliedFilters.remove(type);
        switch (type) {
            case BY_SPECIES:
                species.clear();
                break;
            case BY_DISEASE:
                includeDisease = true;
                break;
            case BY_SIZE:
                break;
            case BY_PVALUE:
                pValue = 1d;
                break;
        }
    }

    public void removeAll() {
        for (Type type : Type.values()) {
            removeFilter(type);
        }
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

    public double getpValue() {
        return pValue;
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
