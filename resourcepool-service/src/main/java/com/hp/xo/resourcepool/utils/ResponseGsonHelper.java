package com.hp.xo.resourcepool.utils;

import com.google.gson.GsonBuilder;
import com.hp.xo.resourcepool.model.ResponseObject;

/**
 * The ApiResonseGsonHelper is different from ApiGsonHelper - it registeres one more adapter for String type required for api response encoding
 */
public class ResponseGsonHelper {
    private static final GsonBuilder s_gBuilder;

    static {
        s_gBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        s_gBuilder.setVersion(1.3);
        s_gBuilder.registerTypeAdapter(ResponseObject.class, new ResponseObjectTypeAdapter());
        s_gBuilder.registerTypeAdapter(String.class, new EncodedStringTypeAdapter());
    }

    public static GsonBuilder getBuilder() {
        return s_gBuilder;
    }
}
