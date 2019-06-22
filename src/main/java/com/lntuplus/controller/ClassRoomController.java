package com.lntuplus.controller;

import com.lntuplus.action.ClassRoomAction;
import com.lntuplus.model.ClassRoomModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping({"/classroom"})
public class ClassRoomController {

    @ResponseBody
    @RequestMapping({"/get"})
    public Object get(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=utf-8");
        String weeks = req.getParameter("weeks");
        String name = req.getParameter("buildingname");
        String campus = req.getParameter("campus");
        ClassRoomAction classRoomAction = new ClassRoomAction();
        ClassRoomModel classRoomModel = classRoomAction.get(weeks, name, campus);
        return classRoomModel;
    }
}
