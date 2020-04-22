package org.reactome.web.pwp.client.tools.citation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.model.client.common.ContentClientAbstract;

public enum ExportType {

    BIBTEX("BibTeX", ".bib", ExportIcons.INSTANCE.bibIcon(), ContentClientAbstract.CONTENT_SERVICE + "citation/export?" + "isPathway=" + "false" + "&" + "ext=" + "bib", "View / Download As BibTeX"),
    RIS("RIS", ".ris", ExportIcons.INSTANCE.risIcon(), ContentClientAbstract.CONTENT_SERVICE + "citation/export?" + "isPathway=" + "false" + "&" + "ext=" + "ris", "View / Download As RIS"),
    TEXT("Text", ".txt", ExportIcons.INSTANCE.txtIcon(), ContentClientAbstract.CONTENT_SERVICE + "citation/export?" + "isPathway=" + "false" + "&" + "ext=" + "txt", "View / Download As Text");

    private String formatName;
    private String ext;
    private transient ImageResource icon;
    private String url;
    String title;

    ExportType(String formatName, String ext, ImageResource icon, String url, String title) {
        this.formatName = formatName;
        this.ext = ext;
        this.icon = icon;
        this.url = url;
        this.title = title;
    }

    public String getFormatName() {
        return formatName;
    }

    public String getExt() {
        return ext;
    }

    public ImageResource getIcon() {
        return icon;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }


    public interface ExportIcons extends ClientBundle {

        ExportIcons INSTANCE = GWT.create(ExportType.ExportIcons.class);

        @Source("images/bib_download.png")
        ImageResource bibIcon();

        @Source("images/ris_download.png")
        ImageResource risIcon();

        @Source("images/txt_download.png")
        ImageResource txtIcon();
    }
}
