package com.web.joke.service;

import com.alibaba.fastjson.JSONObject;
import com.web.joke.enity.Alert;
import com.web.joke.enity.SingleAlert;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public interface AlertService {

    /**
     * 新增修改单个弹窗
     * @param alert
     * @return
     */
    int savePage(SingleAlert alert);

    /**
     * 新增修改
     * @param alert
     * @return
     */
    int save(Alert alert);

    /**
     * 根据id获取弹窗
     * @param alertId
     * @return
     */
    Alert getOneById(long alertId);

    /**
     * 保存单个弹窗的顺序
     * @param alert
     * @param num
     * @param pageId
     * @return
     */
    boolean saveSequence(Alert alert,int num,long pageId);

    /**
     * 获取总页数
     * @param alertId
     * @return
     */
    int getPageNum(long alertId);

    /**
     * 获取照片列表
     * @param alertId
     * @return
     */
    List<JSONObject> getPageList(long alertId);

    /**
     * 根据id获取一个弹窗
     * @param pageId
     * @return
     */
    SingleAlert getOnePage(long pageId);

    /**
     * 获取我的全部弹窗
     * @param uid
     * @return
     */
    List<JSONObject> getMyAlertList(long uid);

    /**
     * 删除弹窗
     * @param pageId
     * @param alertId
     * @return
     */
    int delPage(long pageId,long alertId);

    /**
     * 删除整个弹窗
     * @param alertId
     * @return
     */
    int delAlert(long alertId);

    /**
     * 获取全部弹窗
     * @param alertStr
     * @return
     */
    List<SingleAlert> getAllPage(String alertStr);
}
