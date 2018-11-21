package org.reactome.web.pwp.client.details.tabs.molecules.widget;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.pwp.client.details.tabs.molecules.model.type.PropertyType;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesViewPanel extends DockLayoutPanel {

    private ScrollPanel scrollPanel;
    private final VerticalPanel containerTP; //necessary because scrollPanel can contain only ONE child, contains all TablePanels
    private TablePanel chemicalsPanel;
    private TablePanel proteinsPanel;
    private TablePanel sequencesPanel;
    private TablePanel drugsPanel;
    private TablePanel othersPanel;

    public MoleculesViewPanel(final Result result) {
        super(Style.Unit.PX);

        //Creating and filling container for TablePanels.
        containerTP = new VerticalPanel();
        containerTP.setWidth("100%");
        containerTP.getElement().getStyle().setPaddingTop(5, Style.Unit.PX);
        containerTP.getElement().getStyle().setPaddingBottom(25, Style.Unit.PX);

        /**
         * Creating all the required TablePanels. If there are no Molecules of one type then the corresponding
         * TablePanel will not be set.
         */
        int size;
        if(result != null){
            size = result.getChemicals().size();
            if(size > 0){
                chemicalsPanel = new TablePanel(PropertyType.CHEMICAL_COMPOUNDS, size, result);
                chemicalsPanel.addStyleName("elv-Details-OverviewRow");
                chemicalsPanel.asWidget().setWidth("99%");

                containerTP.add(chemicalsPanel);
            }

            size = result.getProteins().size();
            if(size > 0){
                proteinsPanel = new TablePanel(PropertyType.PROTEINS, size, result);
                proteinsPanel.addStyleName("elv-Details-OverviewRow");
                proteinsPanel.asWidget().setWidth("99%");

                containerTP.add(proteinsPanel);
            }

            size = result.getSequences().size();
            if(size > 0){
                sequencesPanel = new TablePanel(PropertyType.SEQUENCES, size, result);
                sequencesPanel.addStyleName("elv-Details-OverviewRow");
                sequencesPanel.asWidget().setWidth("99%");

                containerTP.add(sequencesPanel);
            }

            size = result.getDrugs().size();
            if(size > 0){
                drugsPanel = new TablePanel(PropertyType.DRUG, size, result);
                drugsPanel.addStyleName("elv-Details-OverviewRow");
                drugsPanel.asWidget().setWidth("99%");

                containerTP.add(drugsPanel);
            }

            size = result.getOthers().size();
            if(size > 0){
                othersPanel = new TablePanel(PropertyType.OTHERS, size, result);
                othersPanel.addStyleName("elv-Details-OverviewRow");
                othersPanel.asWidget().setWidth("99%");

                containerTP.add(othersPanel);
            }
        }

        scrollPanel = new ScrollPanel(containerTP);
        scrollPanel.addStyleName("elv-Details-OverviewPanel");

        this.add(scrollPanel);
    }

    /**
     * Avoids loading if Pathway-with-Diagram stays the same.
     * @param result updated version of result
     */
    public void update(Result result) {

        //Update all the TablePanels for the Molecules...
        int size;
        if(result != null){
            size = result.getChemicals().size();
            if(size > 0){
                chemicalsPanel.update(size, result);
            }

            size = result.getProteins().size();
            if(size > 0){
                proteinsPanel.update(size, result);
            }

            size = result.getSequences().size();
            if(size > 0){
                sequencesPanel.update(size, result);
            }

            size = result.getDrugs().size();
            if(size > 0){
                drugsPanel.update(size, result);
            }

            size = result.getOthers().size();
            if(size > 0){
                othersPanel.update(size, result);
            }
        }

        //Finally set the new content.
        scrollPanel.removeFromParent();
        scrollPanel = new ScrollPanel(containerTP);
        scrollPanel.addStyleName("elv-Details-OverviewPanel");
        this.add(scrollPanel);
    }

}
