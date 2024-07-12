package org.reactome.web.pwp.client.details.tabs.downloads.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import org.reactome.web.pwp.model.client.common.ContentClientAbstract;

import java.util.Arrays;
import java.util.List;

/**
 * @author Guilherme Viteri (gviteri@ebi.ac.uk)
 * @author Kostas Sidiropoulos (ksidiro@ebi.ac.uk)
 */
public enum DownloadType {
    SBML        ("SBML", ContentClientAbstract.CONTENT_SERVICE + "exporter/event/__STID__.sbml", "SBML", DownloadIcons.INSTANCE.SBMLIcon(), Group.FORMAT),
    SBGN        ("SBGN", ContentClientAbstract.CONTENT_SERVICE + "exporter/event/__STID__.sbgn", "SBGN", DownloadIcons.INSTANCE.SBGNIcon(), Group.FORMAT),
    BIOPAX_2    ("BIOPAX 2", "/ReactomeRESTfulAPI/RESTfulWS/biopaxExporter/Level2/__ID__", "Biopax 2", DownloadIcons.INSTANCE.BioPAX2Icon(), Group.FORMAT ),
    BIOPAX_3    ("BIOPAX 3", "/ReactomeRESTfulAPI/RESTfulWS/biopaxExporter/Level3/__ID__", "Biopax 3", DownloadIcons.INSTANCE.BioPAX3Icon(), Group.FORMAT),
    PDF         ("PDF", ContentClientAbstract.CONTENT_SERVICE + "exporter/document/event/__STID__.pdf__PARAMS__", "PDF", DownloadIcons.INSTANCE.PDFIcon(), Group.FORMAT),
    NEWT        ("NEWT EDITOR", "https://web.newteditor.org/?URL=https://" + getHost() + ContentClientAbstract.CONTENT_SERVICE + "exporter/event/__STID__.sbgn" + "&inferNestingOnLoad=true&mapColorScheme=opposed_red_blue&fitLabelsToNodes=true", "NEWT", DownloadIcons.INSTANCE.NEWTIcon(), Group.FORMAT),
    //    PROTEGE     ("Protege", "/cgi-bin/protegeexporter?DB=__DB__&ID=__ID__", "OWL", DownloadIcons.INSTANCE.ProtegeIcon(), Group.FORMAT),
    SVG         ("SVG", ContentClientAbstract.CONTENT_SERVICE + "exporter/diagram/__STID__.svg__PARAMS__", "SVG", DownloadIcons.INSTANCE.SVGIcon(), Group.DIAGRAM),
    //    POWERPOINT  ("Powerpoint", ContentClientAbstract.CONTENT_SERVICE + "exporter/diagram/__STID__.pptx__PARAMS__", "PPTX", DownloadIcons.INSTANCE.PowerPointIcon(), Group.FORMAT),
    PNG         ("PNG", ContentClientAbstract.CONTENT_SERVICE + "exporter/diagram/__STID__.png__PARAMS__", "PNG", DownloadIcons.INSTANCE.PNGIcon(), Group.DIAGRAM, true),
    JPEG        ("JPEG", ContentClientAbstract.CONTENT_SERVICE + "exporter/diagram/__STID__.jpeg__PARAMS__", "JPEG", DownloadIcons.INSTANCE.JPEGIcon(), Group.DIAGRAM, true),
    GIF         ("GIF", ContentClientAbstract.CONTENT_SERVICE + "exporter/diagram/__STID__.gif__PARAMS__", "GIF", DownloadIcons.INSTANCE.GIFIcon(), Group.DIAGRAM, true);

    //NOTE: please put the quality values below in ascending order
    public static final List<Integer> QUALITIES = Arrays.asList(2, 5, 7);

    private String name;
    private String url;
    private String tooltip;
    private transient ImageResource icon;
    private Group group;

    public enum Group {
        FORMAT,
        DIAGRAM
    }
    private boolean hasQualityOptions;

    DownloadType(String name, String url, String tooltip, ImageResource icon, Group group) {
        this(name, url, tooltip, icon, group, false);
    }

    DownloadType(String name, String url, String tooltip, ImageResource icon, Group group, boolean hasQualityOptions) {
        this.name = name;
        this.url = url;
        this.tooltip = tooltip;
        this.icon = icon;
        this.group = group;
        this.hasQualityOptions = hasQualityOptions;
    }

    public String getName() {
        return name;
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getUrl() {
        return url;
    }

    public ImageResource getIcon() {
        return icon;
    }

    public Group getGroup() {
        return group;
    }

    public boolean hasQualityOptions() {
        return hasQualityOptions;
    }

    public static String getHost() {
        String hostName = Window.Location.getHostName();
        if (hostName.equals("localhost") || hostName.equals("127.0.0.1")) {
            return "dev.reactome.org";
        }
        return hostName;
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

        @Source("images/NEWT_editor.png")
        ImageResource NEWTIcon();
    }
}
