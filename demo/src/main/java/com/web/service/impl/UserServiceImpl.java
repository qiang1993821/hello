package com.web.service.impl;

import com.web.dao.UserDao;
import com.web.domain.User;
import com.web.service.UserService;
import com.web.util.CacheUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
@SpringBootApplication
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public int save(User user) {
        try {
            userDao.save(user);
            return 1;
        }catch (Exception e){
            logger.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public List<User> findUserByName(String name) {
        return userDao.findUserByName(name);
    }

    @Override
    public User findOneUser(long id) {
        return userDao.findOneById(id).get(0);
    }

    @Override
    @Transactional
    public int deleteUserByName(String name) {
        return userDao.deleteUserByName(name);
    }

    @Override
    @Transactional
    public int updateUserNameById(String name, Integer id) {
        return userDao.updateNameById(name,id);
    }

//    @Override
//    public Long findIdByOpenid(String openid) {
//        if (openid!=null) {
//            Long uid = (Long) CacheUtil.getCache(openid);
//            if (uid == null) {
//                try {
//                    uid = userDao.findIdByOpenid(openid).get(0);
//                }catch (Exception e){
//                    logger.error("findIdByOpenid:"+e.getMessage());
//                }
//            }
//            return uid;
//        }else {
//            return null;
//        }
//    }

    @Override
    public String getPhoneById(long id) {
        String key = "phone-"+id;
        String phone = (String)CacheUtil.getCache(key);
        if (StringUtils.isBlank(phone)){
            try {
                phone = userDao.getPhoneById(id).get(0);
            }catch (Exception e){
                logger.error("getPhoneById:"+e.getMessage());
            }
        }
        return phone;
    }

    @Override
    public Long findIdByName(String name) {
        if (name!=null) {
            Long uid = (Long) CacheUtil.getCache(name);
            if (uid == null) {
                try {
                    uid = userDao.findIdByName(name).get(0);
                }catch (Exception e){
                    logger.error("findIdByName:"+e.getMessage());
                }
            }
            return uid;
        }else {
            return null;
        }
    }
}
