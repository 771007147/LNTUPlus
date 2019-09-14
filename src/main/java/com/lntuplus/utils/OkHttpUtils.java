package com.lntuplus.utils;

import com.lntuplus.action.LoginAction;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {

    private static OkHttpUtils sOkHttpUtils;
    private static final String PORT1 = "http://202.199.224.24:11180/newacademic";
    private static final String PORT2 = "http://202.199.224.24:11081/academic";
    private static Dispatcher dispatcher = new Dispatcher();

    private static OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)// 设置连接超时时间
            .readTimeout(5, TimeUnit.SECONDS)// 设置读取超时时间
            .dispatcher(dispatcher)
            .build();

    public static OkHttpUtils getInstance() {
        if (sOkHttpUtils == null) {
            synchronized (OkHttpUtils.class) {
                if (sOkHttpUtils == null) {
                    sOkHttpUtils = new OkHttpUtils();
                    dispatcher.setMaxRequests(1024);
                    dispatcher.setMaxRequestsPerHost(1024);
                }
            }
        }
        return sOkHttpUtils;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Call getExecuteCall(String url) {
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        return mOkHttpClient.newCall(request);
    }

    public String getUseablePort() {
        String ip;
        try {
            Call call = getExecuteCall(PORT1 + "/common/security/login.jsp");
            Response resp = call.execute();
            if (resp.isSuccessful()) {
                ip = PORT1;
                call.cancel();
                resp.close();
                return ip;
            } else {
                resp.close();
                call = getExecuteCall(PORT2 + "/common/security/login.jsp");
                try {
                    resp = call.execute();
                    if (resp.isSuccessful()) {
                        ip = PORT2;
                        call.cancel();
                        resp.close();
                        return ip;
                    } else {
                        call.cancel();
                        resp.close();
                        return Constants.STRING_FAILED;
                    }
                } catch (IOException e1) {
                    call.cancel();
                    resp.close();
                    throw new IOException();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            Call call = getExecuteCall(PORT2 + "/common/security/login.jsp");
            try {
                Response resp = call.execute();
                if (resp.isSuccessful()) {
                    ip = PORT2;
                    call.cancel();
                    resp.close();
                    return ip;
                } else {
                    call.cancel();
                    resp.close();
                    return Constants.STRING_FAILED;
                }
            } catch (IOException e2) {
                return Constants.STRING_ERROR;
            }
        }
    }

    public Call getInfoCall(String url, String session) {
        Request mRequest = new Request.Builder().url(url).addHeader("cookie", session).build();
        return mOkHttpClient.newCall(mRequest);
    }

    public Call getInfoCallRequestBody(String url, String session, RequestBody requestBody) {
        Request mRequest = new Request.Builder().url(url).addHeader("cookie", session).post(requestBody).build();
        return mOkHttpClient.newCall(mRequest);
    }

    public Map<String, String> login(String number, String password, String port) {
        Map<String, String> map = new HashMap<>();
        Map<String, String> loginMap = new LoginAction().login(number, password, port);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            System.out.println(TimeUtils.getTime() + " Login失败：" + loginMap.get("success"));

        } else {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
            map.put(Constants.STRING_SESSION, loginMap.get(Constants.STRING_SESSION));
            map.put(Constants.STRING_PORT, loginMap.get(Constants.STRING_PORT));
        }
        return map;
    }

    public String getSession(String url) {
        String s;
        Call call = getExecuteCall(url);
        Response resp = null;
        try {
            resp = call.execute();
            if (resp.isSuccessful()) {
                Response data = resp;
                Headers headers = data.headers();
                System.out.println(headers.toString());
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                s = session.substring(0, session.indexOf(";"));
            } else {
                s = Constants.STRING_FAILED;
                System.out.println(TimeUtils.getTime() + " 获取sessionid失败！");
            }
            call.cancel();
            resp.close();
        } catch (IOException e1) {
            if (resp != null)
                resp.close();
            s = Constants.STRING_ERROR;
            System.out.println(TimeUtils.getTime() + " 连接超时，获取seeionid失败！");
        }
        System.out.println(s);
        return s;
    }

    public String getSession(Response resp) {
        Response data = resp;
        Headers headers = data.headers();
        List<String> cookies = headers.values("Set-Cookie");
        String session = cookies.get(0);
        return session.substring(0, session.indexOf(";"));
    }

}
