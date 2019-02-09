package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;

/**
 * Small icons to show whether certain analysis options where selected
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class OptionBadge extends SimplePanel {

    public enum Type {
        PROJECT_TO_HUMAN(AnalysisTabStyleFactory.RESOURCES.projectToHumanIcon(), "#89bf53", "Results are projected to Human"),
        INLCUDE_INTERACTORS(AnalysisTabStyleFactory.RESOURCES.includeInteractorsIcon(), "#f5b945", "Interactors are included");

        private transient ImageResource icon;
        private String colour;
        private String tooltip;

        Type(ImageResource icon, String colour, String tooltip) {
            this.icon = icon;
            this.colour = colour;
            this.tooltip = tooltip;
        }

    }

    public OptionBadge(Type badgeType) {
        setStyleName(AnalysisTabStyleFactory.RESOURCES.css().optionBadge());
        getElement().getStyle().setBackgroundColor(badgeType.colour);
        add(new Image(badgeType.icon));
        setTitle(badgeType.tooltip);
    }
}
