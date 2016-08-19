package com.web.joke.dao;

import com.web.joke.enity.Alert;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/8/18.
 */
@Repository
public interface AlertDao extends CrudRepository<Alert, Integer> {
}
