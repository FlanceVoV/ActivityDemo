package com.controller;

import com.dao.GroupMapper;
import com.dao.LeaveMapper;
import com.dao.UserMapper;
import com.entity.Group;
import com.entity.Leave;
import com.entity.User;
import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@RestController
@RequestMapping("/user")
public class UserController {

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
    private LeaveMapper leaveMapper;

    @RequestMapping("/login")
    public String login(String account,String password,HttpSession session){

        User user = userMapper.findUser(account,password);

        List<Group> groups = groupMapper.findUserGroup(user.getId());

        user.setGroupList(groups);

        session.setAttribute("nowUser", user);

        return user.getId();
    }

    /**
     * 开始流程
     *
     * @param taskCode
     * @return
     */
    @RequestMapping("/apply")
    public Object apply(String taskCode,String name,HttpSession session){
        User user = (User)session.getAttribute("nowUser");

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(taskCode).singleResult();

        Map<String,String> startMap = new HashMap<>();
        startMap.put("name",name);
        //表单启动流程
        ProcessInstance processInstance =formService.submitStartFormData(processDefinition.getId(),startMap);

        String processInstanceId = processInstance.getId();

        Leave leave = new Leave();
        leave.setStatus("开始请假，等待经理审核");
        leave.setUserId(user.getId());
        leave.setProcessId(processInstanceId);
        leave.setName("name");
        leave.setId(UUID.randomUUID().toString());

        leaveMapper.saveLeave(leave);
        return leave;
    }


    @RequestMapping("/queryApply")
    @ResponseBody
    public Object queryApply(String userId,String processInstanceId){
        Leave leave = null;
        List<Leave> leaves = new ArrayList<>();
        if(processInstanceId != null && !processInstanceId.equals("")){
            leave = leaveMapper.findLeave(userId,processInstanceId);
            return leave;
        }else {
            leaves = leaveMapper.findLeaves(userId);
            return leaves;
        }
    }




}
