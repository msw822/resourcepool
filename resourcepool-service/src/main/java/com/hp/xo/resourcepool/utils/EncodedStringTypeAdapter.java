package com.hp.xo.resourcepool.utils;

import java.lang.reflect.Type;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EncodedStringTypeAdapter implements JsonSerializer<String>{
    public static final Logger s_logger = Logger.getLogger(EncodedStringTypeAdapter.class.getName());

    public JsonElement serialize(String src, Type typeOfResponseObj, JsonSerializationContext ctx) {
        return new JsonPrimitive(encodeString(src));

    }

    private static String encodeString(String value) {
        if (! ServiceOptionUtil.obtainEncodeResponse()) {
            return value;
        }
        
        try {
            return new URLEncoder().encode(value).replaceAll("\\+", "%20");
        } catch (Exception e) {
            s_logger.warn("Unable to encode: " + value, e);
        }
        return value;
    }
    
}
