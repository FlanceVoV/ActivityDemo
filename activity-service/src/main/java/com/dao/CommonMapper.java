package com.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018/4/19 0019.
 */
@Mapper
public interface CommonMapper {

    public void saveCommonService(@Param("tableName")String tableName,@Param("columns")String columns,@Param("values")String values);

    public void upCommonService(@Param("tableName")String tableName,@Param("values")String values,@Param("param")String param);


}
