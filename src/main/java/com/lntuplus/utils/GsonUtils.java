package com.lntuplus.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    private static GsonUtils sGsonUtils = null;

    private static Gson sGson = null;

    private GsonUtils() {
        sGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public static Gson getInstance() {
        if (sGsonUtils == null) {
            synchronized (Gson.class) {
                if (sGsonUtils == null) {
                    sGsonUtils = new GsonUtils();
                }
            }
        }
        return sGsonUtils.getGson();
    }

    private Gson getGson() {
        return sGson;
    }
}
