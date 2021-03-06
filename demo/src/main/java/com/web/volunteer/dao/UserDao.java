package com.web.volunteer.dao;


import com.web.volunteer.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    List<User> findUserByName(@Param("name")String name);

    @Query("SELECT user FROM User user WHERE user.id = :id")
    List<User> findOneById(@Param("id")long id);

//    @Query("SELECT user.id FROM User user WHERE user.token = :token")
//    List<Long> findIdByToken(@Param("token")String token);

    @Query("SELECT user.id FROM User user WHERE user.name = :name")
    List<Long> findIdByName(@Param("name")String name);

    @Query("SELECT user FROM User user WHERE user.mail = :mail")
    List<User> getUserByMail(@Param("mail")String mail);

    @Query("SELECT user.phone FROM User user WHERE user.id = :id")
    List<String> getPhoneById(@Param("id")long id);

    @Query("SELECT user.pwd FROM User user WHERE user.mail = :mail")
    List<String> getPwdByMail(@Param("mail")String mail);
}
