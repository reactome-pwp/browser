package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;

import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species.Species;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Filter {

    public enum Type {
        BY_PVALUE   ("p-Value", "#7968a5"),
        BY_SIZE     ("Size",    "#89bf53"),
        BY_SPECIES  ("Species", "#f5b945"),
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

    private String resource;
    private Integer sizeMin, sizeMax;
    private Double pValue;
    private List<Species> species;
    private boolean includeDisease;

    private Set<Type> appliedFilters;

    private Filter(String resource) {
        setResource(resource);
        species = new ArrayList<>();
        appliedFilters = new HashSet<>(5);
    }

    public static Filter fromAnalysisStatus(AnalysisStatus analysisStatus) {
        Filter filter = new Filter(analysisStatus.getResource());
        filter.setSize(analysisStatus.getMin(), analysisStatus.getMax());
        filter.setSpecies(null);
        filter.setPValue(analysisStatus.getpValue());
        filter.setDisease(analysisStatus.getIncludeDisease());
        return filter;
    }

    public Filter clone() {
        Filter rtn = new Filter(resource);
        rtn.setSize(sizeMin, sizeMax);
        rtn.setSpecies(species);
        rtn.setPValue(pValue);
        rtn.setDisease(Boolean.valueOf(includeDisease));

        return rtn;
    }

    public boolean isActive() {
        return true;
//        return appliedFilters.size() > 0;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setSize(Integer sizeMin, Integer sizeMax) {
        this.sizeMin = sizeMin;
        this.sizeMax = sizeMax;
        if (sizeMin != null && sizeMax != null) {
            appliedFilters.add(Type.BY_SIZE);
        } else {
            appliedFilters.remove(Type.BY_SIZE);
        }
    }

    public void setSpecies(List<Species> species) {
        this.species = species != null && !species.isEmpty() ? new ArrayList<>(species) : new ArrayList<>();
        if (!this.species.isEmpty()) {
            appliedFilters.add(Type.BY_SPECIES);
        } else {
            appliedFilters.remove((Type.BY_SPECIES));
        }
    }

    public void setPValue(Double pValue) {
        this.pValue = pValue;
        if (pValue != null) {
            appliedFilters.add(Type.BY_PVALUE);
        } else {
            appliedFilters.remove(Type.BY_PVALUE);
        }
    }

    public void setDisease(boolean includeDisease) {
        this.includeDisease = includeDisease;
        if (!includeDisease) {
            appliedFilters.add(Type.BY_DISEASE);
        } else {
            appliedFilters.remove(Type.BY_DISEASE);
        }
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
                sizeMax = sizeMin = null;
                break;
            case BY_PVALUE:
                pValue = null;
                break;
        }
    }

    public void removeAll() {
        for (Type type : Type.values()) {
            removeFilter(type);
        }
    }

    public String getResource() {
        return resource;
    }

    public Integer getSizeMin() {
        return sizeMin;
    }

    public Integer getSizeMax() {
        return sizeMax;
    }

    public List<Species> getSpecies() {
        return species;
    }

    public List<?> getSpeciesString() {
        return species != null && !species.isEmpty() ? species.stream().map(s -> s.getSpeciesSummary().getTaxId()).collect(Collectors.toList()) : null;
    }

    public Double getpValue() {
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
                "resource='" + resource + '\'' +
                ", sizeMin=" + sizeMin +
                ", sizeMax=" + sizeMax +
                ", pValue=" + pValue +
                ", species=" + species +
                ", includeDisease=" + includeDisease +
                ", appliedFilters=" + appliedFilters +
                '}';
    }
}
