package com.web.joke.dao;


import com.web.joke.enity.JokeUser;
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
public interface JokeUserDao extends CrudRepository<JokeUser, Integer> {

    @Query("SELECT user FROM JokeUser user WHERE user.mail = :mail")
    List<JokeUser> getUserByMail(@Param("mail") String mail);

}
