package org.reactome.web.pwp.client.manager.analytics;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
abstract class LocationHelper {
    public enum Location {
        PRODUCTION,
        DEV,
        RELEASE,
        CURATOR,
        LOCALHOST,
        OTHER
    }

    static Location getLocation(){
        String hostName = Window.Location.getHostName();
        if(GWT.isScript()){
            if(hostName.equals("reactome.org")){
                return Location.PRODUCTION;
            }
            if(hostName.equals("release.reactome.org")){
                return Location.RELEASE;
            }
            if(hostName.equals("dev.reactome.org")){
                return Location.DEV;
            }
            if(hostName.equals("curator.reactome.org") || hostName.equals("reactomecurator.oicr.on.ca")){
                return Location.CURATOR;
            }
        }
        if(hostName.equals("localhost") || hostName.equals("127.0.0.1")){
            return Location.LOCALHOST;
        }else {
            return Location.OTHER;
        }
    }
}
