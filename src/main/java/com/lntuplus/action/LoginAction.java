package com.lntuplus.action;

import com.lntuplus.utils.*;
import okhttp3.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginAction {

    private OkHttpUtils mOkHttpUtils;
    private String mPort;
    private String mSession;
    private String mSessionUrl = "/common/security/check1.jsp";
    private String mLoginUrl = "/j_acegi_security_check";
    private String mCheckUrl = "/frameset.jsp";

    public Map<String, String> login(String number, String password) {
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, String> map = new HashMap<>();
        mOkHttpUtils = OkHttpUtils.getInstance();
        mPort = sqlSession.selectOne("PortMapper.select");
        if (mPort.equals(Constants.STRING_FAILED) || mPort.equals(Constants.STRING_ERROR)) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_PORT);
            return map;
        }
        jointUrl();
        mSession = mOkHttpUtils.getSession(mSessionUrl);
        if (mSession.equals(Constants.STRING_FAILED) || mSession.equals(Constants.STRING_ERROR)) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_SESSION);
            return map;
        }
        String post = post(number, password, mSession);
        if (post.equals(Constants.STRING_FAILED)) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_POST);
            return map;
        }
        if (!postCheck(post)) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_PASSWORD);
            return map;
        }
        map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
        map.put(Constants.STRING_SESSION, mSession);
        map.put(Constants.STRING_PORT, mPort);
        return map;
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

    private void jointUrl() {
        mSessionUrl = mPort + "/common/security/check1.jsp";
        mLoginUrl = mPort + "/j_acegi_security_check";
        mCheckUrl = mPort + "/frameset.jsp";
    }

    private String post(String number, String password, String session) {
        RequestBody formBody = new FormBody.Builder().add("j_username", number).add("j_password", password).build();
        Call call = mOkHttpUtils.getInfoCallRequestBody(mLoginUrl, session, formBody);
        Response resp = null;
        try {
            resp = call.execute();
            if (resp.isSuccessful()) {
                String data = resp.body().string();
                resp.close();
                return data;
            }
        } catch (IOException e) {
            resp.close();
            e.printStackTrace();
            System.out.println(TimeUtils.getTime() + " Post登录失败！");
        }
        return Constants.STRING_FAILED;
    }

    private boolean postCheck(String Html) {
        Document document = Jsoup.parse(Html);
        String title = (document.getElementsByTag("title").get(0)).text();
        if (title.equals("用户登录")) {
            return false;
        }
        return true;
    }
}
