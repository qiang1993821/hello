package com.web.joke.service.impl;

import com.web.joke.dao.JokeUserDao;
import com.web.joke.enity.JokeUser;
import com.web.joke.service.JokeUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/4/20.
 */
@SpringBootApplication
@Service
public class JokeUserServiceImpl implements JokeUserService {
    private final Logger logger = LoggerFactory.getLogger(JokeUserServiceImpl.class);
    @Autowired
    private JokeUserDao jokeUserDao;

    @Override
    public int save(JokeUser jokeUser) {
        try {
            jokeUserDao.save(jokeUser);
            return 1;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public JokeUser getUserByMail(String mail) {
        JokeUser jokeUser = null;
        try {
            jokeUser = jokeUserDao.getUserByMail(mail).get(0);
        }catch (Exception e){
            logger.error("joke|getUserByMail|"+e.getMessage());
        }
        return jokeUser;
    }
}
