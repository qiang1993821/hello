package com.web.dao;


import com.web.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by roc on 2016/4/20.
 */
@Repository
public interface UserDao extends CrudRepository<User, Integer> {
    @Modifying
    @Query("update User user set user.name = :name where user.id = :id")
    int updateNameById(@Param("name") String name, @Param("id") int id);

    @Modifying
    @Query("delete from User user where user.name = :name")
    int deleteUserByName(@Param("name") String name);

    @Query("SELECT user FROM User user WHERE user.name = :name")
    User findUserByName(String name);

}
