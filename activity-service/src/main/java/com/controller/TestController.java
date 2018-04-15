package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Controller
public class TestController {

    @RequestMapping("/test")
    public Object test(){

        return "test";
    }

}
