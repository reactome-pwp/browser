package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;

import org.reactome.web.analysis.client.filter.ResultFilter;
import org.reactome.web.pwp.client.common.AnalysisStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Filter extends ResultFilter {

    public enum Type {
        BY_PVALUE   ("pValue", "#7968a5"),
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

    private Set<Type> appliedFilters;

    private Filter(String resource) {
        setResource(resource);
        appliedFilters = new HashSet<>(5);
    }

    public static Filter fromAnalysisStatus(AnalysisStatus analysisStatus) {
        Filter filter = new Filter(analysisStatus.getResource());
        filter.setSizeBoundaries(analysisStatus.getMin(), analysisStatus.getMax());
        filter.setSpeciesList(analysisStatus.getSpeciesList());
        filter.setpValue(analysisStatus.getpValue());
        filter.setIncludeDisease(analysisStatus.getIncludeDisease());
        return filter;
    }

    public Filter clone() {
        Filter rtn = new Filter(resource);
        rtn.setSizeBoundaries(min, max);
        rtn.setSpeciesList(speciesList);
        rtn.setpValue(pValue);
        rtn.setIncludeDisease(includeDisease);

        return rtn;
    }

    @Override
    public void setResource(String resource) {
        super.setResource(resource);
    }

    public void setSizeBoundaries(Integer min, Integer max) {
        if (min != null && max != null) {
            if (super.setBoundaries(min, max)) {
                appliedFilters.add(Type.BY_SIZE);
            }
        } else {
            appliedFilters.remove(Type.BY_SIZE);
        }
    }

    @Override
    public void setSpeciesList(List<String> species) {
        super.setSpeciesList(species != null && !species.isEmpty() ? new ArrayList<>(species) : new ArrayList<>());
        if (!speciesList.isEmpty()) {
            appliedFilters.add(Type.BY_SPECIES);
        } else {
            appliedFilters.remove((Type.BY_SPECIES));
        }
    }

    @Override
    public void setpValue(Double pValue) {
        super.setpValue(pValue);
        if (this.pValue != null) {
            appliedFilters.add(Type.BY_PVALUE);
        } else {
            appliedFilters.remove(Type.BY_PVALUE);
        }
    }

    @Override
    public void setIncludeDisease(boolean includeDisease) {
        super.setIncludeDisease(includeDisease);
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
                speciesList.clear();
                break;
            case BY_DISEASE:
                includeDisease = true;
                break;
            case BY_SIZE:
                max = min = null;
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

    public Set<Type> getAppliedFilters() {
        return appliedFilters;
    }
}
