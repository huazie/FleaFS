
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `flea_file_attr`
-- ----------------------------
DROP TABLE IF EXISTS `flea_file_attr`;
CREATE TABLE `flea_file_attr` (
  `attr_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性编号',
  `file_id` varchar(64) NOT NULL COMMENT '文件编号',
  `attr_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '属性码',
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
  `file_state` tinyint(2) NOT NULL COMMENT '文件状态【0: 待上传 1：待审核 2：使用中 3：审核不通过 4：逻辑删除】',
  `fastdfs_id` varchar(150) DEFAULT NULL COMMENT 'fastdfs文件编号',
  `secret_key` varchar(50) DEFAULT NULL COMMENT '密钥',
  `user_id` int(11) DEFAULT NULL COMMENT '操作用户编号',
  `system_user_id` int(11) DEFAULT NULL COMMENT '系统用户编号',
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
  `version_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '版本编号',
  `version_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '版本编码',
  `version_name` varchar(50) NOT NULL COMMENT '版本名称',
  `version_desc` varchar(255) DEFAULT NULL COMMENT '版本描述',
  `file_id` varchar(64) NOT NULL COMMENT '文件编号',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `file_type` varchar(10) NOT NULL COMMENT '文件类型',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小【单位：B】',
  `file_size_desc` varchar(20) DEFAULT NULL COMMENT '文件大小描述',
  `file_state` tinyint(2) NOT NULL COMMENT '文件状态【0: 待上传 1：待审核 2：使用中 3：审核不通过 4：逻辑删除】',
  `fastdfs_id` varchar(150) DEFAULT NULL COMMENT 'fastdfs文件编号',
  `secret_key` varchar(50) DEFAULT NULL COMMENT '密钥',
  `user_id` int(11) DEFAULT NULL COMMENT '操作用户编号',
  `system_user_id` int(11) DEFAULT NULL COMMENT '系统用户编号',
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
  `category_id` int(10) NOT NULL COMMENT '类目编号',
  `operation_type` varchar(16) NOT NULL COMMENT '操作类型',
  `state` tinyint(4) NOT NULL COMMENT '文件记录状态(0: 删除 1: 正常）',
  `user_id` int(11) DEFAULT NULL COMMENT '操作用户编号',
  `system_user_id` int(11) DEFAULT NULL COMMENT '系统用户编号',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `done_date` datetime DEFAULT NULL COMMENT '修改日期',
  `effective_date` datetime NOT NULL COMMENT '生效日期',
  `expiry_date` datetime NOT NULL COMMENT '失效日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`token_id`),
  KEY `INDEX_FILE_ID` (`file_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DELIMITER $$

DROP PROCEDURE IF EXISTS create_sharding_tables$$
CREATE PROCEDURE create_sharding_tables(IN target_center INT)
BEGIN
  DECLARE done INT DEFAULT 0;
  DECLARE current_base_table VARCHAR(255);
  DECLARE current_num INT;

  -- 声明游标获取所有需要创建的分表的base_table和num
  DECLARE cur CURSOR FOR
    SELECT
      base_table,
      num
    FROM (
           SELECT
             base_table,
             (a.d << 4) + b.d AS num
           FROM
             (SELECT 0 d UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
              UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
              UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15) a,
             (SELECT 0 d UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
              UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
              UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15) b,
             (SELECT 'flea_file_attr' AS base_table UNION ALL
              SELECT 'flea_file_info' UNION ALL
              SELECT 'flea_file_version' UNION ALL
              SELECT 'flea_token_info') tables
         ) data
    WHERE
      CASE target_center
        WHEN 1 THEN RIGHT(HEX(num), 1) IN ('0','4','8','C')
        WHEN 2 THEN RIGHT(HEX(num), 1) IN ('1','5','9','D')
        WHEN 3 THEN RIGHT(HEX(num), 1) IN ('2','6','A','E')
        WHEN 4 THEN RIGHT(HEX(num), 1) IN ('3','7','B','F')
        END;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

  OPEN cur;

  read_loop: LOOP
    FETCH cur INTO current_base_table, current_num;
    IF done THEN
      LEAVE read_loop;
    END IF;

    -- 动态执行DROP TABLE语句（添加删除逻辑）
    SET @drop_sql = CONCAT('DROP TABLE IF EXISTS ', current_base_table, '_', LPAD(HEX(current_num), 2, '0'), ';');
    PREPARE drop_stmt FROM @drop_sql;
    EXECUTE drop_stmt;
    DEALLOCATE PREPARE drop_stmt;

    -- 动态执行CREATE TABLE语句
    SET @create_sql = CONCAT('CREATE TABLE ', current_base_table, '_', LPAD(HEX(current_num), 2, '0'), ' LIKE ', current_base_table, ';');
    PREPARE create_stmt FROM @create_sql;
    EXECUTE create_stmt;
    DEALLOCATE PREPARE create_stmt;
  END LOOP;

  CLOSE cur;
END$$

DELIMITER ;

-- 执行示例（创建中心2的分表）
CALL create_sharding_tables(2);