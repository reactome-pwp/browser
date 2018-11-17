package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Disease;
import org.reactome.web.pwp.model.client.classes.Drug;
import org.reactome.web.pwp.model.client.classes.ReferenceEntity;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DrugPanel extends DetailsPanel implements TransparentPanel {
    private Drug drug;

    @SuppressWarnings("UnusedDeclaration")
    public DrugPanel(Drug drug) {
        this(null, drug);
    }

    public DrugPanel(DetailsPanel parentPanel, Drug drug) {
        super(parentPanel);
        this.drug = drug;
        setLoaded(true);    //EntityWithAccessionedSequence will always be passed as a fully loaded object
        initialize();
    }

    private void initialize(){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");

        if(this.drug.getReferenceEntity()!=null) vp.add(getExternalIdentifier(this.drug.getReferenceEntity()));

        if(this.drug.getDisease()!=null) vp.add(getDiseasePanel(this.drug.getDisease()));

        initWidget(vp);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.drug;
    }

    private Widget getExternalIdentifier(ReferenceEntity re){
        InlineLabel label = new InlineLabel(re.getDatabaseName());
        Anchor link = new Anchor(re.getIdentifier(), re.getUrl(), "_blank");
        link.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);

        FlowPanel fp = new FlowPanel();
        fp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        fp.add(label);
        fp.add(link);
        return fp;
    }

    private Widget getDiseasePanel(List<Disease> diseaseList){
        VerticalPanel vp = new VerticalPanel();
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        vp.setWidth("99%");

        vp.add(new Label("Indicated for:"));
        for (Disease disease : diseaseList) {
            Widget pPanel = new ExternalOntologyPanel(this, disease);
            pPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(pPanel);
        }

        return vp;
    }

}
