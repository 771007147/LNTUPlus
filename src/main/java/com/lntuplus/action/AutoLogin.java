package com.lntuplus.action;

import com.lntuplus.utils.DayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"/AutoLogin"})
public class AutoLogin {
    private String appid;
    private String secret;
    private String js_code;
    private String grant_type;
    private String serverUrl = "https://api.weixin.qq.com/sns/jscode2session";
    private String openID;
    private String number;
    private String password;
    private Map data = null;
    private String[][] scoreData;
    private String[][] tableData;
    private String[][] examData;
    private String[] infoData;
    private String success;
    private String gpa;
    private int countExam;
    private List scoreList;
    private List tableList;
    private List examList;
    private Map<String, String> infoMap;

    @ResponseBody
    @RequestMapping({"/autoLogin"})
    public Object autoLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        number = req.getParameter("number");
        password = req.getParameter("password");
        data = new HashMap();
//		if (!getOpenID()) {
//			data.put("success", "openid");
//			return data;
//		}
//		if(number==null) {
//			if(!selectNumber()) {
//				data.put("success", "first");
//				return data;
//			}
//		}
        GetMessages gm = new GetMessages(number, password);
        this.success = gm.getSuccess();
        if (this.success.equals("true")) {
            this.scoreData = gm.getScoreData();
            this.examData = gm.getExamData();
            this.tableData = gm.getTableData();
            this.infoData = gm.getInfoData();
            this.infoMap = gm.getInfoMap();
            this.examList = gm.getExamList();
            this.scoreList = gm.getScoreList();
            this.tableList = gm.getTableList();
            this.gpa = gm.getGpa();
            this.countExam = gm.getCountExam();
            data.put("success", success);
            data.put("stuInfo", infoMap);
            data.put("examList", examList);
            data.put("scoreList", scoreList);
            data.put("tableList", tableList);
            data.put("gpa", gpa);
            data.put("countExam", countExam);
            data.put("nowWeek", nowWeek());
            data.put("hashCode", data.hashCode());
            data.put("forumShow", gm.getForumShow());
//			new Thread(new Runnable() {
//				public void run() {
//					Map saveData = data;
//					System.out.println("开始保存数据...");
//					saveData(saveData);
//					System.out.println("保存数据结束");
//				}
//			}).start();

        } else {
            data.put("success", success);
        }
        return data;
    }

//	private boolean getOpenID() {
//		OkHttpClient client = new OkHttpClient();
//		String jsessionid = null;
//		Request request = new Request.Builder().url(this.serverUrl + "?appid=" + this.appid + "&secret=" + this.secret
//				+ "&js_code=" + this.js_code + "&grant_type=" + this.grant_type).build();
//
//		Call call = client.newCall(request);
//		try {
//			Response resp = call.execute();
//			if (resp.isSuccessful()) {
//				Response data = resp;
//				String ss = data.body().string();
//				ss = ss.substring(ss.indexOf("openid") + 9, ss.length() - 2);
//				this.openID = ss;
//				System.out.println(this.openID);
//				return true;
//			}
//			resp.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		return false;
//	}
//
//	private boolean selectNumber() {
//		ComBean cBean = new ComBean();
//		Date now = new Date();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String dateTime = df.format(now);
//
//		List<List> list = new ArrayList();
//		list = cBean.getCom("select number,password from user where openid='" + this.openID + "';", 2);
//		if (list.size() > 0) {
//			this.number = ((List) list.get(0)).get(0).toString();
//			this.password = ((List) list.get(0)).get(1).toString();
//			System.out.println(this.number + " " + this.password);
//			return true;
//		}
//		return false;
//	}

    private String nowWeek() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dfWeek = new SimpleDateFormat("EEEE");
