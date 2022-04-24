package com.tree.clouds.schedule.controller;

import com.tree.clouds.schedule.service.impl.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Slf4j
public class TestController {

    @Autowired
    private TestService testService;


    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        return testService.upload(file);
    }

}