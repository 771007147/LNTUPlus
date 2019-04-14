package com.lntuplus.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.lntuplus.model.ExamModel;
import com.lntuplus.utils.ComBean;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
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

public class GetMessages {
    private String session;
    private String number;
    private String password;
    private String ip = "http://202.199.224.24:11182/newacademic";
    //	private String ip = "http://202.199.224.24:11082/academic";
    private String ip1 = "http://202.199.224.24:11182/newacademic";
    private String ip2 = "http://202.199.224.24:11082/academic";
    private String sessionUrl = "/common/security/check1.jsp";
    private String loginUrl = "/j_acegi_security_check";
    private String scoreUrl = "/student/queryscore/queryscore.jsdo?groupId=&moduleId=2021";
    private String tableUrl = "/student/currcourse/currcourse.jsdo?groupId=&year=38&term=2";
    private String examUrl = "/student/exam/index.jsdo?groupId=&moduleId=2030";
    private String stuInfoUrl = ip + "/student/studentinfo/studentinfo.jsdo?groupId=&moduleId=2060";
    private String checkUrl = ip + "/frameset.jsp";
    private OkHttpClient client = new OkHttpClient();
    private OkHttpClient.Builder builder = client.newBuilder();
    private String[][] scoreData;
    private String[][] tableData;
    private String[][] examData;
    private String[] infoData;
    private List scoreList;
    private List tableList;
    private List examList;
    private Map<String, String> infoMap;
    private String imgSrc;
    private String success = "true";
    private String gpa = "0.000";
    private int countExam = 0;
    private String forumShow = "1";
    private List lineHeight = new ArrayList();

    private OkHttpUtils mOkHttpUtils;

    public GetMessages(String number, String password) {
        mOkHttpUtils = OkHttpUtils.getInstance();
        this.number = number;
        this.password = password;
        ip = mOkHttpUtils.getUseablePort();
        if (ip.equals("session")) {
            success = "session";
            return;
        }
        jointUrl();
        session = mOkHttpUtils.getSession(sessionUrl);
        if (success.equals("session")) {
            return;
        }
        System.out.println(session);
        String loginReturn = loginPost();
        if (loginReturn == null) {
            success = "connect";
            System.out.println("connectError");
            return;
        }
        if (!passwordCheck(loginReturn).booleanValue()) {
            success = "password";
            System.out.println("passwordError");
            return;
        }
        String checkReturn = getInfo(checkUrl);
        String examReturn = null;
        String scoreReturn = null;
        String infoReturn = null;
        String tableReturn = null;
        if (success.equals("true")) {
            infoReturn = getInfo(stuInfoUrl);
        }
        if (success.equals("true")) {
            examReturn = getInfo(examUrl);
        }
        if (success.equals("true")) {
            scoreReturn = getInfo(scoreUrl);

        }
        if (success.equals("true")) {
            tableReturn = getInfo(tableUrl);
            tableReturn = getAllTable(tableReturn);
        }
        if (success.equals("true")) {
            parseStuInfo(infoReturn);
            parseExam(examReturn);
            parseScore(scoreReturn);
            parseTable(tableReturn);
        }
    }

    private void jointUrl() {
        // TODO Auto-generated method stub
        sessionUrl = ip + "/common/security/check1.jsp";
        loginUrl = ip + "/j_acegi_security_check";
        scoreUrl = ip + "/student/queryscore/queryscore.jsdo?groupId=&moduleId=2021";
        tableUrl = ip + "/student/currcourse/currcourse.jsdo?groupId=&year=38&term=2";
        examUrl = ip + "/student/exam/index.jsdo?groupId=&moduleId=2030";
        stuInfoUrl = ip + "/student/studentinfo/studentinfo.jsdo?groupId=&moduleId=2060";
        checkUrl = ip + "/frameset.jsp";
    }

    private String getAllTable(String html) {
        Document document = Jsoup.parse(html);
        String input = document.getElementsByTag("input").get(0).attr("onClick");
        String urlEnd = input.substring(input.indexOf("/"), input.indexOf(",") - 1);
        String url = ip + "/student/currcourse" + urlEnd;
//		System.out.println(url);
        String resp = getInfo(url);
        return resp;
    }

