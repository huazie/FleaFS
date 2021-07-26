/*
Navicat MySQL Data Transfer

Source Server         : Flea
Source Server Version : 50538
Source Host           : localhost:3306
Source Database       : fleafs

Target Server Type    : MYSQL
Target Server Version : 50538
File Encoding         : 65001

Date: 2021-07-15 20:23:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `flea_file_attr`
-- ----------------------------
DROP TABLE IF EXISTS `flea_file_attr`;
CREATE TABLE `flea_file_attr` (
  `attr_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性编号',
  `file_id` varchar(64) NOT NULL COMMENT '文件编号',
  `attr_code` varchar(50) NOT NULL COMMENT '属性码',
  `attr_value` varchar(1024) DEFAULT NULL COMMENT '属性值',
  `attr_desc` varchar(1024) DEFAULT NULL COMMENT '属性描述',
  `state` tinyint(4) NOT NULL COMMENT '属性状态(0: 删除 1: 正常）',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `done_date` datetime DEFAULT NULL COMMENT '修改日期',
  `effective_date` datetime DEFAULT NULL COMMENT '生效日期',
  `expiry_date` datetime DEFAULT NULL COMMENT '失效日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`attr_id`),
  KEY `INDEX_FILE_ID` (`file_id`) USING BTREE,
  KEY `INDEX_ATTR_CODE` (`attr_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flea_file_attr
-- ----------------------------

-- ----------------------------
-- Table structure for `flea_file_category`
-- ----------------------------
DROP TABLE IF EXISTS `flea_file_category`;
CREATE TABLE `flea_file_category` (
  `category_id` int(10) NOT NULL COMMENT '类目编号',
  `category_code` varchar(32) NOT NULL COMMENT '类目编码',
  `category_name` varchar(64) DEFAULT NULL COMMENT '类目名称',
  `parent_id` int(10) NOT NULL DEFAULT '-1' COMMENT '父类目编号',
  `encrypt_type` varchar(10) NOT NULL COMMENT '文件加密方式【AES、DES、NONE(无需加密)】',
  `max_file_size` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '文件最大值【单位：MB】',
  `operation_state` varchar(16) DEFAULT NULL COMMENT '操作状态【每一位代表一个文件管理操作的状态（0：关闭 1：启用）】',
  `state` tinyint(4) NOT NULL COMMENT '类目状态(0: 删除 1: 正常）',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `done_date` datetime DEFAULT NULL COMMENT '修改日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `INDEX_CATEGORY_CODE` (`category_code`) USING BTREE,
  KEY `INDEX_PARENT_ID` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flea_file_category
-- ----------------------------

-- ----------------------------
-- Table structure for `flea_file_category_attr`
-- ----------------------------
DROP TABLE IF EXISTS `flea_file_category_attr`;
CREATE TABLE `flea_file_category_attr` (
  `attr_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性编号',
  `category_id` int(10) NOT NULL COMMENT '类目编号',
  `attr_code` varchar(50) NOT NULL COMMENT '属性码',
  `attr_value` varchar(1024) DEFAULT NULL COMMENT '属性值',
  `attr_desc` varchar(1024) DEFAULT NULL COMMENT '属性描述',
  `state` tinyint(4) NOT NULL COMMENT '属性状态(0: 删除 1: 正常）',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `done_date` datetime DEFAULT NULL COMMENT '修改日期',
  `effective_date` datetime DEFAULT NULL COMMENT '生效日期',
  `expiry_date` datetime DEFAULT NULL COMMENT '失效日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`attr_id`),
  KEY `INDEX_CATEGORY_ID` (`category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flea_file_category_attr
-- ----------------------------

-- ----------------------------
-- Table structure for `flea_file_info`
-- ----------------------------
DROP TABLE IF EXISTS `flea_file_info`;
CREATE TABLE `flea_file_info` (
  `file_id` varchar(64) NOT NULL COMMENT '文件编号',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `file_type` varchar(10) NOT NULL COMMENT '文件类型',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小【单位：B】',
  `file_size_desc` varchar(20) DEFAULT NULL COMMENT '文件大小描述',
  `file_version_id` int(11) NOT NULL COMMENT '文件版本编号',
  `file_state` tinyint(2) NOT NULL COMMENT '文件状态【0: 待上传 1：待审核 2：使用中 3：审核不通过 4：删除】',
  `fastdfs_id` varchar(150) DEFAULT NULL,
  `secret_key` varchar(50) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `system_id` int(11) DEFAULT NULL,
  `state` tinyint(4) NOT NULL COMMENT '文件记录状态(0: 删除 1: 正常）',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `done_date` datetime DEFAULT NULL COMMENT '修改日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flea_file_info
-- ----------------------------

-- ----------------------------
-- Table structure for `flea_file_version`
-- ----------------------------
DROP TABLE IF EXISTS `flea_file_version`;
CREATE TABLE `flea_file_version` (
  `version_id` int(11) NOT NULL COMMENT '版本编号',
  `version_code` varchar(20) NOT NULL COMMENT '版本编码',
  `version_name` varchar(50) NOT NULL COMMENT '版本名称',
  `version_desc` varchar(255) DEFAULT NULL COMMENT '版本描述',
  `file_id` varchar(64) NOT NULL COMMENT '文件编号',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `file_type` varchar(10) NOT NULL COMMENT '文件类型',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小【单位：B】',
  `file_size_desc` varchar(20) DEFAULT NULL COMMENT '文件大小描述',
  `file_state` tinyint(2) NOT NULL COMMENT '文件状态【0: 待上传 1：待审核 2：使用中 3：审核不通过 4：删除】',
  `fastdfs_id` varchar(150) DEFAULT NULL,
  `secret_key` varchar(50) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `system_id` int(11) DEFAULT NULL,
  `state` tinyint(4) NOT NULL COMMENT '文件记录状态(0: 删除 1: 正常）',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `done_date` datetime DEFAULT NULL COMMENT '修改日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`version_id`),
  UNIQUE KEY `INDEX_FASTDFS_ID` (`fastdfs_id`) USING BTREE,
  KEY `INDEX_FILE_ID` (`file_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flea_file_version
-- ----------------------------

-- ----------------------------
-- Table structure for `flea_token_info`
-- ----------------------------
DROP TABLE IF EXISTS `flea_token_info`;
CREATE TABLE `flea_token_info` (
  `token_id` varchar(48) NOT NULL COMMENT '鉴权令牌',
  `file_id` varchar(48) NOT NULL COMMENT '文件编号',
  `operation_type` varchar(16) NOT NULL COMMENT '操作类型',
  `user_id` int(11) DEFAULT NULL COMMENT '用户编号',
  `system_user_id` int(11) DEFAULT NULL COMMENT '系统用户编号',
  `state` tinyint(4) NOT NULL COMMENT '文件记录状态(0: 删除 1: 正常）',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `done_date` datetime DEFAULT NULL COMMENT '修改日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`token_id`),
  KEY `INDEX_FILE_ID` (`file_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flea_token_info
-- ----------------------------
