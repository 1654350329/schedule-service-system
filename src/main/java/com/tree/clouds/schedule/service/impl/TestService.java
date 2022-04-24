package com.tree.clouds.schedule.service.impl;

import com.tree.clouds.schedule.config.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class TestService {

    public String upload(MultipartFile file) {
        sendMessage(123);
        return "success";
    }

    /**
     * 自定义封装的发送方法
     *
     * @param msg
     */
    private void sendMessage(Integer msg) {
        try {
            WebSocketServer.sendInfo(msg.toString(), "111");
        } catch (IOException e) {
            log.error("消息发送异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}