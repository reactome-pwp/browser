package org.reactome.web.pwp.client.details.tabs.downloads.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.common.widgets.button.CustomButton;
import org.reactome.web.pwp.client.details.tabs.downloads.DownloadsTab;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class DownloadMolecule extends CustomButton {

    public DownloadMolecule(final DownloadsTab.Presenter presenter, final Pathway pathway){
        super();
        this.addStyleName("elv-Download-Item");
        this.setWidth("100px");
        this.setText("Molecules");
        this.setResource(CommonImages.INSTANCE.download());
        this.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //swap to Molecules download
                presenter.swapToMolecules(pathway);
            }
        });
        this.setTitle("View/download participating molecules.");
    }
}
