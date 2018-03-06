package org.reactome.web.pwp.client.details.common.widgets.button;

import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.diagram.common.IconButton;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class IconToggleButton extends IconButton {

    private boolean isActive = false;
    private ImageResource img;
    private ImageResource imgActive;

    public IconToggleButton(String text, ImageResource img, ImageResource imgActive) {
        super(text, img);
        this.img = img;
        this.imgActive = imgActive;
        addClickHandler(event -> {
            isActive = !isActive;
            update();
        });
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
        update();
    }

    private void update() {
        if(isActive) {
            setImage(imgActive);
        } else {
            setImage(img);
        }
    }
}
