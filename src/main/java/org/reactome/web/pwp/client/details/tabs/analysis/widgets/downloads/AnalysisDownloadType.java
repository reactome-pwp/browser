package org.reactome.web.pwp.client.details.tabs.analysis.widgets.downloads;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public enum AnalysisDownloadType {

    RESULTS_CSV    (Resources.INSTANCE.resultsCSVIcon(),    Resources.INSTANCE.resultsCSVInfo(),    "Pathway analysis results",     "/AnalysisService/download/###TOKEN###/pathways/###RESOURCE###/result.csv"),
    RESULTS_JSON   (Resources.INSTANCE.resultsJSONIcon(),   Resources.INSTANCE.resultsJSONInfo(),   "Complete analysis results",    "/AnalysisService/download/###TOKEN###/result.json.gz"),
    MAPPINGS       (Resources.INSTANCE.mappingsIcon(),      Resources.INSTANCE.mappingsInfo(),      "Identifier mappings",          "/AnalysisService/download/###TOKEN###/entities/found/###RESOURCE###/mapping.csv"),
    PDF_REPORT     (Resources.INSTANCE.pdfReportIcon(),     Resources.INSTANCE.pdfReportInfo(),     "Analysis report",              "/AnalysisService/report/###TOKEN###/###SPECIES###/report.pdf?resource=###RESOURCE###&diagramProfile=###D_PROFILE###&analysisProfile=###A_PROFILE###&fireworksProfile=###F_PROFILE###"),
    NOT_FOUND      (Resources.INSTANCE.notFoundIcon(),      Resources.INSTANCE.notFoundInfo(),      "Not found identifiers",        "/AnalysisService/download/###TOKEN###/entities/notfound/not_found.csv");


    private transient ImageResource icon;
    private String title;
    private transient TextResource info;
    private String link;

    AnalysisDownloadType(final ImageResource icon, final TextResource info, final String title, final String link) {
        this.icon = icon;
        this.title = title;
        this.info = info;
        this.link = link;
    }

    public ImageResource getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public TextResource getInfo() {
        return info;
    }

    public String getLink() {
        return link;
    }

    public interface Resources extends ClientBundle {

        Resources INSTANCE = GWT.create(Resources.class);

        @Source("images/results_csv.png")
        ImageResource resultsCSVIcon();

        @Source("images/results_json.png")
        ImageResource resultsJSONIcon();

        @Source("images/mappings.png")
        ImageResource mappingsIcon();

        @Source("images/pdf_report.png")
        ImageResource pdfReportIcon();

        @Source("images/not_found.png")
        ImageResource notFoundIcon();

        @Source("info/AnalysisResultsCSVInfo.html")
        TextResource resultsCSVInfo();

        @Source("info/AnalysisResultsJSONInfo.html")
        TextResource resultsJSONInfo();

        @Source("info/IdentifierMappingsInfo.html")
        TextResource mappingsInfo();

        @Source("info/PDFReportInfo.html")
        TextResource pdfReportInfo();

        @Source("info/NotFoundIdentifiersInfo.html")
        TextResource notFoundInfo();

    }

}
