package com.web.service.impl;

import com.web.dao.UserDao;
import com.web.domain.User;
import com.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/4/20.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public User findUserByName(String name) {
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
}
