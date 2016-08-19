package com.web.joke.service;

import com.web.joke.enity.Alert;

/**
 * Created by Administrator on 2016/8/18.
 */
public interface AlertService {

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
}
