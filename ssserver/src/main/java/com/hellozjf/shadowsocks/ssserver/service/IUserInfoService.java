package com.hellozjf.shadowsocks.ssserver.service;

import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;

import java.util.List;

/**
 * @author Jingfeng Zhou
 */
public interface IUserInfoService {
    List<UserInfo> findAll();
    UserInfo findById(Long id);
    UserInfo save(UserInfo userInfo);
    void delete(Long id);
    UserInfo put(Long id, UserInfo userInfo);
}