    public String getForumShow() {
        // TODO Auto-generated method stub
        ComBean cBean = new ComBean();
//		System.out.println(number);
        int flag = cBean.getCount("select * from forumShow where number='" + number + "';");
//		System.out.println(flag);
        if (flag > 0) {
            forumShow = "0";
        } else {
            forumShow = "1";
        }

        return forumShow;
    }

//    public boolean getInfo(String number, String password) throws IOException {
//        this.number = number;
//        this.password = password;
//        session = getSessionId();
//        if (success.equals("session")) {
//            return false;
//        }
//        System.out.println(session);
//        String loginReturn = loginPost();
//        if (loginReturn == null) {
//            success = "connect";
//            System.out.println("connectError");
//            return false;
//        }
//        if (!passwordCheck(loginReturn).booleanValue()) {
//            success = "password";
//            System.out.println("passwordError");
//            return false;
//        }
//        String checkReturn = getInfo(checkUrl);
//        String examReturn = null;
//        String scoreReturn = null;
//        String infoReturn = null;
//        String tableReturn = null;
//        if (success.equals("true")) {
//            infoReturn = getInfo(stuInfoUrl);
//        }
//        if (success.equals("true")) {
//            examReturn = getInfo(examUrl);
//        }
//        if (success.equals("true")) {
//            scoreReturn = getInfo(scoreUrl);
//        }
//        if (success.equals("true")) {
//            tableReturn = getInfo(tableUrl);
//        }
//        if (success.equals("true")) {
//            parseStuInfo(infoReturn);
//            parseExam(examReturn);
//            parseScore(scoreReturn);
//            parseTable(tableReturn);
//        }
//        return true;
//    }
//
//    public void getStuInfo() {
//        this.number = number;
//        this.password = password;
//        session = getSessionId();
//        if (success.equals("connect")) {
//            return;
//        }
//        System.out.println(session);
//        String loginReturn = loginPost();
//        if (loginReturn == null) {
//            success = "connect";
//            System.out.println("connectError");
//            return;
//        }
//        if (!passwordCheck(loginReturn).booleanValue()) {
//            success = "password";
//            System.out.println("passwordError");
//            return;
//        }
//        String checkReturn = getInfo(checkUrl);
//        String examReturn = null;
//        String scoreReturn = null;
//        String infoReturn = null;
//        String tableReturn = null;
//        if (success.equals("true")) {
//            infoReturn = getInfo(stuInfoUrl);
//        }
//        if (success.equals("true")) {
//            examReturn = getInfo(examUrl);
//        }
//        if (success.equals("true")) {
//            scoreReturn = getInfo(scoreUrl);
//        }
//        if (success.equals("true")) {
//            tableReturn = getInfo(tableUrl);
//        }
//        if (success.equals("true")) {
//            parseStuInfo(infoReturn);
//            parseExam(examReturn);
//            parseScore(scoreReturn);
//            parseTable(tableReturn);
//        }
//    }

    public int getCountExam() {
        return countExam;
    }

