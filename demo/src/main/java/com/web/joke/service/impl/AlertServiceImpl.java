package com.web.joke.service.impl;

import com.alibaba.fastjson.JSONObject;
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
            String[] pageArr = listStr.split("-");
            if (StringUtils.isNotBlank(listStr)){
                for (int i = 0; i < pageArr.length; i++){
                    pageList.add(pageArr[i]);
                }
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

    @Override
    public List<JSONObject> getPageList(long alertId) {
        Alert alert = alertDao.findOne(alertId);
        String alertList =  alert.getAlertList();
        if (StringUtils.isBlank(alertList))
            return null;
        try {
            String[] pageArr = alertList.split("-");
            List<JSONObject> infoList = new ArrayList<JSONObject>();
            for (int i = 0; i < pageArr.length; i++){
                JSONObject info = new JSONObject();
                SingleAlert page = pageDao.findOne(Long.valueOf(pageArr[i]));
                info.put("url","editPage?alertId="+alertId+"&pageId="+page.getId()+"&"+"currentPage="+(i+1));
                info.put("info",page.getContent());
                if (page.getPrompt() == 1)
                    info.put("tips","答");
                infoList.add(info);
            }
            if (infoList.size()>0)
                return infoList;
            else
                return null;
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public SingleAlert getOnePage(long pageId) {
        try {
            return pageDao.findOne(pageId);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<JSONObject> getMyAlertList(long uid) {
        try {
            List<Alert> alertList = alertDao.getMyAlertList(uid);
            if (alertList != null && alertList.size()>0){
                List<JSONObject> infoList = new ArrayList<JSONObject>();
                for (Alert alert:alertList){
                    JSONObject info = new JSONObject();
                    info.put("imgUrl","alert/getImg?alertId="+alert.getId());
                    info.put("url","edit?alertId="+alert.getId());
                    info.put("title",alert.getTitle());
                    infoList.add(info);
                }
                return infoList;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
}
