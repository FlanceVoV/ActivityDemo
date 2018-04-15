package com.dao;

import com.entity.Leave;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Mapper
public interface LeaveMapper {

    @Select(" SELECT id as `id`, process_id as `processId`, status as `status` from test_leave where user_id = #{userId} and process_id = #{processId}")
    Leave findLeave(@Param("userId")String userId,@Param("processId")String processId);

    @Select(" SELECT id as `id`, process_id as `processId`, status as `status` from test_leave where user_id = #{userId}")
    List<Leave> findLeaves(@Param("userId")String userId);

    @Insert(" INSERT INTO " +
            " test_leave(id,name,task_id,status,user_id,process_id) " +
            " VALUES(#{leave.id},#{leave.name},#{leave.taskId},#{leave.status},#{leave.userId},#{leave.processId}) ")
    void saveLeave(@Param("leave")Leave leave);

    @Update(" UPDATE test_leave SET status = #{leave.status} where id = #{leave.id}")
    void upLeave(@Param("leave")Leave leave);

}