    public void setCountExam(int countExam) {
        this.countExam = countExam;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String[] getInfoData() {
        return infoData;
    }

    public void setInfoData(String[] infoData) {
        this.infoData = infoData;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[][] getScoreData() {
        return scoreData;
    }

    public void setScoreData(String[][] scoreData) {
        this.scoreData = scoreData;
    }

    public String[][] getTableData() {
        return tableData;
    }

    public void setTableData(String[][] tableData) {
        this.tableData = tableData;
    }

    public String[][] getExamData() {
        return examData;
    }

    public void setExamData(String[][] examData) {
        this.examData = examData;
    }

    public List getScoreList() {
        return scoreList;
    }

    public void setScoreList(List scoreList) {
        this.scoreList = scoreList;
    }

    public List getTableList() {
        return tableList;
    }

    public void setTableList(List tableList) {
        this.tableList = tableList;
    }

    public List getExamList() {
        return examList;
    }

    public void setExamList(List examList) {
        this.examList = examList;
    }

    public Map<String, String> getInfoMap() {
        return infoMap;
    }

    public void setInfoMap(Map<String, String> infoMap) {
        this.infoMap = infoMap;
    }

    protected String getInfo(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("cookie", session).build();
        Call call = client.newCall(request);
        String state = null;
        String data = null;
        try {
            Response resp = call.execute();
            int code = resp.code();
            System.out.println(code);
            if (code == 500) {
                if (url.substring(url.length() - 25, url.length() - 16).equals("chajuandy")) {
                    return "500";
                }
                String respBody = resp.body().string();

                Document document = Jsoup.parse(respBody);
                Element element = document.getElementById("content_margin");
//				if(element!=null&&element.text().substring(0, 2).equals("您有")){
//					success = "newStuNoInfo";
//					System.out.println("该新生尚未填写必填信息:"+number);
//					return data;
//				}
//                Evaluate evaluate = new Evaluate(number, password, session, ip);
//
//                if (evaluate.getSuccess().equals("success")) {
//                    return getInfo(scoreUrl);
//                } else if (evaluate.getSuccess().equals("newStuNoInfo")) {
//                    this.success = "newStuNoInfo";
//                    System.out.println("该新生尚未填写必填信息:" + number);
//                    return data;
//                } else {
//                    System.out.println("评课失败，学号：" + number);
//                }
//                resp.close();
            }
            if (resp.isSuccessful()) {
                data = resp.body().string();
            } else {
                success = "connect";
            }
            resp.close();
        } catch (IOException e1) {
            success = "connect";
            System.out.println("School server connect error");
        }
        return data;
    }

    public String loginPost() {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new Builder().add("j_username", number).add("j_password", password).build();

        Request request = new Request.Builder().url(loginUrl).addHeader("cookie", session).post(formBody).build();

        Call call = client.newCall(request);
        String data = null;
        Response resp = null;
        try {
            resp = call.execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (resp.isSuccessful()) {
            try {
                data = resp.body().string();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        resp.close();
        return data;
    }

    public void parseStuInfo(String Html) {
        Document document = Jsoup.parse(Html);
        Element element = document.getElementsByClass("infolist_vt").get(0);
        imgSrc = (document.getElementsByTag("img").get(0)).attr("src");
//	    System.out.println(imgSrc);
        infoMap = new HashMap<String, String>();
        Elements tds = element.getElementsByTag("td");
        infoData = new String[26];
        infoData[0] = tds.get(0).text();
        infoData[1] = tds.get(1).text();
        infoData[2] = tds.get(3).text();
        infoData[3] = tds.get(4).text();
        infoData[4] = tds.get(6).text();
        infoData[5] = tds.get(7).text();
        infoData[6] = tds.get(8).text();
        infoData[7] = tds.get(9).text();
        infoData[8] = tds.get(10).text();
        infoData[9] = tds.get(11).text();
        infoData[10] = tds.get(12).text();
        infoData[11] = tds.get(13).text();
        infoData[12] = tds.get(14).text();
        infoData[13] = tds.get(15).text();
        infoData[14] = tds.get(16).text();
        infoData[15] = tds.get(17).text();
        infoData[16] = tds.get(18).text();
        infoData[17] = tds.get(19).text();
        infoData[18] = tds.get(20).text();
        infoData[19] = tds.get(21).text();
        infoData[20] = tds.get(23).text();
        infoData[21] = tds.get(25).text();
        infoData[22] = tds.get(27).text();
        infoData[23] = tds.get(32).text();
        infoData[24] = imgSrc;
        infoData[25] = password;
        infoMap.put("number", (tds.get(0)).text());
        infoMap.put("nationality", (tds.get(1)).text());
        infoMap.put("name", (tds.get(3)).text());
        infoMap.put("place", (tds.get(4)).text());
        infoMap.put("birth", (tds.get(6)).text());
        infoMap.put("typeOfID", (tds.get(7)).text());
        infoMap.put("politicalStatus", (tds.get(8)).text());
        infoMap.put("card", (tds.get(9)).text());
        infoMap.put("vehicleInterval", (tds.get(10)).text());
        infoMap.put("sex", (tds.get(11)).text());
        infoMap.put("nation", (tds.get(12)).text());
        infoMap.put("academic", (tds.get(13)).text());
        infoMap.put("professional", (tds.get(14)).text());
        infoMap.put("class", (tds.get(15)).text());
        infoMap.put("stuType", (tds.get(16)).text());
        infoMap.put("examArea", (tds.get(17)).text());
        infoMap.put("score", (tds.get(18)).text());
        infoMap.put("registrationNumber", (tds.get(19)).text());
        infoMap.put("school", (tds.get(20)).text());
        infoMap.put("languageType", (tds.get(21)).text());
        infoMap.put("comeDate", (tds.get(23)).text());
        infoMap.put("goDate", (tds.get(25)).text());
        infoMap.put("address", (tds.get(27)).text());
        infoMap.put("origin", (tds.get(32)).text());
        infoMap.put("imgSrc", imgSrc);
        infoMap.put("password", password);
        for (String k : infoMap.keySet()) {
            System.out.println(k + ":" + infoMap.get(k));
        }
    }

    public void parseScore(String Html) {
        Document document = Jsoup.parse(Html);

        Element gpaTable = document.getElementsByClass("broken_tab").get(0);
        Element gpaTr = gpaTable.getElementsByTag("tr").get(0);
        String gpaTd = (gpaTr.getElementsByTag("td").get(2)).text();
        if (gpaTd.indexOf("，") > 0) {
            gpa = gpaTd.substring(10, gpaTd.indexOf("，"));
            System.out.println("GPA:" + gpa);
        }
        Element element = document.getElementsByClass("infolist_tab").get(0);

        Elements trs = element.getElementsByTag("tr");
        if (trs.size() == 1) {
            return;
        }

        Elements scripts = element.getElementsByTag("script");
        int from = 0;
        for (int i = scripts.size() - 1; i >= 0; i--) {
            String n = (scripts.get(i)).toString().substring((scripts.get(i)).toString().indexOf("[") + 1,
                    (scripts.get(i)).toString().indexOf("]"));
            if (n.equals("0")) {
                from = i + 1;
                break;
            }
        }
        scoreData = new String[trs.size() - from][12];
        Date day = new Date();
        SimpleDateFormat tS = new SimpleDateFormat("yyyy");
        SimpleDateFormat tE = new SimpleDateFormat("yyyy-mm-dd");
        int termStart = Integer.valueOf(tS.format(day)).intValue();
        int termEnd = 1997;
        Queue<Integer> yearList = new LinkedList();
        Queue<String[]> delList = new LinkedList();
        for (int i = from; i < trs.size(); i++) {
            Element e = trs.get(i);
            Elements tds = e.getElementsByTag("td");
            scoreData[(i - from)][0] = (tds.get(1)).text(); // 课程名称
            scoreData[(i - from)][1] = (tds.get(3)).text(); // 总评（分数）
            scoreData[(i - from)][2] = (tds.get(4)).text(); // 学分
            scoreData[(i - from)][3] = (tds.get(5)).text(); // 考核方式
            scoreData[(i - from)][4] = (tds.get(6)).text(); // 选课属性
            scoreData[(i - from)][5] = (tds.get(8)).text(); // 考试性质
            scoreData[(i - from)][6] = (tds.get(9)).text(); // 学年学期
            scoreData[(i - from)][8] = (tds.get(0)).text(); // 课程号
            scoreData[(i - from)][9] = (tds.get(2)).text(); // 课序号
            scoreData[(i - from)][10] = (tds.get(7)).text(); // 备注
            scoreData[(i - from)][11] = (tds.get(10)).text(); // 是否缓考

            if (!tds.get(11).getElementsByTag("a").attr("href").toString().equals(""))
                scoreData[(i - from)][7] = (ip + "/student/queryscore/"
                        + tds.get(11).getElementsByTag("a").attr("href"));
            else {
                scoreData[(i - from)][7] = "null";
            }
            if (Integer.valueOf(scoreData[(i - from)][6].substring(0, 3)).intValue() < termStart) {
                termStart = Integer.valueOf(scoreData[(i - from)][6].substring(0, 3)).intValue();
            }
        }
        for (int j = 0; j < scoreData.length - 1; j++) {
            for (int k = 0; k < scoreData.length - 1 - j; k++) {
                if (Integer.valueOf(scoreData[k][6].substring(0, 4)).intValue() < Integer
                        .valueOf(scoreData[(k + 1)][6].substring(0, 4)).intValue()) {
                    String[] temp = scoreData[k];
                    scoreData[k] = scoreData[(k + 1)];
                    scoreData[(k + 1)] = temp;
                }
            }
        }
        int termYear = 0;
        for (int i = 0; i < scoreData.length; i++) {
            if (Integer.valueOf(scoreData[i][6].substring(0, 4)).intValue() != termYear) {
                yearList.offer(Integer.valueOf(scoreData[i][6].substring(0, 4)));
                termYear = Integer.valueOf(scoreData[i][6].substring(0, 4)).intValue();
            }
        }
        int size = yearList.size();
        int[] start = new int[size];
        int[] end = new int[size];

        start[0] = 0;
        Queue<Integer> list = yearList;
        int nowYear = ((Integer) list.poll()).intValue();
        int se = 0;
        for (int j = 0; j < scoreData.length - 1; j++) {
            if ((Integer.valueOf(scoreData[j][6].substring(0, 4)).intValue() == nowYear)
                    && (Integer.valueOf(scoreData[(j + 1)][6].substring(0, 4)).intValue() != nowYear)) {
                end[se] = j;
                start[(se + 1)] = (j + 1);
                se++;
                nowYear = ((Integer) yearList.poll()).intValue();
            }
        }
        end[(size - 1)] = (scoreData.length - 1);
        if (size > 1) {
            for (int i = 0; i < size; i++) {
                int s = start[i];
                int e = end[i];
                for (int j = s; j < e; j++) {
                    if (scoreData[j][6].substring(4, 5).equals("春")) {
                        while ((e > j) && (scoreData[e][6].substring(4, 5).equals("春"))) {
                            e--;
                        }
                        if (e > j) {
                            String[] tt = scoreData[j];
                            scoreData[j] = scoreData[e];
                            scoreData[e] = tt;
                            e--;
                        }
                    }
                }
            }
        }
        int ssize = 0;
        if (size == 1) {
            start = new int[1];
            end = new int[1];
            start[0] = 0;
            end[0] = (scoreData.length - 1);
            ssize = 1;
        } else {
            ssize = 1;
            String tempYear = scoreData[0][6];
            for (int i = 0; i < scoreData.length; i++) {
                if (!scoreData[i][6].equals(tempYear)) {
                    tempYear = scoreData[i][6];
                    ssize++;
                }
            }
            start = new int[ssize];
            end = new int[ssize];
        }
        start[0] = 0;
        se = 0;
        for (int j = 0; j < scoreData.length - 1; j++) {
            if (!scoreData[j][6].equals(scoreData[(j + 1)][6])) {
                end[se] = j;
                start[(se + 1)] = (j + 1);
                se++;
            }
        }
        end[(ssize - 1)] = (scoreData.length - 1);
        if (size > 1) {
            for (int i = 0; i < ssize; i++) {
                int s = start[i];
                int e = end[i];
                for (int j = s; j < e; j++) {
                    for (int k = s; k < e - j + s; k++) {
                        double a = 0;
                        double b = 0;
                        switch (scoreData[k][1]) {
                            case "":
                                a = 0;
                                break;
                            case "优秀":
                                a = 90;
                                break;
                            case "优":
                                a = 90;
                                break;
                            case "良":
                                a = 80;
                                break;
                            case "中":
                                a = 70;
                                break;
                            case "及格":
                                a = 60;
                                break;
                            case "合格":
                                a = 60;
                                break;
                            case "不及格":
                                a = 59;
                                break;
                            case "不合格":
                                a = 59;
                                break;
                            case "实践成绩未提交":
                                a = 0;
                                break;
                            default:
                                a = Double.valueOf(scoreData[k][1]);
                                ;
                        }
                        switch (scoreData[(k + 1)][1]) {
                            case "":
                                b = 0;
                                break;
                            case "优秀":
                                b = 90;
                                break;
                            case "优":
                                b = 90;
                                break;
                            case "良":
                                b = 80;
                                break;
                            case "中":
                                b = 70;
                                break;
                            case "及格":
                                b = 60;
                                break;
                            case "合格":
                                b = 60;
                                break;
                            case "不及格":
                                b = 59;
                                break;
                            case "不合格":
                                b = 59;
                                break;
                            case "实践成绩未提交":
                                b = 0;
                                break;
                            default:
                                b = Double.valueOf(scoreData[(k + 1)][1]);
                                ;
                        }
                        if (a < b) {
                            String[] tt = scoreData[k];
                            scoreData[k] = scoreData[(k + 1)];
                            scoreData[(k + 1)] = tt;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < scoreData.length; i++) {
            System.out.println(scoreData[i][0] + " " + scoreData[i][1] + " " + scoreData[i][2] + " " + scoreData[i][3]
                    + " " + scoreData[i][4] + " " + scoreData[i][5] + " " + scoreData[i][6] + " " + scoreData[i][7]);
        }
        scoreList = new ArrayList();
        for (int i = 0; i < scoreData.length; ) {
            String year = scoreData[i][6];
            List<Map> listS = new ArrayList();
            while (i < scoreData.length && scoreData[i][6].equals(year)) {
                Map mapItem = new HashMap();
                mapItem.put("course", scoreData[i][0]);
                mapItem.put("score", scoreData[i][1]);
                mapItem.put("credit", scoreData[i][2]);
                mapItem.put("testType", scoreData[i][3]);
                mapItem.put("properties", scoreData[i][4]);
                mapItem.put("mode", scoreData[i][5]);
                mapItem.put("term", scoreData[i][6]);
                mapItem.put("detailed", scoreData[i][7]);
                mapItem.put("cNumber", scoreData[i][8]);
                mapItem.put("cNo", scoreData[i][9]);
                mapItem.put("remark", scoreData[i][10]);
                mapItem.put("delayed", scoreData[i][11]);
                if (!scoreData[i][7].equals("null")) {
                    String detailed = getInfo(scoreData[i][7]);
                    if (detailed != null)
                        mapItem.put("detailedAll", parseDetailed(detailed));
                }
                listS.add(mapItem);
                i++;
            }
            Map map = new HashMap();
            map.put("year", year);
            map.put("list", listS);
            scoreList.add(map);
        }
//    System.out.println(scoreList);
    }

    public void parseExam(String Html) {
        Document document = Jsoup.parse(Html);
        Element element = document.getElementsByClass("infolist_tab").get(0);
        Elements trs = element.getElementsByTag("tr");
        examData = new String[trs.size() - 1][6];
        examList = new ArrayList<Map>();
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String nowDay = df.format(day);
        for (int i = 1; i < trs.size(); i++) {
            ExamModel examModel = new ExamModel();
            Element e = trs.get(i);
            Elements tds = e.getElementsByTag("td");
            examModel.setCourse(tds.get(0).text());
            examModel.setDate(tds.get(1).text().substring(0, tds.get(1).text().indexOf(" ")));
            examModel.setTime(tds.get(1).text().substring((tds.get(1)).text().indexOf(" ") + 1, (tds.get(1)).text().length() - 1));
            examModel.setAddress(tds.get(2).text());
        }

        for (int j = 0; j < examData.length - 1; j++) {
            for (int k = 0; k < examData.length - 1 - j; k++) {
                if (Integer.valueOf(examData[k][4]) == Integer.valueOf(examData[(k + 1)][4])) {
                    if (Integer.valueOf(examData[k][2].substring(0, 2)).intValue() > Integer
                            .valueOf(examData[(k + 1)][2].substring(0, 2)).intValue()) {
                        String[] temp = examData[k];
                        examData[k] = examData[(k + 1)];
                        examData[(k + 1)] = temp;
                    }
                } else if (Integer.valueOf(examData[k][4]).intValue() < Integer.valueOf(examData[(k + 1)][4])
                        .intValue()) {
                    String[] temp = examData[k];
                    examData[k] = examData[(k + 1)];
                    examData[(k + 1)] = temp;
                }
            }
        }
        for (int j = 0; j < examData.length - 1; j++) {
            for (int k = 0; k < examData.length - 1 - j; k++) {
                if ((Integer.valueOf(examData[k][4]).intValue() > Integer.valueOf(examData[(k + 1)][4]).intValue())
                        && (Integer.valueOf(examData[k][4]).intValue() >= 0)
                        && (Integer.valueOf(examData[(k + 1)][4]).intValue() >= 0)) {
                    String[] temp = examData[k];
                    examData[k] = examData[(k + 1)];
                    examData[(k + 1)] = temp;
                }
            }
        }
        for (int i = 0; i < examData.length; i++) {
            if (Integer.valueOf(examData[i][4]).intValue() == 0) {
                examData[i][4] = "今天考试";
                countExam += 1;
            } else if (Integer.valueOf(examData[i][4]).intValue() < 0) {
                examData[i][4] = "考试完成";
            } else {
                examData[i][4] = ("还有" + examData[i][4] + "天");
                countExam += 1;
            }

            System.out.println(examData[i][0] + " " + examData[i][1] + " " + examData[i][2] + " " + examData[i][3] + " "
                    + examData[i][4]);
        }
        for (int i = 0; i < examData.length; i++) {
            Map<String, String> map = new HashMap();
            map.put("course", examData[i][0]);
            map.put("date", examData[i][1]);
            map.put("time", examData[i][2]);
            map.put("address", examData[i][3]);
            map.put("days", examData[i][4]);
            map.put("week", examData[i][5]);
            examList.add(map);
        }
    }

    public boolean parseExamList(String Html) {
        return false;
    }

    public void parseTable(String Html) {
        Document document = Jsoup.parse(Html);
        Elements trs = document.getElementsByClass("infolist_hr_common");
        tableList = new ArrayList<List>();
        for (int i = 0; i < trs.size(); i++) {
            List<List> tds = new ArrayList();
            Element tdOne = trs.get(i);
            Elements tdAll = tdOne.getElementsByTag("td");
            for (int j = 0; j < tdAll.size(); j++) {
                List<Map<String, String>> dayOne = new ArrayList();
                String table = tdAll.get(j).text().toString();
                Map<String, String> map = new HashMap();
                if (table.equals("")) {
                    tds.add(dayOne);
                    continue;
                } else {
                    int count = countTable(table);
                    String tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("course", tt);
                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("time", tt);
                    if (tt.indexOf("-") == -1) {
                        map.put("start", tt);
                        map.put("end", tt);
                        map.put("singleDouble", "0");
                    } else if (tt.indexOf("单") != -1) {
                        map.put("singleDouble", "1");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length() - 1));
                    } else if (tt.indexOf("双") != -1) {
                        map.put("singleDouble", "2");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length() - 1));
                    } else {
                        map.put("singleDouble", "0");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length()));
                    }

                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("teacher", tt);
                    if (count == 1) {
                        tt = table.substring(0, table.length());
                        map.put("address", tt);
                        dayOne.add(map);
                        tds.add(dayOne);
                        continue;
                    } else {
                        tt = table.substring(0, table.indexOf(" "));
                        table = table.substring(table.indexOf(" ") + 1, table.length());
                        map.put("address", tt);
                        dayOne.add(map);
                    }

                    map = new HashMap();
                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("course", tt);
                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("time", tt);
                    if (tt.indexOf("-") == -1) {
                        map.put("start", tt);
                        map.put("end", tt);
                        map.put("singleDouble", "0");
                    } else if (tt.indexOf("单") != -1) {
                        map.put("singleDouble", "1");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length() - 1));
                    } else if (tt.indexOf("双") != -1) {
                        map.put("singleDouble", "2");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length() - 1));
                    } else {
                        map.put("singleDouble", "0");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length()));
                    }

                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("teacher", tt);
                    if (count == 2) {
                        tt = table.substring(0, table.length());
                        map.put("address", tt);
                        dayOne.add(map);
                        tds.add(dayOne);
                        continue;
                    } else {
                        tt = table.substring(0, table.indexOf(" "));
                        table = table.substring(table.indexOf(" ") + 1, table.length());
                        map.put("address", tt);
                        dayOne.add(map);
                    }
                    map = new HashMap();
                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("course", tt);
                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("time", tt);
                    if (tt.indexOf("-") == -1) {
                        map.put("start", tt);
                        map.put("end", tt);
                        map.put("singleDouble", "0");
                    } else if (tt.indexOf("单") != -1) {
                        map.put("singleDouble", "1");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length() - 1));
                    } else if (tt.indexOf("双") != -1) {
                        map.put("singleDouble", "2");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length() - 1));
                    } else {
                        map.put("singleDouble", "0");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length()));
                    }

                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("teacher", tt);
                    if (count == 3) {
                        tt = table.substring(0, table.length());
                        map.put("address", tt);
                        dayOne.add(map);
                        tds.add(dayOne);
                        continue;
                    } else {
                        tt = table.substring(0, table.indexOf(" "));
                        table = table.substring(table.indexOf(" ") + 1, table.length());
                        map.put("address", tt);
                        dayOne.add(map);
                    }
                    map = new HashMap();
                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("course", tt);
                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("time", tt);
                    if (tt.indexOf("-") == -1) {
                        map.put("start", tt);
                        map.put("end", tt);
                        map.put("singleDouble", "0");
                    } else if (tt.indexOf("单") != -1) {
                        map.put("singleDouble", "1");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length() - 1));
                    } else if (tt.indexOf("双") != -1) {
                        map.put("singleDouble", "2");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length() - 1));
                    } else {
                        map.put("singleDouble", "0");
                        map.put("start", tt.substring(0, tt.indexOf("-")));
                        map.put("end", tt.substring(tt.indexOf("-") + 1, tt.length()));
                    }

                    tt = table.substring(0, table.indexOf(" "));
                    table = table.substring(table.indexOf(" ") + 1, table.length());
                    map.put("teacher", tt);
                    if (count == 4) {
                        tt = table.substring(0, table.length());
                        map.put("address", tt);
                        dayOne.add(map);
                        tds.add(dayOne);
                        continue;
                    } else {
                        tt = table.substring(0, table.indexOf(" "));
                        table = table.substring(table.indexOf(" ") + 1, table.length());
                        map.put("address", tt);
                        dayOne.add(map);
                    }
                }
            }
            tableList.add(tds);
            i++;
        }

    }

    private Map parseDetailed(String html) {
        if (html.equals("500")) {
            return new HashMap<>();
        }
        Map<String, String> map = new HashMap();

        Document document = Jsoup.parse(html);
        Element element = document.getElementsByTag("CENTER").get(0);
        if (element.getElementsByTag("table").toString().equals("")) {
            return map;
        }
        Element table = element.getElementsByTag("table").get(0);
        Elements trs = table.getElementsByTag("tr");
//		System.out.println(trs.toString());
        String s = trs.get(0).text().toString();
        if (s.indexOf("教室编号") >= 0 && s.indexOf("座位号") >= 0) {
            map.put("examRoom", s.substring(s.indexOf("编号：") + 3, s.indexOf(" 座位")));
            map.put("seatNum", s.substring(s.indexOf("座位号：") + 4, s.indexOf("）")));
        }
        map.put("examNature", s.substring(s.indexOf("考试性质：") + 5, s.length()));
        Elements tds = trs.get(1).getElementsByTag("td");
        map.put("courseNo", tds.get(1).text().toString());
        map.put("courseName", tds.get(3).text().toString());
        map.put("openUnit", tds.get(5).text().toString());
        map.put("teacher", tds.get(7).text().toString());
        if (trs.get(4).text().toString().indexOf("成绩信息：") >= 0) {
            map.put("scoreInfo", trs.get(4).text().toString().substring(
                    trs.get(4).text().toString().indexOf("成绩信息：") + 5, trs.get(4).text().toString().length()));
        }
        tds = trs.get(6).getElementsByTag("td");
        map.put("normalScore", tds.get(0).text().toString());
        map.put("midScore", tds.get(1).text().toString());
        map.put("endScore", tds.get(2).text().toString());
        map.put("finalScore", tds.get(3).text().toString());

//		for (String in : map.keySet()) {
//			String str = map.get(in);// 得到每个key多对用value的值
//			System.out.println(in + ":" + str);
//		}
        return map;
    }

    private int countTable(String s) {
        int n = 0;// 计数器
        int index = 0;// 指定字符的长度
        index = s.indexOf(" ");
        while (index != -1) {
            n++;
            index = s.indexOf(" ", index + 1);
        }
        return (n + 1) / 4;
    }

    private String transWeek(String week) {
        String[] Week = {"一", "二", "三", "四", "五", "六", "日"};
        for (int i = 0; i < 7; i++) {
            if (week.equals(Week[i])) {
                return String.valueOf(i + 1);
            }
        }
        return "0";
    }

    private int getDays(String endDate, String startDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(startDate);
        Date date2 = sdf.parse(endDate);
        long days = (date2.getTime() - date1.getTime()) / 86400000L;
        long yushu = (date2.getTime() - date1.getTime()) % 86400000L;
        return (int) days;
    }

    private String getWeek(String date) throws Exception {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(date);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    protected Boolean passwordCheck(String Html) {
        Document document = Jsoup.parse(Html);
        String title = (document.getElementsByTag("title").get(0)).text();
        if (title.equals("用户登录")) {
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }

}
