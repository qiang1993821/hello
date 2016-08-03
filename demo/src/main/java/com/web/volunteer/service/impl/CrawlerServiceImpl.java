package com.web.volunteer.service.impl;


import com.web.volunteer.dao.CUserDao;
import com.web.volunteer.domain.CUser;
import com.web.volunteer.service.CrawlerService;

import com.web.volunteer.util.CrawlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 */
@SpringBootApplication
@Service
public class CrawlerServiceImpl implements CrawlerService {
    @Autowired
    private CUserDao userDao;
    @Override
    public List<Integer> getAllId(){
        try {
            List<Long> list = userDao.getAllId();
            List<Integer> idList = new ArrayList<Integer>();
            for (Long id : list) {
                idList.add(Integer.parseInt(id + ""));
            }
            return idList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<Integer>();
    }
    @Override
    public void saveAlbum(int uid){
        String url = "http://wap50.yuanyula.com/Index/detail?uid="+uid+"&filter=myImg";
        String result = CrawlerUtil.sendGet(url);
        result = result.replaceAll(" ","");//去空白符
        String[] pics = result.split("<img");
        for (int i=0;i<pics.length;i++){
            if (pics[i].startsWith("src=")){
                String[] ss = pics[i].split("\"");
                for (String s:ss){
                    if (s.startsWith("http")){
                        if (s.endsWith("jpg")||s.endsWith("png")){
                            CrawlerUtil.savePic(s,uid+"_"+i);
                        }
                    }
                }
            }
        }
    }
    @Override
    public void saveUser(CUser user,String code){
        String url = "http://wap50.yuanyula.com/Index/otherDetail?uid="+user.getUid()+"&code="+code;
        String result = CrawlerUtil.sendGet(url);
        result = result.replaceAll(" ","");//去空白符
        result = result.split("征友条件")[0];
        user.setGoal(CrawlerUtil.RegexString(result, "最近上线时间</li><li><span>(.+)</span>交友目的"));
        user.setOpinion(CrawlerUtil.RegexString(result, "交友目的</li><li><span>(.+)</span>恋爱观念"));
        user.setMeeting(CrawlerUtil.RegexString(result, "恋爱观念</li><li><span>(.+)</span>首次见面希望"));
        user.setMlplace(CrawlerUtil.RegexString(result, "首次见面希望</li><li><span>(.+)</span>喜欢爱爱的地点"));
        user.setLoaction(CrawlerUtil.RegexString(result, "年龄</li><li><span>(.+)</span>居住地"));
        user.setHeight(CrawlerUtil.RegexString(result, "居住地</li><li><span>(.+)</span>身高"));
//        user.setIncome(CrawlerUtil.RegexString(result, "^<(.+)</span>月收入?$"));
//        user.setMarry(CrawlerUtil.RegexString(result, "^<(.+)</span>婚姻状态?$"));
        user.setEducation(CrawlerUtil.RegexString(result, "详细资料</h3><ul><li><span>(.+)</span>学历"));
        user.setJob(CrawlerUtil.RegexString(result, "学历</li><li><span>(.+)</span>职业"));
        user.setBirthday(CrawlerUtil.RegexString(result, "职业</li><li><span>(.+)</span>生日"));
        user.setWeight(CrawlerUtil.RegexString(result, "生日</li><li><span>(.+)</span>体重"));
        user.setConstellation(CrawlerUtil.RegexString(result, "体重</li><li><span>(.+)</span>星座"));
        //收入和婚姻状态总是截取不到，不知道为什么
        try {
            String[] ss = result.split("月收入")[0].split("<span>");
            user.setIncome(ss[ss.length-1].split("</span>")[0]);
            ss = result.split("婚姻状态")[0].split("<span>");
            user.setMarry(ss[ss.length - 1].split("</span>")[0]);
        }catch (Exception e){

        }
        userDao.save(user);
    }

    @Override
    @Transactional
    public void rmRepeat() {
        List<CUser> userList = userDao.findAll();
        int[] idArray = new int[100000000];
        System.out.println("333333333333333333333333");
        for (CUser user : userList){
            if (idArray[Integer.valueOf(user.getUid()+"")]==1){
                userDao.delById(user.getId());
                System.out.println("=======" + user.getId());
            }
            idArray[Integer.valueOf(user.getUid()+"")] = 1;
        }
    }
}
