package com.lntuplus.utils;

import com.lntuplus.action.AsyncAction;
import com.lntuplus.action.LoginAction;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {

    @Autowired
    private AsyncAction mAsyncAction;

    private static OkHttpUtils sOkHttpUtils;
    //    private static final String PORT1 = "http://s2.natfrp.com:7792/academic";
//    private static final String PORT2 = "http://s2.natfrp.com:7790/newacademic";
    private static final String PORT1 = "http://s1.natfrp.com:7792/academic";
    private static final String PORT2 = "http://s1.natfrp.com:7790/newacademic";
    private static final String PORT3 = "http://202.199.224.24:11189/academic";
    private static final String PORT4 = "http://202.199.224.24:11089/newacademic";
    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    private static Dispatcher dispatcher = new Dispatcher();

    private static OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)// 设置连接超时时间
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
        String port = Constants.STRING_ERROR;
        Future<String> portFuture1 = getPort(PORT1);
        Future<String> portFuture2 = getPort(PORT2);
        Future<String> portFuture3 = getPort(PORT3);
        Future<String> portFuture4 = getPort(PORT4);

        long start = System.currentTimeMillis();
        try {
            while (true) {
                if (portFuture1.isDone()) {
                    if (!portFuture1.get().equals(Constants.STRING_FAILED) && !portFuture1.get().equals(Constants.STRING_ERROR)) {
                        port = PORT1;
                        break;
                    }
                }
                if (portFuture2.isDone()) {
                    if (!portFuture2.get().equals(Constants.STRING_FAILED) && !portFuture2.get().equals(Constants.STRING_ERROR)) {
                        port = PORT2;
                        break;
                    }
                }
                if (portFuture3.isDone()) {
                    if (!portFuture3.get().equals(Constants.STRING_FAILED) && !portFuture3.get().equals(Constants.STRING_ERROR)) {
                        port = PORT3;
                        break;
                    }
                }
                if (portFuture4.isDone()) {
                    if (!portFuture4.get().equals(Constants.STRING_FAILED) && !portFuture4.get().equals(Constants.STRING_ERROR)) {
                        port = PORT4;
                        break;
                    }
                }
                Thread.sleep(500);
                if ((System.currentTimeMillis() - start) / 1000 > 10) {
                    logger.error("Port 轮询等待多线程返回超时，暂无可用端口！");
                    port = Constants.STRING_ERROR;
                    break;
                }
            }
        } catch (Exception e) {
            logger.info("Get ports failed");
        }
        return port;
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
            logger.debug(" Login失败：" + loginMap.get("success"));
        } else {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
            map.put(Constants.STRING_SESSION, loginMap.get(Constants.STRING_SESSION));
            map.put(Constants.STRING_PORT, loginMap.get(Constants.STRING_PORT));
        }
        return map;
    }

    public String getSession(String url) {
        logger.debug(url);
        String s;
        Call call = getExecuteCall(url);
        Response resp = null;
        try {
            resp = call.execute();
            if (resp.isSuccessful()) {
                Response data = resp;
                Headers headers = data.headers();
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                s = session.substring(0, session.indexOf(";"));
            } else {
                s = Constants.STRING_FAILED;
                logger.error("获取sessionid失败！");
            }
            call.cancel();
            resp.close();
        } catch (IOException e1) {
            if (resp != null)
                resp.close();
            s = Constants.STRING_ERROR;
            logger.error("连接超时，获取seeionid失败！");
        }
        return s;
    }

    public String getSession(Response resp) {
        Response data = resp;
        Headers headers = data.headers();
        List<String> cookies = headers.values("Set-Cookie");
        String session = cookies.get(0);
        return session.substring(0, session.indexOf(";"));
    }

    @Async
    public Future<String> getPort(String port) {
        String s = getSession(port + "/common/security/login.jsp");
        if (s.equals(Constants.STRING_FAILED)) {
            return new AsyncResult<>(Constants.STRING_FAILED);
        } else if (s.equals(Constants.STRING_ERROR)) {
            return new AsyncResult<>(Constants.STRING_ERROR);
        } else {
            return new AsyncResult<>(port);
        }
//        Call call = getExecuteCall(port + "/common/security/login.jsp");
//        try {
//            Response resp = call.execute();
//            if (resp.isSuccessful()) {
//                String result = getSession(resp);
//
//                resp.close();
//                return new AsyncResult<>(port);
//            } else {
//                return new AsyncResult<>(Constants.STRING_FAILED);
//            }
//        } catch (Exception e) {
//            return new AsyncResult<>(Constants.STRING_ERROR);
//        }
    }

}
