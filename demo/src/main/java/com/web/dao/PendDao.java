package com.web.dao;

import com.web.domain.Pend;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendDao extends CrudRepository<Pend,Integer> {

    @Query("SELECT pend FROM Pend pend WHERE pend.id = :id")
    List<Pend> findOneById(@Param("id")long id);

    @Query("SELECT pend FROM Pend pend WHERE pend.activityId = :activityId")
    List<Pend> queryByActivityId(@Param("activityId")long activityId);

    @Modifying
    @Query("DELETE FROM Pend pend WHERE pend.activityId = :activityId")
    void delByActivityId(@Param("activityId")long activityId);
}
