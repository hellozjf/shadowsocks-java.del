package com.hellozjf.shadowsocks.ssserver.service;

import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author Jingfeng Zhou
 */
public interface IUserInfoService {
    List<UserInfo> findAll();
    UserInfo findById(String id);
    UserInfo save(UserInfo userInfo);
    void delete(String id);
    UserInfo update(String id, UserInfo userInfo);
}
