package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SpeciesPanel extends DetailsPanel implements TransparentPanel {
    private Species species;

    public SpeciesPanel(Species species) {
        this(null, species);
    }

    public SpeciesPanel(DetailsPanel parentPanel, List<Species> speciesList){
        super(parentPanel);

        StringBuilder sb = new StringBuilder();
        String s = "";
        for (Species species : speciesList) {
            sb.append(s);
            sb.append(species.getDisplayName());
            s = ", ";
        }
        HorizontalPanel hp = new HorizontalPanel();
        hp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label title = new Label("Species:");
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        hp.add(title);
        hp.add(new HTMLPanel(sb.toString()));

        initWidget(hp);
    }

    public SpeciesPanel(DetailsPanel parentPanel, Species species) {
        super(parentPanel);
        this.species = species;

        initWidget(new HTMLPanel(species.getDisplayName()));
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return species;
    }
}
