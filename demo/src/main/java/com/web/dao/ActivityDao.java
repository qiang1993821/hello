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
}
