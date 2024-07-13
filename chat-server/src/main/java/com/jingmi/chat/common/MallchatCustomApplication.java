package com.jingmi.chat.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author zhongzb
 * @date 2021/05/27
 */
@SpringBootApplication(scanBasePackages = {"com.jingmi.chat"})
public class MallchatCustomApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallchatCustomApplication.class, args);
    }

}