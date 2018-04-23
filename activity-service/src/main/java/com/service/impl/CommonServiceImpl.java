package com.service.impl;

import com.dao.CommonMapper;
import com.service.CommonService;
import com.util.SQLUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Administrator on 2018/4/19 0019.
 */
@Service
public class CommonServiceImpl implements CommonService {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private CommonMapper commonMapper;

    private static final String PARAM = "param";

    private static final String PROCESS_CODE = "process_code";

    private static final String PROCESS_START = "process_start";


    @Override
    @Transactional
    public Map<String, Object> saveCommonService(Map<String,String> subMap) {

        Map<String,Object> resultMap = new HashMap<>();
        try {
            //获得需要执行的流程code
            String processCode = subMap.get(CommonServiceImpl.PROCESS_CODE);
            //获得部署的流程
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processCode).singleResult();
            //获取流程描述
            String description = processDefinition.getDescription();
            //通过流程描述获得流程状态字典

            //拿到流程模型
            BpmnModel model = repositoryService.getBpmnModel(processDefinition.getId());
            //根据id拿到开始节点
            FlowElement flowElement = model.getMainProcess().getFlowElement(CommonServiceImpl.PROCESS_START);
            //开始流程
            Map<String,String> startMap = new HashMap<>();
            startMap.put("start","1");
            //表单启动流程
            ProcessInstance processInstance =formService.submitStartFormData(processDefinition.getId(), startMap);
            //获得节点描述
            String jStr = flowElement.getDocumentation();
            //保存执行实例id
            subMap.put("process_id", processInstance.getId());
            //获取用户id
            //serviceForm.put("user_id","123");
            subMap.put("now_node","开始节点");

            //安全控制，权限验证
            //设置id
            subMap.put("id", UUID.randomUUID().toString());
            //防止sql注入
            JSONObject jsonObject = JSONObject.fromObject(jStr);
            //配置流程状态
            subMap.put("status","0");
            //获得表名
            String table = jsonObject.getString("table");
            //设置列名与值
            String columns = "";
            String values = "";
            for(String key:subMap.keySet()){
                columns += key + ",";
                values += "'" + subMap.get(key) + "',";
            }
            columns = SQLUtil.parseSql(columns.substring(0,columns.length()-1));
            values = SQLUtil.parseSql(values.substring(0,values.length()-1));

            commonMapper.saveCommonService(table,columns,values);

            resultMap.put("lcid", processInstance.getId());
            resultMap.put("status","success");

        }catch (JSONException je){
            je.printStackTrace();
            throw new RuntimeException("错误：流程描述解析异常！");
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("错误：系统错误！");
        }
        return resultMap;

    }

    @Override
    @Transactional
    public Map<String, Object> upCommonService(String processId,Map<String,String> subMap) {

        // 接口返回结果
        Map<String,Object> resultMap = new HashMap<>();

        try {
            //会签null
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
            Task task = null;
            if(tasks!=null && tasks.size()>0){
                task = tasks.get(0);
            }else{
                resultMap.put("msg","错误：找不到该流程。");
                return resultMap;
            }

            //需要加流程执行验证

            String jStr = task.getDescription();
            subMap.put("now_node",task.getName());
            resultMap.put("nodeName",task.getName());
            // 任务提交map
            Map<String,String> taskMap = new HashMap<>();
            taskMap.put("result", subMap.get("result"));
            // 驱动流程
            formService.submitTaskFormData(task.getId(), taskMap);

            // 保存业务数据
            //安全控制，权限验证
            //防止sql注入
            JSONObject jsonObject = JSONObject.fromObject(jStr);
            //获得表名
            String table = jsonObject.getString("table");
            String values = "";
            String params = "";

            for(String key:subMap.keySet()){
                if(!key.equals(CommonServiceImpl.PARAM)){
                    values += key + " =  '" + subMap.get(key) + "' " + ",";
                }
            }
            //获取参数
            JSONArray jsonArray = JSONArray.fromObject(subMap.get(CommonServiceImpl.PARAM));

            for(Object paramKey:jsonArray){
                params += paramKey + " = '"+subMap.get(paramKey)+ "' AND ";
            }

            values = SQLUtil.parseSql(values.substring(0,values.length()-1));
            params = SQLUtil.parseSql(params.substring(0,params.length()-4));

            commonMapper.upCommonService(table,values,params);

            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).finished().singleResult();

            // 流程尚未结束
            if(historicProcessInstance==null){
                resultMap.put("msg","流程尚未结束。");
            }
            resultMap.put("status","success");
        }catch (JSONException je){
            je.printStackTrace();
            throw new RuntimeException("错误：流程描述解析异常！");
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("错误：系统错误！");
        }

        return resultMap;
    }



}
