package org.reactome.web.pwp.client.details.tabs.downloads.widgets;

import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Returns a list of urls for the specified {@link DownloadType}.
 * The list contains either one or many urls depending on whether the specific {@link DownloadType}
 * is offered in different qualities.
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DownloadURLBuilder {
    //Mandatory params
    private DownloadType type;
    private String db;
    private DatabaseObject pathway;

    //Optional parameters
    private String diagramProfile;
    private String analysisProfile;
    private String selected;
    private String flagged;
    private AnalysisStatus status;

    public DownloadURLBuilder(final DownloadType type, final String db, final DatabaseObject pathway, final AnalysisStatus status){
        this.type = type;
        this.db = db;
        this.pathway = pathway;
        this.status = status;
    }

    public DownloadURLBuilder setDiagramProfile(final String diagramProfile) {
        this.diagramProfile = diagramProfile;
        return this;
    }

    public DownloadURLBuilder setAnalysisProfile(final String analysisProfile) {
        this.analysisProfile = analysisProfile;
        return this;
    }

    public DownloadURLBuilder setSelected(final String selected) {
        this.selected = selected;
        return this;
    }

    public DownloadURLBuilder setFlagged(final String flagged) {
        this.flagged = flagged;
        return this;
    }

    public List<String> generateUrlList() {
        List<String> rtn = new ArrayList<>();

        String baseUrl = type.getUrl()
                         .replace("__DB__",db)
                         .replace("__ID__", String.valueOf(pathway.getDbId()))
                         .replace("__STID__", pathway.getStId());

        if (baseUrl.contains("__PARAMS__")) {
            List<String> params = new ArrayList<>();
            if (selected != null) { params.add("sel=" + selected); }

            if (flagged != null)  { params.add("flg=" + flagged); }

            if (type.getName().equals("Powerpoint")) {
                params.add("profile=" + diagramProfile); //TODO this should be removed as soon as the pptx exporter uses the same parameter name
            } else {
                params.add("diagramProfile=" + diagramProfile);
            }

            if (!status.isEmpty()) {
                params.add("token=" + status.getToken());
                params.add("analysisProfile=" + analysisProfile);
            }

            String paramsStr = "?" + params.stream().collect(Collectors.joining("&"));
            baseUrl = baseUrl.replace("__PARAMS__", paramsStr);

            if (type.hasQualityOptions()) {
                String url = baseUrl;
                type.QUALITIES.forEach(quality -> {
                    rtn.add(url + "&quality=" + quality);
                });
            } else {
                rtn.add(baseUrl);
            }
        } else {
            rtn.add(baseUrl);
        }

        return rtn;
    }
}
