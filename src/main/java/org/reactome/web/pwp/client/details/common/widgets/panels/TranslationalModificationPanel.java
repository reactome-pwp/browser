package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.TranslationalModification;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TranslationalModificationPanel extends DetailsPanel implements TransparentPanel {
    private TranslationalModification translationalModification;

    @SuppressWarnings("UnusedDeclaration")
    public TranslationalModificationPanel(TranslationalModification translationalModification) {
        this(null, translationalModification);
    }

    public TranslationalModificationPanel(DetailsPanel parentPanel, TranslationalModification translationalModification) {
        super(parentPanel);
        this.translationalModification = translationalModification;
        initialize();
    }

    private void initialize(){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("99%");

        if(this.translationalModification.getCoordinate()!=null){
            FlexTable flexTable = new FlexTable();
            flexTable.setWidth("100%");
            flexTable.getColumnFormatter().setWidth(0, "75px");
            flexTable.setCellPadding(0);
            flexTable.setCellSpacing(0);

            flexTable.setWidget(0, 0, new Label("Coordinate"));
            flexTable.setWidget(0, 1, new Label(this.translationalModification.getCoordinate().toString()));
            vp.add(flexTable);
        }

        vp.add(new Label("PSI MOD:"));
        //We associate our parentPanel as the parent of the next panel because this panel is a kind of auxiliary panel
        Widget pPanel = new PsiModPanel(this.parentPanel, this.translationalModification.getPsiMod());
        pPanel.setWidth("99%");
        pPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(pPanel);

        initWidget(vp);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.translationalModification;
    }
}
