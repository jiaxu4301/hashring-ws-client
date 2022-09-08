package com.example.wsclient.util;

import com.example.wsclient.constant.Constant;
import org.pyj.yeauty.pojo.Session;

/**
 * @Author: jiaxu
 * @Date: 2022/9/2 13:55
 */
public class MessageUtil {
    public static void sendMessage(String userId, String message) throws IllegalArgumentException{
        Session session = Constant.USER_SESSION_MAP.get(userId);
        if (session == null) {
            throw new IllegalArgumentException("用户不在线");
        }
        session.sendText(message);
    }
}
