package com.lntuplus.action;

import java.util.List;

import com.lntuplus.model.EverydayTextModel;
import com.lntuplus.utils.ComBean;

public class EverydayTextAction {

    public EverydayTextModel get() {
        EverydayTextModel everydayTextModel = new EverydayTextModel();
        ComBean cBean = new ComBean();
        List textList = cBean.get1Com("select * from everydayText;", 2);
        everydayTextModel.setTitle(textList.get(0).toString());
        everydayTextModel.setAuthor(textList.get(1).toString());
        System.out.print(everydayTextModel.toString());
        return everydayTextModel;
    }
}
