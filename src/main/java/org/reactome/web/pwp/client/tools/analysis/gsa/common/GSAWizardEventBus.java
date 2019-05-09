package org.reactome.web.pwp.client.tools.analysis.gsa.common;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import org.reactome.web.diagram.util.Console;
import org.reactome.web.pwp.client.Browser;

import java.util.Date;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAWizardEventBus extends SimpleEventBus {
    private DateTimeFormat fmt = DateTimeFormat.getFormat("HH:mm:ss");

    @Override
    public void fireEvent(GwtEvent<?> event) {
        String msg = "Please do not use fireEvent. Use fireEventFromSource instead.";
        throw new RuntimeException(msg);
    }

    @Override
    public void fireEventFromSource(GwtEvent<?> event, Object source) {
        if(Browser.VERBOSE) {
            Console.info(
                    this.fmt.format(new Date()) + " #GSAWizardEvent# " +
                            source.getClass().getSimpleName() + " >> " +
                            event
            );
        }
        super.fireEventFromSource(event, source);
    }
}
