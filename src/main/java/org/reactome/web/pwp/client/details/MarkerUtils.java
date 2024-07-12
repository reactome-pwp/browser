package org.reactome.web.pwp.client.details;

import org.reactome.web.pwp.model.client.classes.EntityWithAccessionedSequence;
import org.reactome.web.pwp.model.client.classes.MarkerReference;
import org.reactome.web.pwp.model.client.classes.Publication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerUtils {

    public static Map<Long, List<Publication>> getMarkerPublicationsFromMarkerRefs(List<MarkerReference> markerReferences) {
        Map<Long, List<Publication>> literatureReferenceMap = new HashMap<>();
        for (MarkerReference markerReference : markerReferences) {
            if (markerReference.getMarker() != null) {
                EntityWithAccessionedSequence ewas = markerReference.getMarker();
                    literatureReferenceMap.put(ewas.getDbId(), markerReference.getLiteratureReference());
            }
        }
        return literatureReferenceMap;
    }
}
