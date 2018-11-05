package com.hellozjf.shadowsocks.ssserver.service.impl;

import com.hellozjf.shadowsocks.ssserver.SSServer;
import com.hellozjf.shadowsocks.ssserver.config.CustomConfig;
import com.hellozjf.shadowsocks.ssserver.config.JsonConfig;
import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.repository.UserInfoRepository;
import com.hellozjf.shadowsocks.ssserver.service.IUserInfoService;
import com.hellozjf.shadowsocks.ssserver.util.ChannelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jingfeng Zhou
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private JsonConfig jsonConfig;

    @Autowired
    private CustomConfig customConfig;

    @Autowired
    private SSServer ssServer;

    @Override
    public List<UserInfo> findAll() {
        return userInfoRepository.findAll();
    }

    @Override
    public UserInfo findById(String id) {
        UserInfo userInfo = userInfoRepository.findById(id).orElse(null);
        if (userInfo == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
        }
        return userInfo;
    }

    @Override
    public UserInfo save(UserInfo userInfo) {

        // 检查username、email、phone是否有重复
        checkDuplicateUsernameEmailPhone(userInfo);

        // 检查method、timeout是否有填写，没填写默认使用Config里面的method和timeout
        checkNullMethodTimeout(userInfo);

        // 分配一个新的port，并启动它
        if (userInfo.getPort() == null) {
            allocServerPort(userInfo);
        } else {
            allocServerPort(userInfo, userInfo.getPort());
        }

        // 新增用户
        userInfo.setId(null);
        UserInfo result = userInfoRepository.save(userInfo);
        return result;
    }

    @Override
    public void delete(String id) {

        // 先找找看id对应的userInfo是否存在
        UserInfo userInfo = userInfoRepository.findById(id).orElse(null);
        if (userInfo == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
        }
        // 删除用户的时候，要将该用户对应的端口也关闭
        SSServer.TcpAndUdpChannel tcpAndUdpChannel = ssServer.getPortChannelMap().get(userInfo.getPort());
        ChannelUtils.closeChannels(tcpAndUdpChannel.getTcpChannel(), tcpAndUdpChannel.getUdpChannel());
        // TODO 删除用户的时候，要将FlowStatisticsDetail和FlowSummary表里面的数据也要删除
        userInfoRepository.deleteById(id);
    }

    @Override
    public UserInfo update(String id, UserInfo userInfo) {

        // 先找找看id对应的userInfo是否存在
        if (userInfoRepository.findById(id).orElse(null) == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
        }

        // 检查username、email、phone是否有重复
        checkDuplicateUsernameEmailPhone(userInfo);

        // 检查method、timeout是否有填写，没填写默认使用Config里面的method和timeout
        checkNullMethodTimeout(userInfo);

        // TODO 检查是否修改了method和timeout，如果修改了也要重新启动端口

        // TODO 检查是否修改了端口，如果修改了端口要重新分配端口，端口只允许分配在10000-10101，修改了端口还要修改FlowStatisticsDetail和FlowSummary表
        userInfo.setPort(null);

        // 修改用户
        userInfo.setId(id);
        UserInfo result = userInfoRepository.save(userInfo);
        return result;
    }

    /**
     * 让系统随机分配一个端口
     * @param userInfo
     */
    private void allocServerPort(UserInfo userInfo) {

        // 先查找所有已经使用的端口
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        List<Integer> allocedPorts = findAllAllocedPorts(userInfoList);

        // 然后从minPort号端口开始分配，直到找到一个有效的端口或者超过maxPort为止
        allocPort(userInfo, allocedPorts);

        // 说明端口没有分配成功，那么该用户不可能再分配到端口了，只能返回失败
        if (userInfo.getPort().intValue() == -1) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_ALLOC_SS_SERVER_PORT);
        }
    }

    /**
     * 优先使用priorPort，如果priorPort被占用，再由系统分配端口
     * @param userInfo
     * @param priorPort
     */
    private void allocServerPort(UserInfo userInfo, Integer priorPort) {

        // 先查找所有已经使用的端口
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        List<Integer> allocedPorts = findAllAllocedPorts(userInfoList);

        // 如果priorPort不在allocedPorts里面，而且用该端口开启ssserver成功，那么就算成功
        boolean bAllocSuccessed = false;
        if (allocedPorts.indexOf(priorPort) == -1) {
            if (priorPort.intValue() < customConfig.getMinPort().intValue()  ||
                    priorPort.intValue() > customConfig.getMaxPort().intValue()) {
                log.debug("自定义端口{}超出范围{}-{}", priorPort, customConfig.getMinPort(), customConfig.getMaxPort());
            } else {
                try {
                    ssServer.startSingle(jsonConfig.getServer(), priorPort, userInfo.getPassword(), userInfo.getMethod());
                    bAllocSuccessed = true;
                } catch (Exception e) {
                    log.debug("开启自定义端口{}失败", priorPort);
                }
            }
        } else {
            log.debug("自定义端口{}已被占用", priorPort);
        }

        // 如果没有分配成功，那么从minPort号端口开始分配，直到找到一个有效的端口或者超过maxPort为止
        if (! bAllocSuccessed) {
            allocPort(userInfo, allocedPorts);
        }

        // 说明端口没有分配成功，那么该用户不可能再分配到端口了，只能返回失败
        if (userInfo.getPort().intValue() == -1) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_ALLOC_SS_SERVER_PORT);
        }
    }

    /**
     * 找出所有已经被分配的端口号
     * @param userInfoList
     * @return
     */
    private List<Integer> findAllAllocedPorts(List<UserInfo> userInfoList) {
        List<Integer> allocedPorts = new ArrayList<>();
        for (UserInfo user : userInfoList) {
            allocedPorts.add(user.getPort());
        }
        return allocedPorts;
    }

    /**
     * 检测method和timeout字段是否为null
     * @param userInfo
     */
    private void checkNullMethodTimeout(UserInfo userInfo) {
        // TODO 讲道理，这里需要检查method字段，确保用户不会填了一个无效的method
        if (userInfo.getMethod() == null) {
            userInfo.setMethod(jsonConfig.getMethod());
        }
        if (userInfo.getTimeout() == null) {
            userInfo.setTimeout(jsonConfig.getTimeout());
        }
    }

    /**
     * 检查数据库中用户名、邮箱、手机号码是否重复
     * @param userInfo
     */
    private void checkDuplicateUsernameEmailPhone(UserInfo userInfo) {

        if (userInfo.getUsername() != null && userInfoRepository.findByUsername(userInfo.getUsername()) != null) {
            // username重复了
            throw new ShadowsocksException(ResultEnum.DUPLICATE_USERNAME);
        }
        if (userInfo.getEmail() != null && userInfoRepository.findByEmail(userInfo.getEmail()) != null) {
            // email重复了
            throw new ShadowsocksException(ResultEnum.DUPLICATE_EMAIL);
        }
        if (userInfo.getPhone() != null && userInfoRepository.findByPhone(userInfo.getPhone()) != null) {
            // phone重复了
            throw new ShadowsocksException(ResultEnum.DUPLICATE_PHONE);
        }
    }

    /**
     * 根据已经分配的端口列表，以及配置文件中最大和最小端口号，开始分配端口
     * @param userInfo
     * @param allocedPorts
     */
    private void allocPort(UserInfo userInfo, List<Integer> allocedPorts) {
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
    }
}
