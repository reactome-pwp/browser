package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset;

import com.google.gwt.user.client.DOM;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.UploadResult;

import java.util.*;

/**
 * Used to store the information for every dataset annotated
 * and submitted to the GSA service
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSADataset {

    private String id;
    private String name;
    private String filename;
    private String type;
    private String typeName;

    private int numberOfLines;
    private List<String> sampleNames;
    private List<String> topIdentifiers;
    private String dataToken;

    private Annotations annotations;

    private Map<String, String> parameters = new HashMap<>();

    private GSADataset() {
    }

    public static GSADataset create(final String type, final String typeName, final String filename, final UploadResult uploadResult, final String defaultName) {
        GSADataset dataset = new GSADataset();
        dataset.id = DOM.createUniqueId();
        dataset.name = defaultName;
        dataset.type = type;
        dataset.typeName = typeName;
        dataset.filename = filename;

        dataset.numberOfLines = uploadResult.getNumberOfLines();
        dataset.dataToken = uploadResult.getDataToken();
        dataset.sampleNames = uploadResult.getSampleNames();
        dataset.topIdentifiers = uploadResult.getTopIdentifiers();

        dataset.annotations = new Annotations(uploadResult.getSampleNames());

        return dataset;
    }

    public static GSADataset create(GSADataset dataset) {
        GSADataset copy = new GSADataset();
        copy.id = dataset.id;
        copy.name = dataset.name;
        copy.type = dataset.type;
        copy.typeName = dataset.typeName;
        copy.filename = dataset.filename;

        copy.numberOfLines = dataset.numberOfLines;
        copy.dataToken = dataset.dataToken;
        copy.sampleNames = new ArrayList<>(dataset.sampleNames);
        copy.topIdentifiers = new ArrayList<>(dataset.topIdentifiers);
        copy.annotations = Annotations.create(dataset.annotations);

        return copy;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public List<String> getSampleNames() {
        return sampleNames;
    }

    public List<String> getTopIdentifiers() {
        return topIdentifiers;
    }

    public String getDataToken() {
        return dataToken;
    }

    public Annotations getAnnotations() {
        return annotations;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GSADataset that = (GSADataset) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GSADataset{" +
                "name='" + name + '\'' +
                "id='" + id + '\'' +
                ", filename='" + filename + '\'' +
                ", type=" + type +
                ", typeName=" + typeName +
                ", numberOfLines=" + numberOfLines +
                ", sampleNames=" + sampleNames +
                ", topIdentifiers=" + topIdentifiers +
                ", dataToken='" + dataToken + '\'' +
                '}';
    }
}
