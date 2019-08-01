package org.reactome.web.pwp.client.tools.analysis.gsa.common;

import jsinterop.annotations.JsType;

import static jsinterop.annotations.JsPackage.GLOBAL;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@JsType(isNative=true, namespace=GLOBAL)
public class JSON {
    public native static String stringify(Object obj);

    public native static Object parse(String obj);
}
