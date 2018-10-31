# 简介
本项目核心代码来自[https://github.com/TongxiJi/shadowsocks-java](https://github.com/TongxiJi/shadowsocks-java)，以下是本人在此基础上所加的功能。

- [x] 整合springboot
- [x] 整合h2/mysql
- [x] 流量监控，存入flow_statistics_detail表
- [ ] 流量控制
- [ ] 动态添加/删除用户
- [ ] 代码美化
- [ ] 代码性能优化
- [ ] web控制端

## 程序初始化
默认新增一个叫admin的用户，密码123456，端口8388

后续再增加用户，端口从10000-10100
