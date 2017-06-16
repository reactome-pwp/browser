package org.reactome.web.pwp.client.details.tabs.downloads.widgets;

import org.reactome.web.pwp.model.client.RESTFulClient;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum DownloadType {
    SBML        ("SBML", "/ContentService/exporter/sbml/__ID__.xml", "SMBL"),
    SBGN        ("SBGN", RESTFulClient.CONTENT_SERVICE_PATH + "sbgnExporter/__ID__", "SBGN"),
    BIOPAX_2    ("BIOPAX 2", RESTFulClient.CONTENT_SERVICE_PATH + "biopaxExporter/Level2/__ID__", "Biopax 2"),
    BIOPAX_3    ("BIOPAX 3", RESTFulClient.CONTENT_SERVICE_PATH + "biopaxExporter/Level3/__ID__", "Biopax 3"),
    PDF         ("PDF", "/cgi-bin/pdfexporter?DB=__DB__&ID=__ID__", "PDF"),
    WORD        ("Word", "/cgi-bin/rtfexporter?DB=__DB__&ID=__ID__", "RTF"),
    PROTEGE     ("Protege", "/cgi-bin/protegeexporter?DB=__DB__&ID=__ID__", "OWL");

    private String name;
    private String url;
    private String tooltip;

    DownloadType(String name, String url, String tooltip) {
        this.name = name;
        this.url = url;
        this.tooltip = tooltip;
    }

    public String getName() {
        return name;
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getUrl(String db, Long dbId){
        return this.url.replace("__DB__",db).replace("__ID__", String.valueOf(dbId));
    }
}
