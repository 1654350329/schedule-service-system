package com.tree.clouds.schedule.config;

import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.utils.SystemUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandler是指你想在url请求的路径
        //addResourceLocations是图片存放的真实路径
        if (SystemUtil.getSystemByName().equalsIgnoreCase("Linux")) {
            Constants.STATIC_PATH = "/mydata/image/";
        }
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + Constants.STATIC_PATH);
        super.addResourceHandlers(registry);
    }
}
