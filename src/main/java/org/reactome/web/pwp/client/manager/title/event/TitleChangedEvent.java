package org.reactome.web.pwp.client.manager.title.event;


import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.manager.title.handler.TitleChangedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TitleChangedEvent extends GwtEvent<TitleChangedHandler> {
    public static Type<TitleChangedHandler> TYPE = new Type<>();

    public static boolean REPORT = true;
    private String title;

    public TitleChangedEvent(String title) {
        this.title = title;
    }

    @Override
    public Type<TitleChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TitleChangedHandler handler) {
        handler.onTitleChanged(this);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        if (REPORT) {
            return "TitleChangedEvent{" +
                    "title='" + title + '\'' +
                    '}';
        }
        return null; //Returning null or empty prevents the event to appear in the console
    }
}
