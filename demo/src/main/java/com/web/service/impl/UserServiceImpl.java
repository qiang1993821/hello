package com.web.service.impl;

import com.web.dao.UserDao;
import com.web.domain.User;
import com.web.service.UserService;
import com.web.util.CacheUtil;
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
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public List<User> findUserByName(String name) {
        return userDao.findUserByName(name);
    }

    @Override
    public Iterable<User> getUsers() {
        return userDao.findAll();
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

    @Override
    public Long findIdByToken(String token) {
        if (token!=null) {
            Long uid = (Long) CacheUtil.getCache(token);
            if (uid == null) {
                try {
                    uid = userDao.findIdByToken(token).get(0);
                }catch (Exception e){
                    logger.error("findIdByToken:"+e.getMessage());
                }
            }
            return uid;
        }else {
            return null;
        }
    }
}
