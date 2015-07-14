package org.reactome.web.pwp.client.tools.launcher;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.PathwayPortalTool;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.events.ToolSelectedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ToolLauncherPresenter extends AbstractPresenter implements ToolLauncher.Presenter {

    @SuppressWarnings("FieldCanBeLocal")
    private ToolLauncher.Display display;

    public ToolLauncherPresenter(EventBus eventBus, ToolLauncher.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        //Nothing here
    }

    @Override
    public void toolSelected(PathwayPortalTool tool) {
        this.eventBus.fireEventFromSource(new ToolSelectedEvent(tool), this);
    }
}
