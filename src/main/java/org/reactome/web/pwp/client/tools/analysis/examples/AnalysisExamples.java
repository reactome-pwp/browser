package org.reactome.web.pwp.client.tools.analysis.examples;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisExamples extends ClientBundle {

    AnalysisExamples EXAMPLES = GWT.create(AnalysisExamples.class);

    @Source("AnalysisInfo.html")
    TextResource analysisInfo();

    @Source("AnalysisExamples.html")
    TextResource analysisExamples();

    @Source("SpeciesComparisonInfo.html")
    TextResource speciesComparisonInfo();

    @Source("uniprot.txt")
    TextResource getUniprot();

    @Source("geneNames.txt")
    TextResource getGeneNames();

    @Source("GeneNCBI_Entrez.txt")
    TextResource geneNCBI();

    @Source("chEBI.txt")
    TextResource getChEBI();

    @Source("kegg.txt")
    TextResource getKegg();

    @Source("expression.txt")
    TextResource getExpression();

    @Source("metabolomics.txt")
    TextResource getMetabolomics();
}