//		Date now = null;
//		try {
//			now = df.parse("2018-10-01");
//		} catch (ParseException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
        Date now = new Date();
        String date = df.format(now);
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);

        Date date1 = null;
        Date date2 = null;

        String upFirstMon = DayUtils.firstMonday(Integer.valueOf(year).intValue(), 3);
        try {
            date1 = df.parse(upFirstMon);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        String downFirstMon = DayUtils.firstMonday(Integer.valueOf(year).intValue(), 9);
        try {
            date2 = df.parse(downFirstMon);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long nd = 86400000L;
        long nh = 3600000L;
        long nm = 60000L;
        long diff;
        if ((date1.before(now)) && (now.before(date2))) {
            diff = now.getTime() - date1.getTime();
        } else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newYearUpFirstMon = null;
            try {
                newYearUpFirstMon = format.parse(upFirstMon);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (now.before(newYearUpFirstMon)) {
                int beforeYear = Integer.valueOf(year) - 1;
                Date beforeYearNineDate = null;
                try {
                    beforeYearNineDate = format.parse(DayUtils.firstMonday(beforeYear, 9));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                diff = now.getTime() - beforeYearNineDate.getTime();
            } else {
                diff = now.getTime() - date2.getTime();
            }
        }
        long howDay = diff / nd;
        long howWeek = howDay / 7L;

        long hour = diff % nd / nh;

        long min = diff % nd % nh / nm;

        return howWeek + "";
    }

//	private void saveData(Map data) {
//		ComBean cBean = new ComBean();
//		Date now = new Date();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String dateTime = df.format(now);
//		int flag = 0;
//
//		Map<String, String> stuInfo = (Map<String, String>) data.get("stuInfo");
//		ArrayList<HashMap<String, String>> examList = (ArrayList<HashMap<String, String>>) data.get("examList");
//
////		for(String key:examList.get(0).keySet())
////        {
////         System.out.println("Key: "+key+" Value: "+examList.get(0).get(key));
////        }
////		for(String key:stuInfo.keySet())
////        {
////         System.out.println("Key: "+key+" Value: "+stuInfo.get(key));
////        }
//		String number = stuInfo.get("number");
//		String password = stuInfo.get("password");
//		String gpa = (String) data.get("gpa");
////      保存绩点
//		String flagS = cBean.getString("select number from gpa where number='" + number + "' and gpa='" + gpa + "';");
//		if (flagS == null) {
//			flag = cBean.comUp("insert into gpa values( '" + stuInfo.get("number") + "','" + data.get("gpa") + "','"
//					+ dateTime + "');");
//			if (flag != 1) {
//				System.out.println("保存gpa失败！");
//			} else {
//				System.out.println("保存gpa成功！");
//			}
//		}
////		保存密码
//		flagS = cBean.getString("select password from account where number='" + number + "';");
//		if (flagS != null) {
//			if (!flagS.equals(password)) {
//				flag = cBean.comUp("update account set password='" + password + "' where number='" + number + "';");
//				if (flag != 1) {
//					System.out.println("更新用户名密码失败！");
//				} else {
//					System.out.println("更新用户名密码成功！");
//				}
//			}
//
//		} else {
//			flag = cBean.comUp("insert into account values( '" + number + "','" + password + "');");
//			if (flag != 1) {
//				System.out.println("保存用户名密码失败！");
//			} else {
//				System.out.println("保存用户名密码成功！");
//			}
//		}
////		保存学生信息
//		flagS = cBean.getString("select stuInfoHashCode from datahashcode where number='" + this.number + "';");
//		int stuInfoHashCode = stuInfo.hashCode();
//
//		if (flagS == null || !flagS.equals(stuInfoHashCode + "")) {
//				flag = cBean.getCount("select number from stuinfo where number='" + this.number + "';");
//				if (flag > 0) {
//					flag = cBean.comUp("delete from stuinfo where number='" + number + "';");
//					if (flag == 1) {
//						System.out.println("删除学生信息成功！");
//					} else {
//						System.out.println("删除学生信息失败！");
//					}
//				}
//				flag = cBean.getCount("select number from stuinfo where number='" + this.number + "';");
//				if(flag == 0) {
//					flag = cBean.comUp("insert into stuinfo values( '" + stuInfo.get("number") + "','"
//							+ stuInfo.get("nationality") + "','" + stuInfo.get("name") + "','" + stuInfo.get("place")
//							+ "','" + stuInfo.get("birth") + "','" + stuInfo.get("typeOfID") + "','"
//							+ stuInfo.get("politicalStatus") + "','" + stuInfo.get("card") + "','"
//							+ stuInfo.get("vehicleInterval") + "','" + stuInfo.get("sex") + "','" + stuInfo.get("nation")
//							+ "','" + stuInfo.get("academic") + "','" + stuInfo.get("professional") + "','"
//							+ stuInfo.get("class") + "','" + stuInfo.get("stuType") + "','" + stuInfo.get("examArea")
//							+ "','" + stuInfo.get("score") + "','" + stuInfo.get("registrationNumber") + "','"
//							+ stuInfo.get("school") + "','" + stuInfo.get("languageType") + "','" + stuInfo.get("comeDate")
//							+ "','" + stuInfo.get("goDate") + "','" + stuInfo.get("address") + "','" + stuInfo.get("origin")
//							+ "','" + stuInfo.get("imgSrc") + "','" + stuInfo.get("password") + "');");
//					if (flag == 1) {
//						System.out.println("保存学生信息成功！");
//					} else {
//						System.out.println("保存学生信息失败！");
//					}
//					flag = cBean.getCount("select number from datahashcode where number='" + this.number + "';");
//					if (flag > 0) {
//						flag = cBean.comUp("update datahashcode set stuInfoHashCode='" + stuInfoHashCode
//								+ "' where number='" + number + "';");
//						if (flag != 1) {
//							System.out.println("更新成绩HashCode成功！");
//						} else {
//							System.out.println("更新成绩HashCode失败！");
//						}
//
//					} else {
//						flag = cBean.comUp("insert into datahashcode values( '" + number + "','" + stuInfoHashCode + "','"
//								+ 0 + "','" + 0 + "','" + dateTime + "');");
//						if (flag == 1) {
//							System.out.println("创建成绩HashCode成功！");
//						} else {
//							System.out.println("创建成绩HashCode成功！");
//						}
//					}
//				}else {
//					System.out.println("表中已有信息");
//				}
//
//
//		}
////		flag = cBean.getCount("select number from score where number='" + this.number + "';");
////		if (flag > 0) {
////			flag = cBean.comUp("delete from score where number='" + this.number + "';");
////			if (flag != 1) {
////				System.out.println("删除成绩失败！");
////			} else {
////				System.out.println("删除成绩成功！");
////				for (int i = 0; i < this.scoreData.length; i++) {
////					flag = cBean.comUp("insert into score values('" + this.number + "','" + this.scoreData[i][0] + "','"
////							+ this.scoreData[i][1] + "','" + this.scoreData[i][2] + "','" + this.scoreData[i][3] + "','"
////							+ this.scoreData[i][4] + "','" + dateTime + "');");
////					if (flag != 1) {
////						System.out.println("更新成绩失败！");
////						System.out.println("Error:" + this.number + "','" + this.scoreData[i][0] + "','"
////								+ this.scoreData[i][1] + "','" + this.scoreData[i][2] + "','" + this.scoreData[i][3]
////								+ "','" + this.scoreData[i][4] + "','" + dateTime);
////					}
////					if (i == this.scoreData.length - 1) {
////						System.out.println("更新成绩成功！");
////					}
////				}
////			}
////		} else if (this.scoreData != null) {
////			for (int i = 0; i < this.scoreData.length; i++) {
////				flag = cBean.comUp("insert into score values('" + this.number + "','" + this.scoreData[i][0] + "','"
////						+ this.scoreData[i][1] + "','" + this.scoreData[i][2] + "','" + this.scoreData[i][3] + "','"
////						+ this.scoreData[i][4] + "','" + dateTime + "');");
////				if (flag != 1) {
////					System.out.println("保存成绩失败！");
////					System.out.println("Error:" + this.number + "','" + this.scoreData[i][0] + "','"
////							+ this.scoreData[i][1] + "','" + this.scoreData[i][2] + "','" + this.scoreData[i][3] + "','"
////							+ this.scoreData[i][4] + "','" + dateTime);
////				}
////				if (i == this.scoreData.length - 1) {
////					System.out.println("保存成绩成功！");
////				}
////			}
////		}
////		flag = cBean.getCount("select number from exam where number='" + this.number + "';");
////		if (flag > 0) {
////			flag = cBean.comUp("delete from exam where number='" + this.number + "';");
////			if (flag != 1) {
////				System.out.println("删除考试失败！");
////			} else {
////				System.out.println("删除考试成功！");
////				for (int i = 0; i < this.examData.length; i++) {
////					flag = cBean.comUp("insert into exam values('" + this.number + "','" + this.examData[i][0] + "','"
////							+ this.examData[i][1] + "','" + this.examData[i][2] + "','" + this.examData[i][3] + "','"
////							+ this.examData[i][4] + "','" + dateTime + "');");
////					if (flag != 1) {
////						System.out.println("更新考试失败！");
////						System.out.println("Error:" + this.number + "','" + this.examData[i][0] + "','"
////								+ this.examData[i][1] + "','" + this.examData[i][2] + "','" + this.examData[i][3]
////								+ "','" + this.examData[i][4] + "','" + dateTime);
////					}
////					if (i == this.examData.length - 1) {
////						System.out.println("更新考试成功！");
////					}
////				}
////			}
////		} else if (this.examData != null) {
////			for (int i = 0; i < this.examData.length; i++) {
////				flag = cBean.comUp("insert into exam values('" + this.number + "','" + this.examData[i][0] + "','"
////						+ this.examData[i][1] + "','" + this.examData[i][2] + "','" + this.examData[i][3] + "','"
////						+ this.examData[i][4] + "','" + dateTime + "');");
////				if (flag != 1) {
////					System.out.println("保存考试失败！");
////					System.out.println("Error:" + this.number + "','" + this.examData[i][0] + "','"
////							+ this.examData[i][1] + "','" + this.examData[i][2] + "','" + this.examData[i][3] + "','"
////							+ this.examData[i][4] + "','" + dateTime);
////				}
////				if (i == this.examData.length - 1) {
////					System.out.println("保存考试成功！");
////				}
////			}
////		}
//	}
}
