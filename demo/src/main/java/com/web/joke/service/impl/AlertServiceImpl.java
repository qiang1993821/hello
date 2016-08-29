package com.web.joke.service.impl;

import com.web.joke.dao.AlertDao;
import com.web.joke.dao.SingleAlertDao;
import com.web.joke.enity.Alert;
import com.web.joke.enity.SingleAlert;
import com.web.joke.service.AlertService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
@SpringBootApplication
@Service
public class AlertServiceImpl implements AlertService {
    private final Logger logger = LoggerFactory.getLogger(AlertServiceImpl.class);
    @Autowired
    private AlertDao alertDao;
    @Autowired
    private SingleAlertDao pageDao;

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
        return alertDao.findOne(alertId);
    }

    @Override
    public int savePage(SingleAlert alert) {
        try {
            pageDao.save(alert);
            return 1;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean saveSequence(Alert alert, int num, long pageId) {
        try {
            String listStr = alert.getAlertList();
            List<String> pageList = new ArrayList<String>();
            if (StringUtils.isNotBlank(listStr)){
                pageList = Arrays.asList(listStr.split("-"));
            }
            pageList.remove(pageId+"");
            String newList = "";
            for (int i = 1;i<pageList.size()+2;i++){
                if (i<num){
                    newList += pageList.get(i-1)+"-";
                }else if (i==num){
                    newList += pageId+"-";
                }else {
                    newList += pageList.get(i-2)+"-";
                }
            }
            alert.setAlertList(newList);
            alertDao.save(alert);
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public int getPageNum(long alertId) {
        Alert alert = alertDao.findOne(alertId);
        String alertList =  alert.getAlertList();
        if (StringUtils.isBlank(alertList))
            return 0;
        return alertList.split("-").length;
    }
}
