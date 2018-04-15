# ActivityDemo

# 工作流执行实例
配合 SpringCloud_Activity项目使用 https://github.com/FlanceVoV/SpringCloud_Activity  

默认提供简单请假流程  

1.用户登录  
http://localhost:8081/activity/user/login?account=e&password=123  

2.开始请假任务  
http://localhost:8081/activity/user/apply?taskCode=leave&name=qjcs  

3.根据userId查看请假列表  
http://localhost:8081/activity/user/queryApply?userId=user_test_5  

4.请假经理审批  
http://localhost:8081/activity/admin/approve?groupId=group_test_1&result=1&leaveId=478fef72-38e3-4242-b1ae-d6278ef431a7&processId=22501  
groupId     组id(group_test_1经理)  
result      审批结果(0通过，1不通过)  
leaveId     请假id  
processId   流程实例id  

5.请假人事审批  
http://localhost:8081/activity/admin/approve?groupId=group_test_3&result=0&leaveId=096e4e72-e2dd-4cfc-9a64-449991a9a7d5&processId=20020  
groupId     组id(group_test_3人事)  
result      审批结果(0通过，1不通过)  
leaveId     请假id  
processId   流程实例id  

