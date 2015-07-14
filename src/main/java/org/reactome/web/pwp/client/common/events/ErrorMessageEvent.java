package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.ErrorMessageHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ErrorMessageEvent extends GwtEvent<ErrorMessageHandler> {
    public static final Type<ErrorMessageHandler> TYPE = new Type<>();

    private String message;
    private Throwable throwable;

    public ErrorMessageEvent(String message) {
        this.message = message;
    }

    public ErrorMessageEvent(String message, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

    @Override
    public Type<ErrorMessageHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ErrorMessageHandler handler) {
        handler.onInternalMessage(this);
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String toString() {
        return "InternalMessageEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
