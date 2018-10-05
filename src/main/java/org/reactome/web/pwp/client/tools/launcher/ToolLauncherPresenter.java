package org.reactome.web.pwp.client.tools.launcher;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.pwp.client.common.PathwayPortalTool;
import org.reactome.web.pwp.client.common.events.BrowserReadyEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.events.ToolSelectedEvent;
import org.reactome.web.pwp.client.common.handlers.BrowserReadyHandler;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.model.client.classes.DBInfo;

import java.util.Objects;

import static org.reactome.web.pwp.client.tools.launcher.ToolLauncher.ToolStatus.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ToolLauncherPresenter extends AbstractPresenter implements ToolLauncher.Presenter, BrowserReadyHandler {

    private DBInfo dbInfo;
    @SuppressWarnings("FieldCanBeLocal")
    private ToolLauncher.Display display;

    public ToolLauncherPresenter(EventBus eventBus, ToolLauncher.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);

        this.eventBus.addHandler(BrowserReadyEvent.TYPE, this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        //Nothing here
    }

    @Override
    public void toolSelected(PathwayPortalTool tool) {
        this.eventBus.fireEventFromSource(new ToolSelectedEvent(tool), this);
    }

    private void checkAnalysisStatus() {
        AnalysisClient.getDatabaseInformation(new AnalysisHandler.DatabaseInformation() {
            @Override
            public void onDBInfoLoaded(org.reactome.web.analysis.client.model.DBInfo dbInfo) {
                if (!Objects.equals(ToolLauncherPresenter.this.dbInfo.getChecksum(), dbInfo.getChecksum())){
                    display.setStatus(WARNING);
                } else {
                    display.setStatus(ACTIVE);
                }
            }

            @Override
            public void onDBInfoError(AnalysisError error) {
                display.setStatus(ERROR);
            }

            @Override
            public void onAnalysisServerException(String message) {
                display.setStatus(ERROR);
            }
        });
    }

    @Override
    public void onBrowserReady(BrowserReadyEvent event) {
        this.dbInfo = event.getDbInfo();
        this.checkAnalysisStatus();
    }
}
