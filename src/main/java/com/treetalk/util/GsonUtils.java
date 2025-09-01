package com.treetalk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/8/26 14:39
 */
public class GsonUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setDateFormat(DATE_FORMAT)
            .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> 
                src == null ? null : new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ofPattern(DATE_FORMAT))))
            .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> 
                json == null || json.isJsonNull() ? null : LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern(DATE_FORMAT)))
            .create();


    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}