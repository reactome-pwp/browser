package org.reactome.web.pwp.client.tools.citation;

import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.model.client.common.ContentClientAbstract;

public enum ExportFormat {
    //TODO fix the icons

    BIBTEX("BibTeX", ".bib", CommonImages.INSTANCE.download(), ContentClientAbstract.CONTENT_SERVICE + "citation/export?" + "isPathway=" + "false" + "&" + "ext=" + "bib", "View / Download As BibTeX"),
    RIS("RIS", ".ris", CommonImages.INSTANCE.download(), ContentClientAbstract.CONTENT_SERVICE + "citation/export?" + "isPathway=" + "false" + "&" + "ext=" + "ris", "View / Download As RIS"),
    TEXT("Text", ".txt", CommonImages.INSTANCE.download(), ContentClientAbstract.CONTENT_SERVICE + "citation/export?" + "isPathway=" + "false" + "&" + "ext=" + "txt", "View / Download As Text");

    private String formatName;
    private String ext;
    private transient ImageResource icon;
    private String url;
    String title;

    ExportFormat(String formatName, String ext, ImageResource icon, String url, String title) {
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
}
