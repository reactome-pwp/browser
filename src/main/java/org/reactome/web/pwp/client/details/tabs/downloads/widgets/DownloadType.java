package org.reactome.web.pwp.client.details.tabs.downloads.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.common.ContentClientAbstract;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum DownloadType {
    SBML        ("SBML", ContentClientAbstract.CONTENT_SERVICE + "exporter/sbml/__ID__.xml", "SMBL", DownloadIcons.INSTANCE.SBMLIcon(), Group.FORMAT),
    SBGN        ("SBGN", "/ReactomeRESTfulAPI/RESTfulWS/sbgnExporter/__ID__", "SBGN", DownloadIcons.INSTANCE.SBGNIcon(), Group.FORMAT),
    BIOPAX_2    ("BIOPAX 2", "/ReactomeRESTfulAPI/RESTfulWS/biopaxExporter/Level2/__ID__", "Biopax 2", DownloadIcons.INSTANCE.BioPAX2Icon(), Group.FORMAT ),
    BIOPAX_3    ("BIOPAX 3", "/ReactomeRESTfulAPI/RESTfulWS/biopaxExporter/Level3/__ID__", "Biopax 3", DownloadIcons.INSTANCE.BioPAX3Icon(), Group.FORMAT),
    PDF         ("PDF", "/cgi-bin/pdfexporter?DB=__DB__&ID=__ID__", "PDF", DownloadIcons.INSTANCE.PDFIcon(), Group.FORMAT),
    WORD        ("Word", "/cgi-bin/rtfexporter?DB=__DB__&ID=__ID__", "RTF", DownloadIcons.INSTANCE.WordIcon(), Group.FORMAT),
    PROTEGE     ("Protege", "/cgi-bin/protegeexporter?DB=__DB__&ID=__ID__", "OWL", DownloadIcons.INSTANCE.ProtegeIcon(), Group.FORMAT),
    POWERPOINT  ("Powerpoint", ContentClientAbstract.CONTENT_SERVICE + "exporter/diagram/__STID__.pptx__PARAMS__", "PPTX", DownloadIcons.INSTANCE.PowerPointIcon(), Group.FORMAT),
    SVG         ("SVG", ContentClientAbstract.CONTENT_SERVICE + "exporter/diagram/__STID__.svg__PARAMS__", "SVG", DownloadIcons.INSTANCE.SVGIcon(), Group.DIAGRAM),
    PNG         ("PNG", ContentClientAbstract.CONTENT_SERVICE + "exporter/diagram/__STID__.png__PARAMS__", "PNG", DownloadIcons.INSTANCE.PNGIcon(), Group.DIAGRAM),
    JPEG        ("JPEG", ContentClientAbstract.CONTENT_SERVICE + "exporter/diagram/__STID__.jpeg__PARAMS__", "JPEG", DownloadIcons.INSTANCE.JPEGIcon(), Group.DIAGRAM),
    GIF         ("GIF", ContentClientAbstract.CONTENT_SERVICE + "exporter/diagram/__STID__.gif__PARAMS__", "GIF", DownloadIcons.INSTANCE.GIFIcon(), Group.DIAGRAM);

    private String name;
    private String url;
    private String tooltip;
    private transient ImageResource icon;
    private Group group;

    public enum Group {
        FORMAT,
        DIAGRAM

    }

    DownloadType(String name, String url, String tooltip, ImageResource icon, Group group) {
        this.name = name;
        this.url = url;
        this.tooltip = tooltip;
        this.icon = icon;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getUrl(String db, DatabaseObject pathway, AnalysisStatus status, String selected, String flag, String diagramProfile, String analysisProfile){
        String url = this.url.replace("__DB__",db).replace("__ID__", String.valueOf(pathway.getDbId())).replace("__STID__", pathway.getStId());

        if (url.contains("__PARAMS__")) {
            List<String> params = new ArrayList<>();
            if (this.getName() == "Powerpoint") {
                params.add("profile=" + diagramProfile); //TODO this should be removed as soon as the pptx exporter uses the same parameter name
            } else {
                params.add("diagramProfile=" + diagramProfile);
            }
            if (selected != null)   params.add("sel=" + selected);
            if (flag != null)       params.add("flg=" + flag);
            if (!status.isEmpty())  params.add("analysisProfile=" + analysisProfile);
            if (!status.isEmpty())  params.add("token=" + status.getToken());

            String paramsStr ="";
            if (!params.isEmpty()) {
                paramsStr = "?" + params.stream().collect(Collectors.joining("&"));
            }
            url = url.replace("__PARAMS__", paramsStr);
        }
        return url;
    }

    public ImageResource getIcon() {
        return icon;
    }

    public Group getGroup() {
        return group;
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

        @Source("images/PowerPoint_download.png")
        ImageResource PowerPointIcon();

        @Source("images/PNG_download.png")
        ImageResource PNGIcon();

        @Source("images/SVG_download.png")
        ImageResource SVGIcon();

        @Source("images/JPEG_download.png")
        ImageResource JPEGIcon();

        @Source("images/GIF_download.png")
        ImageResource GIFIcon();
    }
}
