package com.common_activiti_controller;

import com.dao.LeaveMapper;
import com.service.CommonService;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.activiti.bpmn.model.Artifact;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2018/4/19 0019.
 */
@RestController
@RequestMapping("autoActiviti")
public class CommonActivitiController {

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


    /**
     * 通用业务流程设计 v0.1bt
     *
     * 1.流程设计开始以表达提交形式开始并且表单内容为{"start":"1"}
     * 2.开始节点固定命名process_start
     * 3.业务表信息以json字符串形式存入流程节点描述(目前需要传入表名告知操作的是哪张业务表)
     * 4.前台提交表单数据字段必须和数据字段一一对应
     * 5.采用ajax方式post提交，表单转义为jsonStr后在后台解析
     * 6.result为流程判断固定值 0或1。0===>通过，1===>不通过(可以打回上一步)，-1===>终止流程
     * 7.当前节点固定值为now_node
     * 8.用户id可以在前端传入，也可以从session中获取。目前写死并且没有加验证。
     * 9.目前不支持并行网关
     * 10.目前查询节点默认通过用户id和流程id查询(需要保证唯一)。
     * 11.流程设计task名称需要规范。
     * 12.事务同步方面需要在出现异常时抛出RunTimeException才会让activiti事务回滚
     *
     *
     * -1.文件上传
     *
     * 数据库必备字段:process_id,user_id,status,process_code,now_node,id,result
     *
     */


    /**
     * 启动流程
     * @param sMap
     * @return
     */
    @RequestMapping("/startActiviti")
    public Map<String,Object> startActiviti(String sMap){

        //接口返回
        Map<String,Object> resultMap = new HashMap<>();
        try {
            //解析表单提交
            Map<String,String> serviceForm = JSONObject.fromObject(sMap);

            resultMap = commonService.saveCommonService(serviceForm);

        }catch (JSONException je){
            resultMap.put("status","fail");
            resultMap.put("msg","错误：表单数据解析异常！");
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("status","fail");
            resultMap.put("msg",e.getMessage());
        }

        return resultMap;
    }


    @RequestMapping("/moveActiviti/{processId}")
    public Map<String,Object> moveActiviti(@PathVariable("processId")String processId,String sMap){

        // 接口返回结果
        Map<String,Object> resultMap = new HashMap<>();

        try {
            //解析表单提交
            Map<String,String> serviceForm = JSONObject.fromObject(sMap);
            resultMap = commonService.upCommonService(processId,serviceForm);

        }catch (JSONException je){
            resultMap.put("status","fail");
            resultMap.put("msg","错误：表单数据解析异常！");
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("status","fail");
            resultMap.put("msg",e.getMessage());
        }

        return resultMap;
    }




}
