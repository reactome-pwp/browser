package org.reactome.web.pwp.client.details.tabs.analysis.widgets.downloads;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.diagram.profiles.analysis.AnalysisColours;
import org.reactome.web.diagram.profiles.diagram.DiagramColours;
import org.reactome.web.fireworks.profiles.FireworksColours;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnalysisDownloadItem extends FocusPanel implements ClickHandler {
    private AnalysisDownloadType type;
    private String token;
    private String resource;
    private String species;

    public AnalysisDownloadItem(final AnalysisDownloadType type, final String token, final String resource, final String species) {
        this.type = type;
        this.token = token;
        this.resource = resource;
        this.species = species;
        setStyleName(RESOURCES.getCSS().item());

        Image icon = new Image(type.getIcon());
        icon.setStyleName(RESOURCES.getCSS().icon());

        FlowPanel leftPanel = new FlowPanel();
        leftPanel.setStyleName(RESOURCES.getCSS().leftPanel());
        leftPanel.add(icon);

        Label title = new Label(type.getTitle());
        title.setStyleName(RESOURCES.getCSS().title());
        HTMLPanel info = new HTMLPanel(type.getInfo().getText().replaceAll("###RESOURCE###", resource));
        info.setStyleName(RESOURCES.getCSS().info());

        FlowPanel rightPanel = new FlowPanel();
        rightPanel.setStyleName(RESOURCES.getCSS().rightPanel());
        rightPanel.add(title);
        rightPanel.add(info);

        FlowPanel main = new FlowPanel();
        main.add(leftPanel);
        main.add(rightPanel);
        add(main);

        addClickHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        String diagramProfile = DiagramColours.get().PROFILE.getName();
        String analysisProfile = AnalysisColours.get().PROFILE.getName();
        String fireworksProfile = FireworksColours.getSelectedProfileName();
        if (fireworksProfile == null || fireworksProfile.equals("undefined")) fireworksProfile = FireworksColours.ProfileType.getStandard().getProfile().getName();

        String link = type.getLink().replaceAll("###RESOURCE###", resource)
                                    .replaceAll("###TOKEN###", token)
                                    .replaceAll("###SPECIES###", URL.encode(species))
                                    .replaceAll("###D_PROFILE###", diagramProfile)
                                    .replaceAll("###A_PROFILE###", analysisProfile)
                                    .replaceAll("###F_PROFILE###", fireworksProfile);

        Window.open(link, type == AnalysisDownloadType.PDF_REPORT ? "_blank" : "_self", "");
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    @CssResource.ImportedWithPrefix("pwp-AnalysisDownloadItem")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/downloads/AnalysisDownloadItem.css";

        String item();

        String leftPanel();

        String rightPanel();

        String icon();

        String title();

        String info();

    }
}
