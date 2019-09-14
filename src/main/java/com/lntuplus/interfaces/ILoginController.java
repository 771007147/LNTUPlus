package com.lntuplus.interfaces;

import java.util.Map;

public interface ILoginController  {
    void stuInfoCallback(Map<String, Object> map);
    void scoreCallback(Map<String, Object> map);
    void examCallback(Map<String, Object> map);
    void tableCallback(Map<String, Object> map);
}
