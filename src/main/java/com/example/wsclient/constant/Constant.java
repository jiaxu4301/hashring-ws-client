package com.example.wsclient.constant;


import org.pyj.yeauty.pojo.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jiaxu
 * @Date: 2022/8/31 20:22
 */
public interface Constant {
    Map<String, Session> USER_SESSION_MAP = new ConcurrentHashMap<>();
}
