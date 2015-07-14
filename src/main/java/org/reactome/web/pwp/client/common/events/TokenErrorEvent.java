package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.TokenErrorHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TokenErrorEvent extends GwtEvent<TokenErrorHandler> {
    public static final Type<TokenErrorHandler> TYPE = new Type<>();

    private String token;

    public TokenErrorEvent(String token) {
        this.token = token;
    }

    @Override
    public Type<TokenErrorHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TokenErrorHandler handler) {
        handler.onTokenError(this);
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "TokenErrorEvent{" +
                "token='" + token + '\'' +
                '}';
    }
}
