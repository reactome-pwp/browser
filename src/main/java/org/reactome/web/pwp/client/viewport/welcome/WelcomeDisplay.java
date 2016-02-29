package org.reactome.web.pwp.client.viewport.welcome;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class WelcomeDisplay extends DockLayoutPanel implements Welcome.Display {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Welcome.Presenter presenter;
    private ScrollPanel welcome;

    public WelcomeDisplay() {
        super(Style.Unit.PX);
        HTMLPanel welcome = new HTMLPanel(WelcomeResources.INSTANCE.getWelcomeMessage().getText());
        this.welcome = new ScrollPanel(welcome);

        add(this.welcome);
    }

    @Override
    public boolean isVisible() {
        return welcome.isVisible();
    }

    @Override
    public void setPresenter(Welcome.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setVisible(boolean visible) {
        welcome.setVisible(visible);
    }

    public interface WelcomeResources extends ClientBundle {

        WelcomeResources INSTANCE = GWT.create(WelcomeResources.class);

        @Source("welcome.html")
        TextResource getWelcomeMessage();

    }
}
