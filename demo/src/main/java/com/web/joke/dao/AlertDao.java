package com.web.joke.dao;

import com.web.joke.enity.Alert;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
@Repository
public interface AlertDao extends CrudRepository<Alert, Long> {
    @Query("SELECT alert FROM Alert alert WHERE alert.uid = :uid ORDER BY alert.id DESC")
    List<Alert> getMyAlertList(@Param("uid") long uid);
}
