package org.reactome.web.pwp.client.common;

import com.google.gwt.http.client.URL;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisStatus {

    private String token;
    private String resource;

    public AnalysisStatus() {
        this(null);
    }

    public AnalysisStatus(String token) {
        setToken(token);
//        this.resource = token == null ? null : "TOTAL";
        this.resource = null;
    }

    public AnalysisStatus(String token, String resource) {
        setToken(token);
        if (token != null && !token.isEmpty()) {
            this.resource = resource;
        } else {
            this.resource = null;
        }
    }

    public AnalysisStatus clone(){
        return new AnalysisStatus(this.token, this.resource);
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

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getToken() {
        return token == null || token.isEmpty() ? null : token;
    }

    public String getResource() {
        return resource;
    }

    public boolean isEmpty() {
        return this.token == null || this.token.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnalysisStatus that = (AnalysisStatus) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        return !(resource != null ? !resource.equals(that.resource) : that.resource != null);

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnalysisStatus{" +
                "token='" + token + '\'' +
                ", resource='" + resource + '\'' +
                '}';
    }
}
