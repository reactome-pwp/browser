package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.model.client.classes.DatabaseIdentifier;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

import java.util.Collection;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExternalIdentifierPanel extends DetailsPanel {

    public ExternalIdentifierPanel(List<DatabaseIdentifier> databaseIdentifiers) {
        this(null, databaseIdentifiers);
    }

    public ExternalIdentifierPanel(DetailsPanel parentPanel, List<DatabaseIdentifier> databaseIdentifiers) {
        super(parentPanel);
        initialize(databaseIdentifiers);
    }

    private void initialize(Collection<DatabaseIdentifier> databaseIdentifiers) {
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-Advanced");

        vp.setWidth("100%");
        for (DatabaseIdentifier databaseIdentifier : databaseIdentifiers) {
            InlineLabel label = new InlineLabel(databaseIdentifier.getDatabaseName());
            FlowPanel fp = new FlowPanel();
            fp.add(label);
            Anchor link = new Anchor(databaseIdentifier.getIdentifier(), databaseIdentifier.getUrl(), "_blank");
            link.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
            fp.add(link);
            vp.add(fp);
        }
        initWidget(vp);
        getElement().getStyle().setPadding(10, Style.Unit.PX);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return null;
    }
}
