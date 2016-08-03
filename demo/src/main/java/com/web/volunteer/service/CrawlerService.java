package com.web.volunteer.service;

import com.web.volunteer.domain.CUser;

import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 */
public interface CrawlerService {
    List<Integer> getAllId();
    void saveAlbum(int uid);
    void saveUser(CUser user,String code);
    void rmRepeat();
}
