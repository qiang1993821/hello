package com.web.service;

import com.web.domain.User;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface UserService {
    /**
     * 新增用户
     * @param user
     * @return
     */
    void save(User user);

    /**
     * 根据名字获取用户
     * @param name
     * @return
     */
    List<User> findUserByName(String name);

    /**
     * 获取全部用户
     * @return
     */
    Iterable<User> getUsers();

    /**
     * 根据用户名字删除
     * @param name
     */
    int deleteUserByName(String name);

    /**
     * 根据id更新用户信息
     * @param name
     * @param id
     * @return
     */
    int updateUserNameById(String name,Integer id);

    /**
     * 根绝token获取用户id
     * @param token
     * @return
     */
    Long findIdByToken(String token);
}
