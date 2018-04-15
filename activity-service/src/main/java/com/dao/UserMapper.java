package com.dao;

import com.entity.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * Created by lorne on 2017/6/28.
 */
@Mapper
public interface UserMapper {


    @Select(" SELECT " +
            " u.id as `id`," +
            " u.name as `name`," +
            " u.password as `password`," +
            " u.account as `account` " +
            " from test_user as `u` " +
            " where 1 = 1 " +
            " AND " +
            " u.account = #{account} " +
            " AND" +
            " u.password = #{password} ")
    User findUser(@Param("account")String account,@Param("password")String password);


}
