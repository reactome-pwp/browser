package org.reactome.web.pwp.client.details.common.widgets.disclosure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DisclosureImages extends ClientBundle {

    public DisclosureImages INSTANCE = GWT.create(DisclosureImages.class);

    @ClientBundle.Source("images/minus.png")
    ImageResource getCollapseImage();

    @ClientBundle.Source("images/plus.png")
    ImageResource getExpandImage();

    @ClientBundle.Source("images/loading.gif")
    ImageResource getLoadingImage();
}
