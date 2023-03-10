package org.reactome.web.pwp.client.details.tabs.description.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.help.HelpPopup;
import org.reactome.web.pwp.client.details.common.help.HelpPopupImage;
import org.reactome.web.pwp.client.details.common.help.InstanceTypeExplanation;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.OverviewTableFactory;
import org.reactome.web.pwp.model.client.classes.*;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DescriptionPanel extends DockLayoutPanel implements MouseOverHandler, MouseOutHandler {

    private HelpPopup popup;

    /**
     * The HTML templates used to render the star system.
     */
    interface ReviewStatusTemplates extends SafeHtmlTemplates {
        /**
         * The template for this star rating, which includes star rating and a value.
         *
         * @param stars review status value
         *              five stars - 100
         *              four stars - 80
         *              three stars - 60
         *              two stars - 40
         *              one star - 20
         * @param score convert to a score to display in PWB
         *              example: 100 - 5
         * @return a {@link SafeHtml} instance
         */
        @Template(
                "<div id = \"star\" class=\"c100 p{0} small \">" +
                        "<span> {1}/5</span>" +
                        "<div id = \"star-slice\" class=\"slice\">" +
                        "<div id = \"star-bar\" class=\"bar\"></div>" +
                        "<div id = \"star-fill\" class=\"fill\"></div>" +
                        "</div>" +
                        "</div>")
        SafeHtml reviewStatus(String stars, String score);
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
        if (databaseObject.getStId() != null) {
            topBar.add(getStableId(databaseObject));
        }
        topBar.add(getSpecies(databaseObject));
        addNorth(topBar, 35);

        if (databaseObject instanceof Event) {
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

    private Widget getTitle(DatabaseObject databaseObject) {
        HorizontalPanel titlePanel = new HorizontalPanel();
        titlePanel.setStyleName("elv-Details-Title");
        if (databaseObject instanceof Event) {
            if (((Event) databaseObject).getInDisease()) titlePanel.addStyleName("elv-Details-Title-Disease");
        }

        try {
            ImageResource img = databaseObject.getImageResource();
            String helpTitle = databaseObject.getSchemaClass().name;
            HTMLPanel helpContent = new HTMLPanel(InstanceTypeExplanation.getExplanation(databaseObject.getSchemaClass()));
            titlePanel.add(new HelpPopupImage(img, helpTitle, helpContent));
        } catch (Exception e) {
            Console.error(getClass() + ": " + e.getMessage());
            //ToDo: Look into new Error Handling
        }
        HTMLPanel title = new HTMLPanel(databaseObject.getDisplayName());
        title.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        title.setTitle(databaseObject.getDisplayName());
        titlePanel.add(title);

        return titlePanel;
    }

    private Widget getSpecies(DatabaseObject databaseObject) {
        String species = null;
        if (databaseObject instanceof PhysicalEntity) {
            List<Species> speciesList = ((PhysicalEntity) databaseObject).getSpecies();
            if (!speciesList.isEmpty()) {
                species = speciesList.get(0).getDisplayName();
            }
        } else if (databaseObject instanceof Event) {
            Event event = (Event) databaseObject;
            if (!event.getSpecies().isEmpty()) {
                species = event.getSpecies().get(0).getDisplayName();
            }
        }

        HorizontalPanel speciesPanel = new HorizontalPanel();
        if (species != null) {
            speciesPanel.setStyleName("elv-Details-Species");
            speciesPanel.add(new HTMLPanel("Species: " + species));
        }
        return speciesPanel;
    }

    private Widget getStableId(DatabaseObject databaseObject) {
        String stId = databaseObject.getStIdVersion();
        if (stId == null || stId.isEmpty()) stId = databaseObject.getClassName();

        HorizontalPanel stIdPanel = new HorizontalPanel();
        stIdPanel.setStyleName("elv-Details-StId");
        stIdPanel.add(new HTMLPanel("Id: " + stId));
        return stIdPanel;
    }

    private Widget getReviewStatus(DatabaseObject databaseObject) {
        ReviewStatus reviewStatus = null;
        Event event = (Event) databaseObject;
        if (event.getReviewStatus() != null) {
            reviewStatus = event.getReviewStatus();
        }
        HorizontalPanel reviewStatusPanel = new HorizontalPanel();
        FocusPanel helpPanel = new FocusPanel();
        SafeHtml rendered = null;
        if (reviewStatus != null && reviewStatus.getDisplayName() != null) {
            switch (reviewStatus.getDisplayName()) {
                case "five stars":
                    rendered = templates.reviewStatus("100", "5");
                    break;
                case "four stars":
                    rendered = templates.reviewStatus("80", "4");
                    break;
                case "three stars":
                    rendered = templates.reviewStatus("60", "3");
                    break;
                case "two stars":
                    rendered = templates.reviewStatus("40", "2");
                    break;
                case "one star":
                    rendered = templates.reviewStatus("20", "1");
            }

            String helpTitle = "Review Status";
            HTMLPanel helpContent = new HTMLPanel("" +
                    "Review Based Release of Reactome Content. \n <br>" +
                    " 1 - Unreleased, awaiting internal review. \n <br> " +
                    " 2 - Unreleased, awaiting internal reassessment after major updates. \n <br> " +
                    " 3 - Released, awaiting external review. \n <br> " +
                    " 4 - Released, awaiting external reassessment after major updates. \n <br> " +
                    " 5 - Released, fully reviewed. \n <br>"
            );
            popup = new HelpPopup(helpTitle, helpContent);
            helpPanel.addMouseOutHandler(this);
            helpPanel.addMouseOverHandler(this);
            helpPanel.getElement().getStyle().setProperty("cursor", "help");

            HTMLPanel reviewStatusTitle = new HTMLPanel("Review Status: ");
            reviewStatusTitle.addStyleName("elv-Details-ReviewStatus");
            reviewStatusPanel.add(reviewStatusTitle);
            reviewStatusPanel.add(new HTMLPanel(rendered));
            helpPanel.add(reviewStatusPanel.asWidget());
        }
        return helpPanel;
    }

    @Override
    public void onMouseOut(MouseOutEvent mouseOutEvent) {
        popup.hide(true);
    }

    @Override
    public void onMouseOver(MouseOverEvent mouseOverEvent) {
        popup.setPositionAndShow(mouseOverEvent);
    }

}
