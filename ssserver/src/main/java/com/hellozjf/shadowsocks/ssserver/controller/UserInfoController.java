package com.hellozjf.shadowsocks.ssserver.controller;

import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.service.IUserInfoService;
import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
@RestController
@RequestMapping("/userInfo")
@Api(tags = "用户信息")
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 获取所有用户信息
     * @return
     */
    @GetMapping("/")
    @ApiOperation("获取所有用户信息")
    public ResultVO<List<UserInfo>> get() {
        return ResultVO.success(userInfoService.findAll());
    }

    /**
     * 获取单个用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("获取单个用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "path")
    })
    public ResultVO<UserInfo> get(@PathVariable("id") String id) {
        return ResultVO.success(userInfoService.findById(id));
    }

    /**
     * 新增一个用户
     * @param userInfo
     * @return
     */
    @PostMapping("/")
    @ApiOperation("新增一个用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userInfo", value = "用户信息", required = true, dataType = "UserInfo", paramType = "query")
    })
    public ResultVO<UserInfo> post(UserInfo userInfo) {
        return ResultVO.success(userInfoService.save(userInfo));
    }

    /**
     * 删除一个用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除一个用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String", paramType = "path")
    })
    public ResultVO delete(@PathVariable("id") String id) {
        userInfoService.delete(id);
        return ResultVO.success();
    }

    /**
     * 更新一个用户
     * @param id
     * @param userInfo
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation("更新一个用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "userInfo", value = "用户信息", required = true, dataType = "UserInfo", paramType = "query")
    })
    public ResultVO<UserInfo> put(@PathVariable("id") String id, UserInfo userInfo) {
        return ResultVO.success(userInfoService.update(id, userInfo));
    }
}
