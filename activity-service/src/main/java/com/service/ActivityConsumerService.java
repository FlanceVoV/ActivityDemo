package com.service;

import org.activiti.engine.task.TaskQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activityService")
public interface ActivityConsumerService {
    /**
     * 流程demo
     * @return
     */
    @RequestMapping(value="/startActivityDemo",method=RequestMethod.GET)
    public String startActivityDemo(String name);

    /**
     * 流程demo
     * @return
     */
    @RequestMapping(value="/startActivityDemo2",method=RequestMethod.GET)
    public String startActivityDemo2(String id);

}