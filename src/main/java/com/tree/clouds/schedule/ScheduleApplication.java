package com.tree.clouds.schedule;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@MapperScan("com.tree.clouds.schedule.mapper")
public class ScheduleApplication {
    public static void main(String[] args) {

        SpringApplication.run(ScheduleApplication.class, args);
        log.info("时光相册系统启动成功");
    }
}
