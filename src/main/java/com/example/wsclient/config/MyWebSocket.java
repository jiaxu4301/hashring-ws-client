package com.example.wsclient.config;

import com.example.wsclient.constant.Constant;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.pyj.yeauty.annotation.*;
import org.pyj.yeauty.pojo.Session;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@Slf4j
@ServerPath(path = "/ws")
public class MyWebSocket {

    @BeforeHandshake
    public void handshake(Session session, @RequestParam String userId) {
        log.info("handshake success：userId={}", userId);
        Constant.USER_SESSION_MAP.put(userId, session);
        session.setAttribute("userId", userId);
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
        String userId = session.getAttribute("userId");
        log.info("new connection：userId={}", userId);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String userId = session.getAttribute("userId");
        Constant.USER_SESSION_MAP.remove(userId);
        log.info("onClose ,userId={}", userId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        String userId = session.getAttribute("userId");
        log.info("OnMessage：from userId={},message={}", userId, message);
        // MessageUtil.sendMessage(message.split(",")[0], message.split(",")[1]);
        String userId0 = message.split(",")[0];
        String message0 = message.split(",")[1];

        RestTemplate restTemplate = new RestTemplate();
        URI uri = UriComponentsBuilder.fromHttpUrl("http://127.0.0.1:8888/api/sendmessage")
                .queryParam("userId", userId0)
                .queryParam("message", message0)
                .build().encode().toUri();
        restTemplate.postForObject(uri, null, String.class);
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }

}