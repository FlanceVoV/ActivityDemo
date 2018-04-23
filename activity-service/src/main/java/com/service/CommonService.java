package com.service;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/19 0019.
 */
public interface CommonService {


    Map<String,Object> saveCommonService(Map<String,String> subMap);

    Map<String,Object> upCommonService(String processId,Map<String,String> subMap);
}
