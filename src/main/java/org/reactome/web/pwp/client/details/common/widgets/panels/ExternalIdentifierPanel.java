package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.model.classes.DatabaseIdentifier;
import org.reactome.web.pwp.model.classes.DatabaseObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExternalIdentifierPanel extends DetailsPanel {

    public ExternalIdentifierPanel(List<DatabaseIdentifier> databaseIdentifiers) {
        this(null, databaseIdentifiers);
    }

    public ExternalIdentifierPanel(DetailsPanel parentPanel, List<DatabaseIdentifier> databaseIdentifiers) {
        super(parentPanel);
        //Converting it to a set is because there is a temporary problem in the db
        //and the data for some cross references is stored twice. This can be removed
        //when the problem is fixed in the data base
        Set<DatabaseIdentifier> aux = new HashSet<DatabaseIdentifier>();
        for (DatabaseIdentifier databaseIdentifier : databaseIdentifiers) {
            aux.add(databaseIdentifier);
        }
        initialize(aux);
    }

    private void initialize(Set<DatabaseIdentifier> databaseIdentifiers){
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-Advanced");

        vp.setWidth("100%");
        for (DatabaseIdentifier databaseIdentifier : databaseIdentifiers) {
            String[] aux = databaseIdentifier.getDisplayName().split(":");
            InlineLabel label = new InlineLabel(aux[0]);
            FlowPanel fp = new FlowPanel();
            fp.add(label);
            if(aux.length>=2){
                Anchor link = new Anchor(aux[1], databaseIdentifier.getUrl(), "_blank");
                link.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
                fp.add(link);
            }
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
