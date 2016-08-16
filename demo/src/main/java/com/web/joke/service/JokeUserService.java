package com.web.joke.service;


import com.web.joke.enity.JokeUser;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface JokeUserService {
    /**
     * 新增/修改用户
     * @param jokeUser
     * @return
     */
    int save(JokeUser jokeUser);

    /**
     * 根据邮箱获取用户
     * @param mail
     * @return
     */
    JokeUser getUserByMail(String mail);
}
