package com.example.wsclient.httphandler;

import com.example.wsclient.constant.Constant;
import com.example.wsclient.util.MessageUtil;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pyj.http.NettyHttpRequest;
import org.pyj.http.annotation.NettyHttpHandler;
import org.pyj.http.handler.IFunctionHandler;
import org.pyj.http.handler.Result;
import org.pyj.http.handler.ResultJson;
import org.pyj.yeauty.pojo.Session;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@NettyHttpHandler(path = "/api/sendmessage", method = "POST")
public class MessageHttpHandler implements IFunctionHandler<String> {

    @SneakyThrows
    @Override
    public Result<String> execute(NettyHttpRequest request) {
        Map<String, String> requestParamMap = getRequestParamMap(request);

        String userId = requestParamMap.get("userId");
        String message = requestParamMap.get("message");
        log.info("发送消息-> userId={},message={}", userId, message);

        try {
            MessageUtil.sendMessage(userId, message);
        } catch (IllegalArgumentException e) {
            return new ResultJson<>(200, e.getMessage());
        }

        return new ResultJson<>(200, "发送成功");
    }

    private Map<String, String> getRequestParamMap(NettyHttpRequest request) throws UnsupportedEncodingException {
        Map<String, String> paramMap = new HashMap<>();
        String uri = request.getUri();
        String requestParam = uri.substring(uri.indexOf("?") + 1);
        requestParam = URLDecoder.decode(requestParam,"UTF-8");
        Stream.of(requestParam.split("&")).forEach(s -> {
            String[] pairs = s.split("=");
            paramMap.put(pairs[0], pairs[1]);
        });

        return paramMap;
    }
}