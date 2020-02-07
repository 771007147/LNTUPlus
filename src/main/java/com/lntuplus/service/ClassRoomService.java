package com.lntuplus.service;

import com.lntuplus.action.ClassRoomAction;
import com.lntuplus.model.ClassRoomModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

@Controller
@EnableAsync
public class ClassRoomService {

    private static final Logger logger = LoggerFactory.getLogger(ClassRoomService.class);

    @Autowired
    private ServletContext servletContext;
    private static String[] ssBuildingNameHLD = {"eyl", "yhl", "jyl", "hldjf", "hldwlsys"};
    private static String[] ssBuildingNameFX = {"bwl", "byl", "xhl", "zhl", "zyl", "zxl", "wlsys", "zljf"};

//    @Override
//    public void afterPropertiesSet() {
//        getClassRoom();
//    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void getClassRoom() {
        int week = TimeUtils.weekNo();
        if (week == 25) {
            week = 0;
        } else {
            week = week + 1;
        }
        week += 1;
        ClassRoomAction classRoomAction = new ClassRoomAction();
        Map<String, ClassRoomModel> hldClassRoom = new HashMap<>();
        for (int i = 0; i < ssBuildingNameHLD.length; i++) {
            ClassRoomModel classRoomModel = classRoomAction.get(String.valueOf(week), ssBuildingNameHLD[i], "0");
            boolean succ = classRoomModel.getSuccess().equals(Constants.STRING_SUCCESS);
            if (succ) {
                hldClassRoom.put(ssBuildingNameHLD[i], classRoomModel);
            }
        }
        servletContext.setAttribute("hldClassRoom", hldClassRoom);
        Map<String, ClassRoomModel> fxClassRoom = new HashMap<>();
        for (int i = 0; i < ssBuildingNameFX.length; i++) {
            ClassRoomModel classRoomModel = classRoomAction.get(String.valueOf(week), ssBuildingNameFX[i], "1");
            boolean succ = classRoomModel.getSuccess().equals(Constants.STRING_SUCCESS);
            if (succ) {
                fxClassRoom.put(ssBuildingNameFX[i], classRoomModel);
            }
        }
        servletContext.setAttribute("fxClassRoom", fxClassRoom);
        logger.info("获取本周空教室成功！");
    }
}