package org.reactome.web.pwp.client.common;

import com.google.gwt.http.client.URL;
import org.reactome.web.analysis.client.filter.ResultFilter;
import org.reactome.web.pwp.model.client.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public class AnalysisStatus {

    private String token;
    private String resource;
    private Double pValue;
    private boolean includeDisease;
    private Integer min;
    private Integer max;
    private List<String> speciesList;

    public AnalysisStatus() {
        this.token = null;
        this.resource = null;
        this.pValue = null;
        this.includeDisease = true;
        this.min = null;
        this.max = null;
        this.speciesList = new ArrayList<>();
    }

    public AnalysisStatus(String token) {
        this();
        setToken(token);
    }

    public AnalysisStatus(String token, String resource) {
        this(token);
        if (token != null && !token.isEmpty()) {
            this.resource = resource;
        }
    }

    public AnalysisStatus(String token, String resource, Double pValue, boolean includeDisease, Integer min, Integer max, List<String> speciesList) {
        setToken(token);
        if (token != null && !token.isEmpty()) {
            this.resource = resource;
            this.pValue = pValue;
            this.includeDisease = includeDisease;
            this.min = min;
            this.max = max;
            this.speciesList = speciesList;
        }
    }

    public AnalysisStatus clone(){
        return new AnalysisStatus(token, resource, pValue, includeDisease, min, max, speciesList);
    }

    public String getFilter() {
        List<String> rtn = new ArrayList<>();
        if (!getResource().equals("TOTAL")) rtn.add("resource:" + getResource());
        if (getpValue() != null) rtn.add("pValue:" + getpValue());
        if (!includeDisease) rtn.add("includeDisease:" + getIncludeDisease());
        if (getMin() != null) rtn.add("min:" + getMin());
        if (getMax() != null) rtn.add("max:" + getMax());
        if (!getSpeciesList().isEmpty()) rtn.add("species:" + StringUtils.join(getSpeciesList(), ","));
        return rtn.isEmpty() ? null : StringUtils.join(rtn, "$");
    }

    public void setFilter(String filter) {
        if (filter == null) return;
        //backwards compatibility
        if (!filter.contains("$") && !filter.contains(":")) {
            setResource(filter);
        } else {
            String[] opts = filter.split("\\$");
            for (String opt : opts) {
                String[] aux = opt.split(":");
                if (aux.length == 2) setFilter(aux[0], aux[1]);
            }
        }
    }

    public String getToken() {
        return token == null || token.isEmpty() ? null : token;
    }

    public void setToken(String token) {
        if (token != null) {
            do { //The idea is to keep a completely URL decoded token
                this.token = token;
                token = URL.decodeQueryString(token);
            } while (!this.token.equals(token));
        }
        this.token = token;
    }

    public String getResource() {
        return resource == null ? "TOTAL" : resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Double getpValue() {
        return pValue != null && pValue >= 0 && pValue <= 1 ? pValue : null;
    }

    public void setpValue(Double pValue) {
        this.pValue = pValue;
    }

    public boolean getIncludeDisease() {
        return includeDisease;
    }

    public void setIncludeDisease(boolean includeDisease) {
        this.includeDisease = includeDisease;
    }

    public Integer getMin() {
        return min != null && max != null && min <= max ? min : null;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return min != null && max != null && min <= max ? max : null;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public List<String> getSpeciesList() {
        return speciesList;
    }

    private void setSpeciesList(String str) {
        if (str == null || str.isEmpty()) {
            this.speciesList = new ArrayList<>();
        } else {
            this.speciesList = Arrays.asList(str.split(","));
        }
    }

    public void setSpeciesList(List<String> speciesList) {
        this.speciesList = speciesList;
    }

    public boolean isEmpty() {
        return this.token == null || this.token.isEmpty();
    }

    public ResultFilter getResultFilter() {
        return new ResultFilter(resource == null ? "TOTAL" : resource, pValue, includeDisease, min, max, speciesList);
    }

    public void setResultFilter(ResultFilter filter){
        setResource(filter.getResource());
        setpValue(filter.getpValue());
        setIncludeDisease(filter.getIncludeDisease());
        setMin(filter.getMin());
        setMax(filter.getMax());
        setSpeciesList(filter.getSpeciesList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisStatus that = (AnalysisStatus) o;
        return includeDisease == that.includeDisease &&
                Objects.equals(token, that.token) &&
                Objects.equals(resource, that.resource) &&
                Objects.equals(pValue, that.pValue) &&
                Objects.equals(min, that.min) &&
                Objects.equals(max, that.max) &&
                Objects.equals(speciesList, that.speciesList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, resource, pValue, includeDisease, min, max, speciesList);
    }

    @Override
    public String toString() {
        return "AnalysisStatus{" +
                "token='" + token + '\'' +
                ", resource='" + resource + '\'' +
                (pValue != null ? ", pValue='" + pValue + '\'' : "") +
                ", includeDisease=" + includeDisease +
                (min != null ? ", min=" + min : "") +
                (max != null ? ", max=" + max : "") +
                (!speciesList.isEmpty() ? ", species=" + StringUtils.join(speciesList, "-") : "") +
                '}';
    }

    private void setFilter(String key, String value){
        try {
            switch (key.toLowerCase()) {
                case "resource":        setResource(value);                         break;
                case "pvalue":          setpValue(Double.valueOf(value));           break;
                case "includedisease":  setIncludeDisease(Boolean.valueOf(value));  break;
                case "min":             setMin(Integer.valueOf(value));             break;
                case "max":             setMax(Integer.valueOf(value));             break;
                case "species":         setSpeciesList(value);                      break;
            }
        } catch (Exception e) {
            //Nothing here
        }
    }
}
