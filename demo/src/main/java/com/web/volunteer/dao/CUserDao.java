package com.web.volunteer.dao;



import com.web.volunteer.domain.CUser;
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
public interface CUserDao extends CrudRepository<CUser, Integer> {
    @Query("SELECT user.uid FROM CUser user")
    List<Long> getAllId();

    @Query("SELECT cUser FROM CUser cUser ")
    List<CUser> findAll();

    @Modifying
    @Query("DELETE FROM CUser user WHERE user.id = :id")
    void delById(@Param("id")long id);
}
