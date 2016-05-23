package com.web.service;

import com.web.domain.Activity;
import com.web.domain.Pend;

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
}
