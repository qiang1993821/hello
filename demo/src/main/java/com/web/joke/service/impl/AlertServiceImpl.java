package com.web.joke.service.impl;

import com.web.joke.dao.AlertDao;
import com.web.joke.enity.Alert;
import com.web.joke.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/8/18.
 */
@SpringBootApplication
@Service
public class AlertServiceImpl implements AlertService {
    private final Logger logger = LoggerFactory.getLogger(AlertServiceImpl.class);
    @Autowired
    private AlertDao alertDao;

    @Override
    public int save(Alert alert) {
        try {
            alertDao.save(alert);
            return 1;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public Alert getOneById(long alertId) {
        int id = Integer.valueOf(String.valueOf(alertId));
        return alertDao.findOne(id);
    }
}
