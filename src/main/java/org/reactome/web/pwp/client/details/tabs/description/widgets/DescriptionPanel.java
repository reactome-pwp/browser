package org.reactome.web.pwp.client.details.tabs.description.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.diagram.client.ViewerContainer;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.DetailsDisplay;
import org.reactome.web.pwp.client.details.common.help.HelpPopupImage;
import org.reactome.web.pwp.client.details.common.help.InstanceTypeExplanation;
import org.reactome.web.pwp.client.details.tabs.description.DescriptionTabDisplay;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.OverviewTableFactory;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.cells.DatasetCell;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.PhysicalEntity;
import org.reactome.web.pwp.model.client.classes.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DescriptionPanel extends DockLayoutPanel {

    /**
     * The HTML templates used to render the star system.
     */
    interface ReviewStatusTemplates extends SafeHtmlTemplates {
        /**
         * The template for this star rating, which includes styles and a value.
         *
         * @param score  the safe value. Since the value type is {@link SafeHtml},
         *               it will not be escaped before including it in the template.
         *               Alternatively, you could make the value type String, in which
         *               case the value would be escaped.
         * @return a {@link SafeHtml} instance
         */
        @Template(
                "<div class=\"c100 p{0} small \">" +
                      "<span> {1}/5</span>" +
                      "<div class=\"slice\">" +
                          "<div class=\"bar\"></div>" +
                          "<div class=\"fill\"></div>" +
                      "</div>" +
                "</div>")
        SafeHtml reviewStatus(String score, String scale);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */

    private static ReviewStatusTemplates templates = GWT.create(ReviewStatusTemplates.class);



    public DescriptionPanel(DatabaseObject databaseObject) {
        super(Style.Unit.PX);
        addStyleName("elv-Details-Tab");

        HorizontalPanel topBar = new HorizontalPanel();
        topBar.add(getTitle(databaseObject));
        if (databaseObject.getStId()!=null) {
            topBar.add(getStableId(databaseObject));
        }
        topBar.add(getSpecies(databaseObject));
        addNorth(topBar, 35);

        if(databaseObject instanceof Event){
            topBar.add(getReviewStatus(databaseObject));
        }

        DockLayoutPanel overview = new DockLayoutPanel(Style.Unit.EM);
        overview.addStyleName("elv-Details-OverviewPanel");

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
//        vp.getElement().getStyle().setPaddingTop(5, Style.Unit.PX);
        vp.getElement().getStyle().setPaddingBottom(25, Style.Unit.PX);

        vp.add(OverviewTableFactory.getOverviewTable(databaseObject));

        ScrollPanel scrollPanel = new ScrollPanel(vp);
        scrollPanel.setWidth("100%");
        overview.add(scrollPanel);

        add(overview);
    }

    private Widget getTitle(DatabaseObject databaseObject){
        HorizontalPanel titlePanel = new HorizontalPanel();
        titlePanel.setStyleName("elv-Details-Title");
        if (databaseObject instanceof Event) {
            if (((Event) databaseObject).getInDisease()) titlePanel.addStyleName("elv-Details-Title-Disease");
        }

        try{
            ImageResource img = databaseObject.getImageResource();
            String helpTitle = databaseObject.getSchemaClass().name;
            HTMLPanel helpContent = new HTMLPanel(InstanceTypeExplanation.getExplanation(databaseObject.getSchemaClass()));
            titlePanel.add(new HelpPopupImage(img, helpTitle, helpContent));
        }catch (Exception e){
            Console.error(getClass() + ": " + e.getMessage());
            //ToDo: Look into new Error Handling
        }
        HTMLPanel title = new HTMLPanel(databaseObject.getDisplayName());
        title.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        title.setTitle(databaseObject.getDisplayName());
        titlePanel.add(title);

        return titlePanel;
    }

    private Widget getSpecies(DatabaseObject databaseObject){
        String species = null;
        if(databaseObject instanceof PhysicalEntity){
            List<Species> speciesList = ((PhysicalEntity) databaseObject).getSpecies();
            if(!speciesList.isEmpty()){
                species = speciesList.get(0).getDisplayName();
            }
        }else if(databaseObject instanceof Event){
            Event event = (Event) databaseObject;
            if(!event.getSpecies().isEmpty()){
                species = event.getSpecies().get(0).getDisplayName();
            }
        }

        HorizontalPanel speciesPanel = new HorizontalPanel();
        if(species!=null){
            speciesPanel.setStyleName("elv-Details-Species");
            speciesPanel.add(new HTMLPanel("Species: " + species));
        }
        return speciesPanel;
    }

    private Widget getStableId(DatabaseObject databaseObject){
        String stId = databaseObject.getStIdVersion();
        if (stId == null || stId.isEmpty()) stId = databaseObject.getClassName();

        HorizontalPanel stIdPanel = new HorizontalPanel();
        stIdPanel.setStyleName("elv-Details-StId");
        stIdPanel.add(new HTMLPanel("Id: " + stId));
        return stIdPanel;
    }

    private Widget getReviewStatus(DatabaseObject databaseObject) {
        String reviewStatus = null;
        Event event = (Event) databaseObject;
        if (!event.getReviewStatus().getDisplayName().isEmpty()) {
            reviewStatus = event.getReviewStatus().getDisplayName();
        }
        Element div = DOM.createElement("div");
        HorizontalPanel reviewStatusPanel = new HorizontalPanel();
        if (reviewStatus != null) {

            SafeHtml rendered = templates.reviewStatus("40", "2");

//            if (DOM.getElementById("star") != null) {
//                Console.error("star is not empty");
//                DOM.getElementById("star").addClassName(RESOURCES.getCSS().c100());
//                DOM.getElementById("star").addClassName(RESOURCES.getCSS().c100());
//                DOM.getElementById("slice").addClassName(RESOURCES.getCSS().slice());
//                DOM.getElementById("bar").addClassName(RESOURCES.getCSS().bar());
//                DOM.getElementById("fill").addClassName(RESOURCES.getCSS().fill());
//            }
//
//
//            if (DOM.getElementById("star") == null) {
//                Console.error("star is  empty");
//            }

            reviewStatusPanel.add(new HTMLPanel(rendered));

        }
        return reviewStatusPanel;
    }
//
//    public static Resources RESOURCES;
//    static {
//        RESOURCES = GWT.create(Resources.class);
//        RESOURCES.getCSS().ensureInjected();
//    }
//
//    /**
//     * A ClientBundle of resources used by this widget.
//     */
//    public interface Resources extends ClientBundle {
//        /**
//         * The styles used in this widget.
//         */
//        @Source(ResoruceCSS.CSS)
//        ResoruceCSS getCSS();
//    }
//
//    /**
//     * Styles used by this widget.
//     */
//    @CssResource.ImportedWithPrefix("pwp-Description")
//    public interface ResoruceCSS extends CssResource {
//        /**
//         * The path to the default CSS styles used by this resource.
//         */
//        String CSS = "org/reactome/web/pwp/client/details/tabs/DescriptionTab.css";
//
//        String c100();
//
//        String small();
//
//        String fill();
//
//        String slice();
//
//        String bar();
//    }
}
