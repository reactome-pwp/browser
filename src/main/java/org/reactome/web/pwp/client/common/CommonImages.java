package org.reactome.web.pwp.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface CommonImages extends ClientBundle {

    CommonImages INSTANCE = GWT.create(CommonImages.class);

    @Source("images/analysisTools.png")
    ImageResource analysisTool();

    @Source("images/back.png")
    ImageResource back();

    @Source("images/download.png")
    ImageResource download();

    @Source("images/download_file.png")
    ImageResource downloadFile();

    @Source("images/exclamation.png")
    ImageResource exclamation();

    @Source("images/external_link_icon.gif")
    ImageResource externalLink();

    @Source("images/error.png")
    ImageResource error();

    @Source("images/information.png")
    ImageResource information();

    @Source("images/loader.gif")
    ImageResource loader();

    @Source("images/logo.png")
    ImageResource logo();

    @Source("images/ORCID.png")
    ImageResource orcid();

    @Source("images/success.png")
    ImageResource success();

}
