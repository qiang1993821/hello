package com.web.dao;

import com.web.domain.Activity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityDao extends PagingAndSortingRepository<Activity,Integer> {

    @Query("SELECT activity FROM Activity activity WHERE activity.id = :id")
    List<Activity> findOneById(@Param("id")long id);

    @Query("SELECT activity FROM Activity activity WHERE activity.name = :name order by activity.id desc")
    List<Activity> queryByName(@Param("name")String name);

    @Query("SELECT activity.name FROM Activity activity WHERE activity.name LIKE :name order by activity.id desc")
    List<String> queryLikeName(@Param("name")String name);
}
