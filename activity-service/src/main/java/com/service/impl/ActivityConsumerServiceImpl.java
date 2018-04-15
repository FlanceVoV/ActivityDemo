package com.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.service.ActivityConsumerService;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityService")
public class ActivityConsumerServiceImpl implements ActivityConsumerService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

//        //流程启动
//        ExecutionEntity pi1 = (ExecutionEntity) runtimeService.startProcessInstanceByKey("leave");
//        String processId = pi1.getId();
//        String taskId = pi1.getTasks().get(0).getId();
//        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();


    //taskService.complete(taskId, map);//完成第一步申请


    //        String taskId2 = task.getId();
//        map.put("pass", false);
//        taskService.complete(taskId2, map);//驳回申请


    private static String id;

    /**
     *
     * 经理审批
     * @return
     */
    @Override
    public String startActivityDemo(String name) {
        System.out.println("method startActivityDemo begin....");
        Map<String,String> smap = new HashMap<String,String>();
        smap.put("start","1");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").singleResult();
        //ExecutionEntity pi1 = (ExecutionEntity) runtimeService.startProcessInstanceByKey("leave");

        String id = processDefinition.getId();

        //表单启动流程
        ProcessInstance processInstance =formService.submitStartFormData(id,smap);

        String vid = processInstance.getId();

        /**
         *  获取任务
         *  如果是会签任务则可能返回多个任务
         *  会签任务：多个任务共同组成一个任务
         *  根据传进来的用户id获得该用户的会签任务
         *
         */
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for(Task task:taskList){


        }

        Task task1 = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        Map<String,String> taskMap = new HashMap<String,String>();
        taskMap.put("test","true");
        formService.submitTaskFormData(task1.getId(), taskMap);

        return task1.getName()+"======="+vid;
    }

    /**
     * 人事审批
     * @return
     */
    @Override
    public String startActivityDemo2(String id) {
        Task task = taskService.createTaskQuery().processInstanceId(id).singleResult();
        Map<String,String> taskMap = new HashMap<String,String>();
        taskMap.put("test","false");
        formService.submitTaskFormData(task.getId(), taskMap);

        return task.getName();
    }

}