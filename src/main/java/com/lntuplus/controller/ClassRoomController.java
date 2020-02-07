package com.lntuplus.controller;

import com.lntuplus.action.ClassRoomAction;
import com.lntuplus.action.WeekAction;
import com.lntuplus.model.ClassRoomModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping({"/classroom"})
@CrossOrigin
public class ClassRoomController {

    private static final Logger logger = LoggerFactory.getLogger(ClassRoomController.class);
    @Autowired
    private ServletContext servletContext;

    @ResponseBody
    @RequestMapping({"/get"})
    public Object get(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=utf-8");
        String weeks = req.getParameter("weeks");
        String name = req.getParameter("buildingname");
        String campus = req.getParameter("campus");
        String nowWeek = String.valueOf(new WeekAction().getWeek() + 1);
        if (nowWeek.equals(weeks)) {
            String attr = null;
            if (campus.equals("0")) {
                attr = "hldClassRoom";
            } else {
                attr = "fxClassRoom";
            }
            Map<String, ClassRoomModel> classRooms = (Map<String, ClassRoomModel>) servletContext.getAttribute(attr);
            if (classRooms != null && classRooms.containsKey(name)) {
                return classRooms.get(name);
            }
        }

        ClassRoomAction classRoomAction = new ClassRoomAction();
        ClassRoomModel classRoomModel = classRoomAction.get(weeks, name, campus);
        return classRoomModel;
    }
}
