package com.controller;

import com.dao.LeaveMapper;
import com.entity.Leave;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/13 0013.
 */

@RestController
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private LeaveMapper leaveMapper;

    @RequestMapping("/approve")
    public String approve(String groupId,String processId,String result,String leaveId){

        //会签
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
        Task task = null;
        //单个任务
        //Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        if(tasks!=null && tasks.size()>0){
            task = tasks.get(0);
        }else{
            return "系统出错：找不到该流程=========";
        }

        Map<String,String> taskMap = new HashMap<String,String>();
        taskMap.put("result",result);
        taskMap.put("groupId", groupId);

        //通过
        if(result.equals("0")){
            Leave leave = new Leave();
            leave.setId(leaveId);
            leave.setStatus(groupId + "==通过==");
            leaveMapper.upLeave(leave);
            formService.submitTaskFormData(task.getId(), taskMap);
        }else{
            Leave leave = new Leave();
            leave.setId(leaveId);
            leave.setStatus(groupId + "==未通过==");
            leaveMapper.upLeave(leave);
            Map<String,Object> comMap = new HashMap<>();
            comMap.put("result", result);
            comMap.put("groupId", groupId);
            taskService.complete(task.getId(), comMap);
        }

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().finished().singleResult();

        if(historicProcessInstance==null){
            return "流程尚未结束========="+task.getName();
        }

        //结束指定任务
        //taskService.complete(task.getId(), variables);

        return "流程结束了";
    }




}
