package com.lntuplus.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody.Builder;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Evaluate {
    private String session;
    private String number;
    private String password;
    private String ip = "http://202.199.224.24:11182/newacademic";
    private String sessionUrl = "/common/security/check1.jsp";
    private String evaluateUrl = "/eva/index/resultlist.jsdo?groupId=&moduleId=506";
    private String evaluateGetUrl = "/eva/index";
    private String evaluatePostUrl = "/eva/index/putresult.jsdo";
    private String loginUrl = "/j_acegi_security_check";
    private String checkUrl = "/frameset.jsp";
    private OkHttpClient client = new OkHttpClient();
    private OkHttpClient.Builder builder = this.client.newBuilder();
    private String[][] scoreData;
    private String[][] tableData;
    private String[][] examData;
    private String[] evaluateData;
    private String[] formData1 = new String[8];
    private String[] formName1 = new String[8];
    private String[][] formData2 = new String[12][5];
    private String[][] formName2 = new String[12][5];
    private String success = "false";

    public Evaluate(String number, String password, String session, String ip) throws IOException {
        this.ip = ip;
        this.number = number;
        this.password = password;
        this.session = session;
        addIP();
        String temp2 = getInfo(this.evaluateUrl);
        if (success.equals("newStuNoInfo")) {
            return;
        }
        getList(temp2);
    }

    private void addIP() {
        // TODO Auto-generated method stub
        sessionUrl = ip + sessionUrl;
        evaluateUrl = ip + evaluateUrl;
        evaluateGetUrl = ip + evaluateGetUrl;
        evaluatePostUrl = ip + evaluatePostUrl;
        checkUrl = ip + checkUrl;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public void getList(String Html) throws IOException {
        String state = "false";
        Document document = Jsoup.parse(Html);

        Elements trs = document.getElementsByClass("infolist");

        this.evaluateData = new String[trs.size()];
        for (int i = 0; i < trs.size(); i++) {
            final String temp = ((Element) trs.get(i)).attr("href").substring(1,
                    ((Element) trs.get(i)).attr("href").length());
            System.out.println(temp);
            state = Evaluate.this.post(temp);

            if (state.equals("success")) {
                success = "success";
                System.out.println("评课成功回调！");
            } else {
                success = "evaluateError";
                return;
            }
        }
    }

    public String post(String url) throws IOException {
        String data = "false";
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(this.evaluateGetUrl + url).addHeader("cookie", this.session)
                .build();
        System.out.println(this.evaluateGetUrl + url);
        Call call = client.newCall(request);
        data = null;
        Response resp = call.execute();
        if (resp.isSuccessful()) {
            data = resp.body().string();
            Document document = Jsoup.parse(data);
            Elements inputs = document.getElementsByTag("input");
            Element table = document.getElementsByTag("table").get(3);
            Elements trs = table.getElementsByTag("tr");
            System.out.println(trs.size());
            for (int i = 0; i < 8; i++) {
                Evaluate.this.formData1[i] = (inputs.get(i)).val();
                Evaluate.this.formName1[i] = (inputs.get(i)).attr("name");
                System.out.println(Evaluate.this.formName1[i] + " " + Evaluate.this.formData1[i]);
            }
            int x = 0;
            int y = 0;
            for (int i = 1; i < 13; i++) {
                for (int j = 0; j < 5; j++) {
                    System.out.println((((trs.get(i)).getElementsByTag("td").get(2)).getElementsByTag("input").get(j))
                            .attr("name") + " "
                            + ((((Element) trs.get(i)).getElementsByTag("td").get(2)).getElementsByTag("input").get(j))
                            .val());
                    Evaluate.this.formName2[(i
                            - 1)][j] = ((Element) ((Element) ((Element) trs.get(i)).getElementsByTag("td").get(2))
                            .getElementsByTag("input").get(j)).attr("name");
                    Evaluate.this.formData2[(i
                            - 1)][j] = ((Element) ((Element) ((Element) trs.get(i)).getElementsByTag("td").get(2))
                            .getElementsByTag("input").get(j)).val();
                }
            }
            Builder formBody = new Builder();
            for (int i = 0; i < 8; i++) {
                formBody.add(Evaluate.this.formName1[i], Evaluate.this.formData1[i]);
            }
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 5; j++) {
                    formBody.add(Evaluate.this.formName2[i][j], Evaluate.this.formData2[i][j]);
                }
            }
            RequestBody requestBody = formBody.build();
            request = new Request.Builder().url(Evaluate.this.evaluatePostUrl)
                    .addHeader("cookie", Evaluate.this.session).post(requestBody).build();
            call = client.newCall(request);
            data = null;
            resp = call.execute();
            if (resp.isSuccessful()) {
                data = "success";
                System.out.println("评课成功!");
            }
            resp.close();
            return data;
        }
        return data;
    }

    public String getSession() {
        return this.session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[][] getScoreData() {
        return this.scoreData;
    }

    public void setScoreData(String[][] scoreData) {
        this.scoreData = scoreData;
    }

    public String[][] getTableData() {
        return this.tableData;
    }

    public void setTableData(String[][] tableData) {
        this.tableData = tableData;
    }

    public String[][] getExamData() {
        return this.examData;
    }

    public void setExamData(String[][] examData) {
        this.examData = examData;
    }

    private String getInfo(String url) {
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(3, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(3, TimeUnit.SECONDS)// 设置读取超时时间
                .build();
        Request request = new Request.Builder().url(url).addHeader("cookie", this.session).build();
        System.out.println(url);
        Call call = client.newCall(request);
        String data = null;
        try {
            Response resp = call.execute();
            if (resp.isSuccessful()) {
                data = resp.body().string();
            }
            resp.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            success = "newStuNoInfo";
            return data;
        }
        return data;
    }

    public String loginPost() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new Builder().add("j_username", this.number).add("j_password", this.password)
                .build();

        Request request = new Request.Builder().url(this.loginUrl).addHeader("cookie", this.session).post(formBody)
                .build();

        final Call call = client.newCall(request);
        String state = null;
        String data = null;
        Response resp = call.execute();
        if (resp.isSuccessful()) {
            data = resp.body().string();
        }
        resp.close();
        return data;

    }

    public String getSessionId() {
        OkHttpClient client = new OkHttpClient();
        String jsessionid = null;
        Request request = new Request.Builder().url(this.sessionUrl).build();

        Call call = client.newCall(request);
        try {
            Response resp = call.execute();
            if (resp.isSuccessful()) {
                Response data = resp;
                Headers headers = data.headers();
                List<String> cookies = headers.values("Set-Cookie");
                String session = (String) cookies.get(0);
                jsessionid = session.substring(0, session.indexOf(";"));
            }
            resp.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return jsessionid;
    }

    public class info {
        String number;
        String password;
        String jsessionid;

        public info() {
        }

        public String getNumber() {
            return this.number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getJsessionid() {
            return this.jsessionid;
        }

        public void setJsessionid(String jsessionid) {
            this.jsessionid = jsessionid;
        }
    }

    private String transWeek(String week) {
        String[] Week = {"日", "一", "二", "三", "四", "五", "六"};
        for (int i = 0; i < 7; i++) {
            if (week.equals(Week[i])) {
                return String.valueOf(i);
            }
        }
        return "日";
    }

    private int getDays(String endDate, String startDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(startDate);
        Date date2 = sdf.parse(endDate);
        long days = (date2.getTime() - date1.getTime()) / 86400000L;
        long yushu = (date2.getTime() - date1.getTime()) % 86400000L;

        return (int) days;
    }
}
