package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ExtendedTextBox extends TextBox {

    public ExtendedTextBox() {
        super();
        sinkEvents(Event.ONPASTE);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
            case Event.ONPASTE:
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                    @Override
                    public void execute() {
                        ValueChangeEvent.fire(ExtendedTextBox.this, getText());
                    }

                });
                break;
        }
    }
}
