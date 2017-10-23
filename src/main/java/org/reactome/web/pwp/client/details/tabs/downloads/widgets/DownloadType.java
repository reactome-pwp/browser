package org.reactome.web.pwp.client.details.tabs.downloads.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.model.client.common.ContentClientAbstract;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum DownloadType {
    SBML        ("SBML", ContentClientAbstract.CONTENT_SERVICE + "exporter/sbml/__ID__.xml", "SMBL", DownloadIcons.INSTANCE.SBMLIcon()),
    SBGN        ("SBGN", "/ReactomeRESTfulAPI/RESTfulWS/sbgnExporter/__ID__", "SBGN", DownloadIcons.INSTANCE.SBGNIcon()),
    BIOPAX_2    ("BIOPAX 2", "/ReactomeRESTfulAPI/RESTfulWS/biopaxExporter/Level2/__ID__", "Biopax 2", DownloadIcons.INSTANCE.BioPAX2Icon()),
    BIOPAX_3    ("BIOPAX 3", "/ReactomeRESTfulAPI/RESTfulWS/biopaxExporter/Level3/__ID__", "Biopax 3", DownloadIcons.INSTANCE.BioPAX3Icon()),
    PDF         ("PDF", "/cgi-bin/pdfexporter?DB=__DB__&ID=__ID__", "PDF", DownloadIcons.INSTANCE.PDFIcon()),
    WORD        ("Word", "/cgi-bin/rtfexporter?DB=__DB__&ID=__ID__", "RTF", DownloadIcons.INSTANCE.WordIcon()),
    PROTEGE     ("Protege", "/cgi-bin/protegeexporter?DB=__DB__&ID=__ID__", "OWL", DownloadIcons.INSTANCE.ProtegeIcon());

    private String name;
    private String url;
    private String tooltip;
    private transient ImageResource icon;

    DownloadType(String name, String url, String tooltip, ImageResource icon) {
        this.name = name;
        this.url = url;
        this.tooltip = tooltip;
        this.icon = icon;
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

    public ImageResource getIcon() {
        return icon;
    }

    public interface DownloadIcons extends ClientBundle {

        DownloadIcons INSTANCE = GWT.create(DownloadIcons.class);

        @Source("images/SBML_download.png")
        ImageResource SBMLIcon();

        @Source("images/SBGN_download.png")
        ImageResource SBGNIcon();

        @Source("images/BioPAX2_download.png")
        ImageResource BioPAX2Icon();

        @Source("images/BioPAX3_download.png")
        ImageResource BioPAX3Icon();

        @Source("images/PDF_download.png")
        ImageResource PDFIcon();

        @Source("images/DOC_download.png")
        ImageResource WordIcon();

        @Source("images/Protege_download.png")
        ImageResource ProtegeIcon();
    }
}
