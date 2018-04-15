package com.dao;

import com.entity.Group;
import com.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Mapper
public interface GroupMapper {

    @Select(" SELECT " +
            " g.id as `id`," +
            " g.name as `name`," +
            " g.code as `code` " +
            " from test_group as `g` " +
            " left join " +
            " test_user_group as `gu` " +
            " on g.id = gu.group_id " +
            " left join " +
            " test_user as `u` " +
            " on u.id = gu.user_id " +
            " where 1 = 1 " +
            " AND " +
            " u.id = #{userId} " )
    List<Group> findUserGroup(@Param("userId")String userId);

}
