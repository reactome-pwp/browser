package org.reactome.web.pwp.client.manager.analytics;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class LocationHelper {
    public enum Location {
        PRODUCTION,
        DEV,
        CURATOR,
        LOCALHOST,
        OTHER
    }

    public static Location getLocation(){
        String hostName = Window.Location.getHostName();
        if(GWT.isScript()){
            if(hostName.equals("www.reactome.org") || hostName.equals("reactome.org")){
                return Location.PRODUCTION;
            }
            if(hostName.equals("reactomedev.oicr.on.ca")){
                return Location.DEV;
            }
            if(hostName.equals("reactomecurator.oicr.on.ca")){
                return Location.CURATOR;
            }
        }
        if(hostName.equals("localhost") || hostName.equals("127.0.0.1")){
            return Location.LOCALHOST;
        }else {
            return Location.OTHER;
        }
    }

    public static boolean isAnalysisAvailable(){
        switch (getLocation()){
            case CURATOR:
               return false;
            default:
                return true;
        }
    }

    public static boolean isBeta(){
        switch (getLocation()){
            case DEV:
            case LOCALHOST:
                return true;
            default:
                return false;
        }
    }
}
