package com.hellozjf.shadowsocks.ssserver.controller;

import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.service.IUserInfoService;
import com.hellozjf.shadowsocks.ssserver.util.ResultUtils;
import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 获取所有用户信息
     * @return
     */
    @GetMapping("/")
    public ResultVO get() {
        return ResultUtils.success(userInfoService.findAll());
    }

    /**
     * 获取单个用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO get(@PathVariable("id") Long id) {
        return ResultUtils.success(userInfoService.findById(id));
    }

    /**
     * 新增一个用户
     * @param userInfo
     * @return
     */
    @PostMapping("/")
    public ResultVO post(UserInfo userInfo) {
        return ResultUtils.success(userInfoService.save(userInfo));
    }

    /**
     * 删除一个用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable("id") Long id) {
        userInfoService.delete(id);
        return ResultUtils.success();
    }

    /**
     * 更新一个用户
     * @param id
     * @param userInfo
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO put(@PathVariable("id") Long id, UserInfo userInfo) {
        return ResultUtils.success(userInfoService.put(id, userInfo));
    }
}
