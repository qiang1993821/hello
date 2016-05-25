package com.web.service;

import com.alibaba.fastjson.JSONObject;
import com.web.domain.Activity;
import com.web.domain.Pend;

import java.util.List;

/**
 * Created by Administrator on 2016/5/22.
 */
public interface ActivityService {
    /**
     * 新增/修改活动
     * @param activity
     * @return
     */
    int save(Activity activity);

    /**
     * 发起活动
     * @param activity
     * @return
     */
    int launch(Activity activity);

    /**
     * 通过id获取活动
     * @param id
     * @return
     */
    Activity findOneById(long id);

    /**
     * 报名活动
     * @param pend
     */
    int join(Pend pend);

    /**
     * 批准报名
     * @param pend
     */
    int approve(Pend pend);

    /**
     * 拒绝报名
     * @param pend
     * @return
     */
    int reject(Pend pend);

    /**
     * 分页查询活动信息
     * @param page
     * @return
     */
    List<JSONObject> queryByPage(int page);

    /**
     * 根据活动名称查询，会有重名
     * @param name
     * @return
     */
    List<JSONObject> queryByName(String name);

    /**
     * 模糊查询
     * @param name
     * @return
     */
    List<String> fuzzyQuery(String name);
}
