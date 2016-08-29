package com.web.joke.service;

import com.web.joke.enity.Alert;
import com.web.joke.enity.SingleAlert;

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
}
