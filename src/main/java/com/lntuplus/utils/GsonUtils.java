package com.lntuplus.utils;

import com.google.gson.Gson;

public class GsonUtils {

    private static GsonUtils sGsonUtils = null;

    private static Gson sGson = null;

    private GsonUtils() {
        sGson = new Gson();
    }

    public static Gson getInstance() {
        if (sGsonUtils == null) {
            synchronized (DBSessionFactory.class) {
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
