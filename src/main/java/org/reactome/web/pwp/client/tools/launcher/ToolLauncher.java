package org.reactome.web.pwp.client.tools.launcher;

import org.reactome.web.pwp.client.common.PathwayPortalTool;
import org.reactome.web.pwp.client.common.module.BrowserModule;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ToolLauncher {

    interface Presenter extends BrowserModule.Presenter {
        void toolSelected(PathwayPortalTool tool);
    }

    interface Display extends BrowserModule.Display {
        void setPresenter(Presenter presenter);
    }
}
