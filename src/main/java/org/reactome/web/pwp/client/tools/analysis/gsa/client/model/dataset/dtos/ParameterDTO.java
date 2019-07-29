package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.dtos;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;

import static jsinterop.annotations.JsPackage.GLOBAL;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@JsType(namespace = GLOBAL, name = "Object", isNative = true)
public class ParameterDTO {
    private String name;
    private String value;

    protected ParameterDTO() {
        // Nothing here
    }

    @JsOverlay
    public static ParameterDTO create(String name, String value) {
        ParameterDTO rtn = new ParameterDTO();
        rtn.name = name;
        rtn.value = value;

        return rtn;
    }
}
