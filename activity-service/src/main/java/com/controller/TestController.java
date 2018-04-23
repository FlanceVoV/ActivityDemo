package com.controller;

import com.dao.GroupMapper;
import com.dao.LeaveMapper;
import com.dao.UserMapper;
import com.entity.Leave;
import com.entity.User;
import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Controller
@RequestMapping("page")
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private LeaveMapper leaveMapper;

    @RequestMapping("/login")
    public String test(){

        return "page/login/login";
    }


    /**
     * 开始流程
     *
     * @param taskCode
     * @return
     */
    @RequestMapping("/apply")
    public String apply(String taskCode){

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(taskCode).singleResult();

        //表单启动流程
        //ProcessInstance processInstance =formService.submitStartFormData(processDefinition.getId(),startMap);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());

        String processInstanceId = processInstance.getId();

        return processInstanceId;
    }


}
