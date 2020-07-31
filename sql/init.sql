CREATE TABLE `AlarmConfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '告警名称',
  `app` varchar(25) NOT NULL COMMENT '监听的APP',
  `pileNo` int(11) NOT NULL COMMENT '桩点',
  `warningStrategy` int(11) NOT NULL COMMENT '预警策略 , see : com.stuben.monitop.server.enums.StrategyEnum',
  `warningThreshold` int(11) NOT NULL COMMENT '预警阈值',
  `alarmTrigger` int(11) NOT NULL COMMENT '预警-->告警的触发次数',
  `alarmEmails` varchar(500) NOT NULL COMMENT '告警的邮箱 , 用 , 分隔',
  `enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否生效',
  `lastHeartbeatTime` datetime NOT NULL COMMENT '最近心跳时间',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='告警配置';

CREATE TABLE `MonitorProxyInfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(20) NOT NULL COMMENT '桩号名称',
  `appName` varchar(50) NOT NULL COMMENT '监控的程序名称',
  `pileNo` int(11) NOT NULL COMMENT '桩号',
  `classFullName` varchar(100) NOT NULL DEFAULT '' COMMENT '类的全限定名',
  `methodName` varchar(30) NOT NULL DEFAULT '' COMMENT '方法名称',
  `param` varchar(100) NOT NULL DEFAULT '' COMMENT '参数的SPEL表达式',
  `condition` varchar(100) DEFAULT '' COMMENT '参数满足的条件的SPEL表达式',
  `enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否生效',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='监控代理信息';
