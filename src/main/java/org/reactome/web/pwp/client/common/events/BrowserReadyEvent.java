package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.BrowserReadyHandler;
import org.reactome.web.pwp.model.client.classes.DBInfo;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class BrowserReadyEvent extends GwtEvent<BrowserReadyHandler> {
    public static final Type<BrowserReadyHandler> TYPE = new Type<>();

    private DBInfo dbInfo;

    public BrowserReadyEvent(DBInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    @Override
    public Type<BrowserReadyHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BrowserReadyHandler handler) {
        handler.onBrowserReady(this);
    }

    public DBInfo getDbInfo() {
        return dbInfo;
    }

    @Override
    public String toString() {
        return "BrowserReadyEvent{}";
    }
}
