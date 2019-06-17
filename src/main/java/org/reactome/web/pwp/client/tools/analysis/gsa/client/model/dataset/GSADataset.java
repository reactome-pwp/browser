package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset;

import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.UploadResult;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSADataset {

    private String name;
    private String filename;
    private String type;

    private int numberOfLines;
    private List<String> sampleNames;
    private List<String> topIdentifiers;
    private String dataToken;

    private GSADataset() {
    }

    public static GSADataset create(final String type, final String filename,  final UploadResult uploadResult) {
        GSADataset dataset = new GSADataset();
        dataset.type = type;
        dataset.filename = filename;

        dataset.numberOfLines = uploadResult.getNumberOfLines();
        dataset.dataToken = uploadResult.getDataToken();
        dataset.sampleNames = uploadResult.getSampleNames();
        dataset.topIdentifiers = uploadResult.getTopIdentifiers();

        return dataset;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public String getType() {
        return type;
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

    @Override
    public String toString() {
        return "GSADataset{" +
                "name='" + name + '\'' +
                ", filename='" + filename + '\'' +
                ", type=" + type +
                ", numberOfLines=" + numberOfLines +
                ", sampleNames=" + sampleNames +
                ", topIdentifiers=" + topIdentifiers +
                ", dataToken='" + dataToken + '\'' +
                '}';
    }
}
