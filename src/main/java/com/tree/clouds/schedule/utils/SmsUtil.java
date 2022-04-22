package com.tree.clouds.schedule.utils;

import cn.hutool.http.HttpUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsUtil {
    //    private String account = "123162";
//    private String password = "naKfTX";
//    private String extno = "10690371";
//    private String url = "http://47.99.242.143:7862/sms";
    private String account;
    private String password;
    private String extno;
    private String url;

    public static void main(String[] args) {

//        endMs("【测试】测试", Collections.singletonList("15280165562"));
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("action", "balance");
//        paramMap.put("account", account);
//        paramMap.put("rt", "json");
//        String result= HttpUtil.post("http://47.99.242.143:7862/sms", paramMap);
//        System.out.println("result = " + result);
    }

    public void endMs(String content, List<String> phones) {
        String phone = String.join(",", phones);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("action", "send");
        paramMap.put("account", account);
        paramMap.put("password", password);
        paramMap.put("mobile", phone);
        paramMap.put("content", new String(content.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        paramMap.put("extno", extno);
        paramMap.put("rt", "json");
        String result = HttpUtil.post(url, paramMap);
    }
}
