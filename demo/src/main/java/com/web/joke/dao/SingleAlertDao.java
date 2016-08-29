package com.web.joke.dao;

import com.web.joke.enity.SingleAlert;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/8/29.
 */
@Repository
public interface SingleAlertDao extends CrudRepository<SingleAlert, Long> {
}
