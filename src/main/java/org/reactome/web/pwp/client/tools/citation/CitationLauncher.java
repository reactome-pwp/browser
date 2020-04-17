package org.reactome.web.pwp.client.tools.citation;

import org.reactome.web.pwp.client.common.module.BrowserModule;

/**
 * @author Yusra Haider <yhaider@ebi.ac.uk>
 */

public interface CitationLauncher {

    interface Presenter extends BrowserModule.Presenter {
        void displayClosed();
    }

    interface Display extends BrowserModule.Display {
        void hide();

        void center();

        void show();

        void setPresenter(CitationLauncher.Presenter presenter);

        void setCitation(String citation);

        void setExportBar(String id);
    }
}





