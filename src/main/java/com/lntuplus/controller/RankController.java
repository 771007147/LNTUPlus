package com.lntuplus.controller;

import com.lntuplus.action.RankAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/rank")
public class RankController {

    @ResponseBody
    @RequestMapping(value = "/singleCourse")
    public Object singleCourse(HttpServletRequest req, HttpServletResponse resp) {
        String course = req.getParameter("course");
        String mark = req.getParameter("mark");
        String number = req.getParameter("number");
        RankAction rankAction = new RankAction();
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/gpa")
    public Object specialtyGPA(HttpServletRequest req, HttpServletResponse resp) {
        String gpa = req.getParameter("gpa");
        String number = req.getParameter("number");
        RankAction rankAction = new RankAction();
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> classMap = rankAction.GPARank(number, gpa, 0);
        map.put("classRank", classMap.get("rank"));
        map.put("classCount", classMap.get("count"));
        Map<String, Integer> specialtyMap = rankAction.GPARank(number, gpa, 1);
        map.put("specialtyRank", specialtyMap.get("rank"));
        map.put("specialtyCount", specialtyMap.get("count"));
        Map<String, Integer> academicMap = rankAction.GPARank(number, gpa, 2);
        map.put("academicRank", academicMap.get("rank"));
        map.put("academicCount", academicMap.get("count"));
        Map<String, Integer> schoolMap = rankAction.GPARank(number, gpa, 3);
        map.put("schoolRank", schoolMap.get("rank"));
        map.put("schoolCount", schoolMap.get("count"));
        return map;
    }
}
