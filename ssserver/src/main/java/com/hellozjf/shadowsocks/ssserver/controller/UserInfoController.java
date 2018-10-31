package com.hellozjf.shadowsocks.ssserver.controller;

import com.hellozjf.shadowsocks.ssserver.SSServer;
import com.hellozjf.shadowsocks.ssserver.config.CustomConfig;
import com.hellozjf.shadowsocks.ssserver.config.JsonConfig;
import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.repository.UserInfoRepository;
import com.hellozjf.shadowsocks.ssserver.util.ChannelUtils;
import com.hellozjf.shadowsocks.ssserver.util.ResultUtils;
import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private JsonConfig jsonConfig;

    @Autowired
    private CustomConfig customConfig;

    @Autowired
    private SSServer ssServer;

    /**
     * 获取所有用户信息
     * @return
     */
    @GetMapping("/")
    public ResultVO get() {
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        return ResultUtils.success(userInfoList);
    }

    /**
     * 获取单个用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO get(@PathVariable("id") Long id) {
        UserInfo userInfo = userInfoRepository.findById(id).orElse(null);
        if (userInfo == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
        }
        return ResultUtils.success(userInfo);
    }

    /**
     * 新增一个用户
     * @param userInfo
     * @return
     */
    @PostMapping("/")
    public ResultVO post(UserInfo userInfo) {

        // 检查username、email、phone是否有重复
        checkDuplicateUsernameEmailPhone(userInfo);

        // 检查method、timeout是否有填写，没填写默认使用Config里面的method和timeout
        checkNullMethodTimeout(userInfo);

        // 分配一个新的port，并启动它
        allocServerPort(userInfo);

        // 新增用户
        userInfo.setId(null);
        UserInfo result = userInfoRepository.save(userInfo);
        return ResultUtils.success(result);
    }


    private void allocServerPort(UserInfo userInfo) {

        // 先查找所有已经使用的端口
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        List<Integer> allocedPorts = new ArrayList<>();
        for (UserInfo user : userInfoList) {
            allocedPorts.add(user.getPort());
        }

        // 然后从minPort号端口开始分配，直到找到一个有效的端口或者超过maxPort为止
        userInfo.setPort(-1);
        for (int port = customConfig.getMinPort(); port <= customConfig.getMaxPort(); port++) {
            if (allocedPorts.indexOf(port) == -1) {
                // 试试看能不能分配端口
                try {
                    ssServer.startSingle(jsonConfig.getServer(), port, userInfo.getPassword(), userInfo.getMethod());
                    // 端口开启成功，那就说明这是我们需要的端口了
                    userInfo.setPort(port);
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
        }

        // 说明端口没有分配成功，那么该用户不可能再分配到端口了，只能返回失败
        if (userInfo.getPort().intValue() == -1) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_ALLOC_SS_SERVER_PORT);
        }
    }

    private void checkNullMethodTimeout(UserInfo userInfo) {
        // TODO 讲道理，这里需要检查method字段，确保用户不会填了一个无效的method
        if (userInfo.getMethod() == null) {
            userInfo.setMethod(jsonConfig.getMethod());
        }
        if (userInfo.getTimeout() == null) {
            userInfo.setTimeout(jsonConfig.getTimeout());
        }
    }

    private void checkDuplicateUsernameEmailPhone(UserInfo userInfo) {

        if (userInfo.getUsername() != null && userInfoRepository.findByUsername(userInfo.getUsername()) != null) {
            // username重复了
            throw new ShadowsocksException(ResultEnum.DUPLICATE_USERNAME);
        }
        if (userInfo.getEmail() != null && userInfoRepository.findByEmail(userInfo.getEmail()) != null) {
            // email重复了
            throw new ShadowsocksException(ResultEnum.DUPLICATE_EMAIL);
        }
        if (userInfo.getPhone() != null && userInfoRepository.findByEmail(userInfo.getPhone()) != null) {
            // phone重复了
            throw new ShadowsocksException(ResultEnum.DUPLICATE_PHONE);
        }
    }

    /**
     * 删除一个用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable("id") Long id) {
        // 先找找看id对应的userInfo是否存在
        UserInfo userInfo = userInfoRepository.findById(id).orElse(null);
        if (userInfo == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
        }
        // 删除用户的时候，要将该用户对应的端口也关闭
        SSServer.TcpAndUdpChannel tcpAndUdpChannel = ssServer.getPortChannelMap().get(userInfo.getPort());
        ChannelUtils.closeChannels(tcpAndUdpChannel.getTcpChannel(), tcpAndUdpChannel.getUdpChannel());
        userInfoRepository.deleteById(id);
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

        // 先找找看id对应的userInfo是否存在
        if (userInfoRepository.findById(id).orElse(null) == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
        }

        // 检查username、email、phone是否有重复
        checkDuplicateUsernameEmailPhone(userInfo);

        // 检查method、timeout是否有填写，没填写默认使用Config里面的method和timeout
        checkNullMethodTimeout(userInfo);

        // TODO 检查是否修改了method和timeout，如果修改了也要重新启动端口

        // TODO 检查是否修改了端口，如果修改了端口要重新分配端口，现在先不允许修改端口
        userInfo.setPort(null);

        // 修改用户
        userInfo.setId(id);
        UserInfo result = userInfoRepository.save(userInfo);
        return ResultUtils.success(result);
    }
}
