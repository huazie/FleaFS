/*
FleaFS 配置库【fleafsconfig】配置数据脚本

说明：
    1. fleafsconfig 为 Flea 框架配置库，库中表结构（如 flea_config_data、flea_jersey_resource、
       flea_jersey_res_service、flea_jersey_res_client 等）由框架提供，
       建表脚本详见 flea-framework 的 flea-core/fleaconfig.sql，如尚未建库建表请先执行该脚本。
    2. 本脚本只包含 FleaFS 业务所需的配置数据，可重复执行（INSERT ... ON DUPLICATE KEY UPDATE）；
       其中 flea_config_data 无业务唯一键，采用 WHERE NOT EXISTS 保证不重复插入。
    3. 本脚本中的资源、服务、客户端数据变更后，需清理 Flea 缓存或重启应用方可生效。
    4. FleaFS 业务国际码和错误码映射（ERROR-SERVICE0000000000 ~ 0013）统一在
       fleafs-config 模块的 flea/jersey/fleafs-jersey-filter.xml 中配置，
       故配置库表 flea_jersey_i18n_error_mapping 无需 FleaFS 记录。

Target Server Type    : MYSQL
File Encoding         : 65001
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- 1. FleaFS 业务配置数据【flea_config_data】
-- ----------------------------
-- TOKEN失效分钟数配置（未配置时，代码默认 5 分钟）
INSERT INTO `flea_config_data` (`config_type`, `config_code`, `config_name`, `config_desc`, `config_state`, `data1`)
SELECT 'TOKEN_EXPIRY_CONFIG', 'TOKEN_EXPIRY_CONFIG', 'TOKEN失效配置', 'FleaFS鉴权令牌失效分钟数【数据1：分钟数】', 1, '5'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `flea_config_data`
                  WHERE `config_type` = 'TOKEN_EXPIRY_CONFIG' AND `config_code` = 'TOKEN_EXPIRY_CONFIG');

-- ----------------------------
-- 2. Flea Jersey 资源定义【flea_jersey_resource】
-- ----------------------------
INSERT INTO `flea_jersey_resource` (`resource_code`, `resource_name`, `resource_packages`, `state`, `create_date`, `remarks`)
VALUES ('upload', '上传资源', 'com.huazie.ffs.module.upload.web', 1, NOW(), '文件上传资源')
ON DUPLICATE KEY UPDATE `resource_name` = VALUES(`resource_name`), `resource_packages` = VALUES(`resource_packages`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_resource` (`resource_code`, `resource_name`, `resource_packages`, `state`, `create_date`, `remarks`)
VALUES ('download', '下载资源', 'com.huazie.ffs.module.download.web', 1, NOW(), '文件下载资源')
ON DUPLICATE KEY UPDATE `resource_name` = VALUES(`resource_name`), `resource_packages` = VALUES(`resource_packages`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_resource` (`resource_code`, `resource_name`, `resource_packages`, `state`, `create_date`, `remarks`)
VALUES ('update', '更新资源', 'com.huazie.ffs.module.update.web', 1, NOW(), '文件更新资源')
ON DUPLICATE KEY UPDATE `resource_name` = VALUES(`resource_name`), `resource_packages` = VALUES(`resource_packages`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_resource` (`resource_code`, `resource_name`, `resource_packages`, `state`, `create_date`, `remarks`)
VALUES ('delete', '删除资源', 'com.huazie.ffs.module.delete.web', 1, NOW(), '文件删除资源')
ON DUPLICATE KEY UPDATE `resource_name` = VALUES(`resource_name`), `resource_packages` = VALUES(`resource_packages`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_resource` (`resource_code`, `resource_name`, `resource_packages`, `state`, `create_date`, `remarks`)
VALUES ('version', '版本资源', 'com.huazie.ffs.module.version.web', 1, NOW(), '文件版本查询资源')
ON DUPLICATE KEY UPDATE `resource_name` = VALUES(`resource_name`), `resource_packages` = VALUES(`resource_packages`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_resource` (`resource_code`, `resource_name`, `resource_packages`, `state`, `create_date`, `remarks`)
VALUES ('search', '搜索资源', 'com.huazie.ffs.module.search.web', 1, NOW(), '文件搜索资源')
ON DUPLICATE KEY UPDATE `resource_name` = VALUES(`resource_name`), `resource_packages` = VALUES(`resource_packages`), `state` = VALUES(`state`);

-- ----------------------------
-- 3. Flea Jersey 服务定义【flea_jersey_res_service】
-- ----------------------------
INSERT INTO `flea_jersey_res_service` (`service_code`, `resource_code`, `service_name`, `service_interfaces`, `service_method`, `service_input`, `service_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_SERVICE_UPLOAD_AUTH', 'upload', '上传鉴权服务', 'com.huazie.ffs.module.upload.service.interfaces.IFleaUploadSV', 'uploadAuth', 'com.huazie.ffs.pojo.upload.input.InputUploadAuthInfo', 'com.huazie.ffs.pojo.upload.output.OutputUploadAuthInfo', 1, NOW(), '上传鉴权服务')
ON DUPLICATE KEY UPDATE `resource_code` = VALUES(`resource_code`), `service_name` = VALUES(`service_name`), `service_interfaces` = VALUES(`service_interfaces`), `service_method` = VALUES(`service_method`), `service_input` = VALUES(`service_input`), `service_output` = VALUES(`service_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_service` (`service_code`, `resource_code`, `service_name`, `service_interfaces`, `service_method`, `service_input`, `service_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_SERVICE_FILE_UPLOAD', 'upload', '文件上传服务', 'com.huazie.ffs.module.upload.service.interfaces.IFleaUploadSV', 'fileUpload', 'com.huazie.ffs.pojo.upload.input.InputFileUploadInfo', 'com.huazie.ffs.pojo.upload.output.OutputFileUploadInfo', 1, NOW(), '文件上传服务')
ON DUPLICATE KEY UPDATE `resource_code` = VALUES(`resource_code`), `service_name` = VALUES(`service_name`), `service_interfaces` = VALUES(`service_interfaces`), `service_method` = VALUES(`service_method`), `service_input` = VALUES(`service_input`), `service_output` = VALUES(`service_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_service` (`service_code`, `resource_code`, `service_name`, `service_interfaces`, `service_method`, `service_input`, `service_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_SERVICE_DOWNLOAD_AUTH', 'download', '下载鉴权服务', 'com.huazie.ffs.module.download.service.interfaces.IFleaDownloadSV', 'downloadAuth', 'com.huazie.ffs.pojo.download.input.InputDownloadAuthInfo', 'com.huazie.ffs.pojo.download.output.OutputDownloadAuthInfo', 1, NOW(), '下载鉴权服务')
ON DUPLICATE KEY UPDATE `resource_code` = VALUES(`resource_code`), `service_name` = VALUES(`service_name`), `service_interfaces` = VALUES(`service_interfaces`), `service_method` = VALUES(`service_method`), `service_input` = VALUES(`service_input`), `service_output` = VALUES(`service_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_service` (`service_code`, `resource_code`, `service_name`, `service_interfaces`, `service_method`, `service_input`, `service_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_SERVICE_FILE_DOWNLOAD', 'download', '文件下载服务', 'com.huazie.ffs.module.download.service.interfaces.IFleaDownloadSV', 'fileDownload', 'com.huazie.ffs.pojo.download.input.InputFileDownloadInfo', 'com.huazie.ffs.pojo.download.output.OutputFileDownloadInfo', 1, NOW(), '文件下载服务')
ON DUPLICATE KEY UPDATE `resource_code` = VALUES(`resource_code`), `service_name` = VALUES(`service_name`), `service_interfaces` = VALUES(`service_interfaces`), `service_method` = VALUES(`service_method`), `service_input` = VALUES(`service_input`), `service_output` = VALUES(`service_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_service` (`service_code`, `resource_code`, `service_name`, `service_interfaces`, `service_method`, `service_input`, `service_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_SERVICE_UPDATE_AUTH', 'update', '更新鉴权服务', 'com.huazie.ffs.module.update.service.interfaces.IFleaUpdateSV', 'updateAuth', 'com.huazie.ffs.pojo.update.input.InputUpdateAuthInfo', 'com.huazie.ffs.pojo.update.output.OutputUpdateAuthInfo', 1, NOW(), '更新鉴权服务')
ON DUPLICATE KEY UPDATE `resource_code` = VALUES(`resource_code`), `service_name` = VALUES(`service_name`), `service_interfaces` = VALUES(`service_interfaces`), `service_method` = VALUES(`service_method`), `service_input` = VALUES(`service_input`), `service_output` = VALUES(`service_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_service` (`service_code`, `resource_code`, `service_name`, `service_interfaces`, `service_method`, `service_input`, `service_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_SERVICE_FILE_UPDATE', 'update', '文件更新服务', 'com.huazie.ffs.module.update.service.interfaces.IFleaUpdateSV', 'fileUpdate', 'com.huazie.ffs.pojo.update.input.InputFileUpdateInfo', 'com.huazie.ffs.pojo.update.output.OutputFileUpdateInfo', 1, NOW(), '文件更新服务')
ON DUPLICATE KEY UPDATE `resource_code` = VALUES(`resource_code`), `service_name` = VALUES(`service_name`), `service_interfaces` = VALUES(`service_interfaces`), `service_method` = VALUES(`service_method`), `service_input` = VALUES(`service_input`), `service_output` = VALUES(`service_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_service` (`service_code`, `resource_code`, `service_name`, `service_interfaces`, `service_method`, `service_input`, `service_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_SERVICE_DELETE_AUTH', 'delete', '删除鉴权服务', 'com.huazie.ffs.module.delete.service.interfaces.IFleaDeleteSV', 'deleteAuth', 'com.huazie.ffs.pojo.delete.input.InputDeleteAuthInfo', 'com.huazie.ffs.pojo.delete.output.OutputDeleteAuthInfo', 1, NOW(), '删除鉴权服务')
ON DUPLICATE KEY UPDATE `resource_code` = VALUES(`resource_code`), `service_name` = VALUES(`service_name`), `service_interfaces` = VALUES(`service_interfaces`), `service_method` = VALUES(`service_method`), `service_input` = VALUES(`service_input`), `service_output` = VALUES(`service_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_service` (`service_code`, `resource_code`, `service_name`, `service_interfaces`, `service_method`, `service_input`, `service_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_SERVICE_FILE_DELETE', 'delete', '文件删除服务', 'com.huazie.ffs.module.delete.service.interfaces.IFleaDeleteSV', 'fileDelete', 'com.huazie.ffs.pojo.delete.input.InputFileDeleteInfo', 'com.huazie.ffs.pojo.delete.output.OutputFileDeleteInfo', 1, NOW(), '文件逻辑删除服务')
ON DUPLICATE KEY UPDATE `resource_code` = VALUES(`resource_code`), `service_name` = VALUES(`service_name`), `service_interfaces` = VALUES(`service_interfaces`), `service_method` = VALUES(`service_method`), `service_input` = VALUES(`service_input`), `service_output` = VALUES(`service_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_service` (`service_code`, `resource_code`, `service_name`, `service_interfaces`, `service_method`, `service_input`, `service_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_SERVICE_FILE_VERSION', 'version', '文件版本查询服务', 'com.huazie.ffs.module.version.service.interfaces.IFleaVersionSV', 'queryFileVersions', 'com.huazie.ffs.pojo.version.input.InputFileVersionInfo', 'com.huazie.ffs.pojo.version.output.OutputFileVersionInfo', 1, NOW(), '文件版本查询服务')
ON DUPLICATE KEY UPDATE `resource_code` = VALUES(`resource_code`), `service_name` = VALUES(`service_name`), `service_interfaces` = VALUES(`service_interfaces`), `service_method` = VALUES(`service_method`), `service_input` = VALUES(`service_input`), `service_output` = VALUES(`service_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_service` (`service_code`, `resource_code`, `service_name`, `service_interfaces`, `service_method`, `service_input`, `service_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_SERVICE_FILE_SEARCH', 'search', '文件搜索服务', 'com.huazie.ffs.module.search.service.interfaces.IFleaSearchSV', 'fileSearch', 'com.huazie.ffs.pojo.search.input.InputFileSearchInfo', 'com.huazie.ffs.pojo.search.output.OutputFileSearchInfo', 1, NOW(), '文件搜索服务')
ON DUPLICATE KEY UPDATE `resource_code` = VALUES(`resource_code`), `service_name` = VALUES(`service_name`), `service_interfaces` = VALUES(`service_interfaces`), `service_method` = VALUES(`service_method`), `service_input` = VALUES(`service_input`), `service_output` = VALUES(`service_output`), `state` = VALUES(`state`);

-- ----------------------------
-- 4. Flea Jersey 资源客户端定义【flea_jersey_res_client】
-- ----------------------------
INSERT INTO `flea_jersey_res_client` (`client_code`, `resource_url`, `resource_code`, `service_code`, `request_mode`, `media_type`, `client_input`, `client_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_CLIENT_UPLOAD_AUTH', 'http://localhost:8080/fleafs', 'upload', 'FLEA_SERVICE_UPLOAD_AUTH', 'post', 'application/xml', 'com.huazie.ffs.pojo.upload.input.InputUploadAuthInfo', 'com.huazie.ffs.pojo.upload.output.OutputUploadAuthInfo', 1, NOW(), '上传鉴权服务')
ON DUPLICATE KEY UPDATE `resource_url` = VALUES(`resource_url`), `resource_code` = VALUES(`resource_code`), `service_code` = VALUES(`service_code`), `request_mode` = VALUES(`request_mode`), `media_type` = VALUES(`media_type`), `client_input` = VALUES(`client_input`), `client_output` = VALUES(`client_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_client` (`client_code`, `resource_url`, `resource_code`, `service_code`, `request_mode`, `media_type`, `client_input`, `client_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_CLIENT_FILE_UPLOAD', 'http://localhost:8080/fleafs', 'upload', 'FLEA_SERVICE_FILE_UPLOAD', 'fpost', 'multipart/form-data', 'com.huazie.ffs.pojo.upload.input.InputFileUploadInfo', 'com.huazie.ffs.pojo.upload.output.OutputFileUploadInfo', 1, NOW(), '文件上传服务')
ON DUPLICATE KEY UPDATE `resource_url` = VALUES(`resource_url`), `resource_code` = VALUES(`resource_code`), `service_code` = VALUES(`service_code`), `request_mode` = VALUES(`request_mode`), `media_type` = VALUES(`media_type`), `client_input` = VALUES(`client_input`), `client_output` = VALUES(`client_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_client` (`client_code`, `resource_url`, `resource_code`, `service_code`, `request_mode`, `media_type`, `client_input`, `client_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_CLIENT_DOWNLOAD_AUTH', 'http://localhost:8080/fleafs', 'download', 'FLEA_SERVICE_DOWNLOAD_AUTH', 'post', 'application/xml', 'com.huazie.ffs.pojo.download.input.InputDownloadAuthInfo', 'com.huazie.ffs.pojo.download.output.OutputDownloadAuthInfo', 1, NOW(), '下载鉴权服务')
ON DUPLICATE KEY UPDATE `resource_url` = VALUES(`resource_url`), `resource_code` = VALUES(`resource_code`), `service_code` = VALUES(`service_code`), `request_mode` = VALUES(`request_mode`), `media_type` = VALUES(`media_type`), `client_input` = VALUES(`client_input`), `client_output` = VALUES(`client_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_client` (`client_code`, `resource_url`, `resource_code`, `service_code`, `request_mode`, `media_type`, `client_input`, `client_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_CLIENT_FILE_DOWNLOAD', 'http://localhost:8080/fleafs', 'download', 'FLEA_SERVICE_FILE_DOWNLOAD', 'fget', 'multipart/form-data', 'com.huazie.ffs.pojo.download.input.InputFileDownloadInfo', 'com.huazie.ffs.pojo.download.output.OutputFileDownloadInfo', 1, NOW(), '文件下载服务')
ON DUPLICATE KEY UPDATE `resource_url` = VALUES(`resource_url`), `resource_code` = VALUES(`resource_code`), `service_code` = VALUES(`service_code`), `request_mode` = VALUES(`request_mode`), `media_type` = VALUES(`media_type`), `client_input` = VALUES(`client_input`), `client_output` = VALUES(`client_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_client` (`client_code`, `resource_url`, `resource_code`, `service_code`, `request_mode`, `media_type`, `client_input`, `client_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_CLIENT_UPDATE_AUTH', 'http://localhost:8080/fleafs', 'update', 'FLEA_SERVICE_UPDATE_AUTH', 'post', 'application/xml', 'com.huazie.ffs.pojo.update.input.InputUpdateAuthInfo', 'com.huazie.ffs.pojo.update.output.OutputUpdateAuthInfo', 1, NOW(), '更新鉴权服务')
ON DUPLICATE KEY UPDATE `resource_url` = VALUES(`resource_url`), `resource_code` = VALUES(`resource_code`), `service_code` = VALUES(`service_code`), `request_mode` = VALUES(`request_mode`), `media_type` = VALUES(`media_type`), `client_input` = VALUES(`client_input`), `client_output` = VALUES(`client_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_client` (`client_code`, `resource_url`, `resource_code`, `service_code`, `request_mode`, `media_type`, `client_input`, `client_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_CLIENT_FILE_UPDATE', 'http://localhost:8080/fleafs', 'update', 'FLEA_SERVICE_FILE_UPDATE', 'fpost', 'multipart/form-data', 'com.huazie.ffs.pojo.update.input.InputFileUpdateInfo', 'com.huazie.ffs.pojo.update.output.OutputFileUpdateInfo', 1, NOW(), '文件更新服务')
ON DUPLICATE KEY UPDATE `resource_url` = VALUES(`resource_url`), `resource_code` = VALUES(`resource_code`), `service_code` = VALUES(`service_code`), `request_mode` = VALUES(`request_mode`), `media_type` = VALUES(`media_type`), `client_input` = VALUES(`client_input`), `client_output` = VALUES(`client_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_client` (`client_code`, `resource_url`, `resource_code`, `service_code`, `request_mode`, `media_type`, `client_input`, `client_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_CLIENT_DELETE_AUTH', 'http://localhost:8080/fleafs', 'delete', 'FLEA_SERVICE_DELETE_AUTH', 'post', 'application/xml', 'com.huazie.ffs.pojo.delete.input.InputDeleteAuthInfo', 'com.huazie.ffs.pojo.delete.output.OutputDeleteAuthInfo', 1, NOW(), '删除鉴权服务')
ON DUPLICATE KEY UPDATE `resource_url` = VALUES(`resource_url`), `resource_code` = VALUES(`resource_code`), `service_code` = VALUES(`service_code`), `request_mode` = VALUES(`request_mode`), `media_type` = VALUES(`media_type`), `client_input` = VALUES(`client_input`), `client_output` = VALUES(`client_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_client` (`client_code`, `resource_url`, `resource_code`, `service_code`, `request_mode`, `media_type`, `client_input`, `client_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_CLIENT_FILE_DELETE', 'http://localhost:8080/fleafs', 'delete', 'FLEA_SERVICE_FILE_DELETE', 'post', 'application/xml', 'com.huazie.ffs.pojo.delete.input.InputFileDeleteInfo', 'com.huazie.ffs.pojo.delete.output.OutputFileDeleteInfo', 1, NOW(), '文件逻辑删除服务')
ON DUPLICATE KEY UPDATE `resource_url` = VALUES(`resource_url`), `resource_code` = VALUES(`resource_code`), `service_code` = VALUES(`service_code`), `request_mode` = VALUES(`request_mode`), `media_type` = VALUES(`media_type`), `client_input` = VALUES(`client_input`), `client_output` = VALUES(`client_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_client` (`client_code`, `resource_url`, `resource_code`, `service_code`, `request_mode`, `media_type`, `client_input`, `client_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_CLIENT_FILE_VERSION', 'http://localhost:8080/fleafs', 'version', 'FLEA_SERVICE_FILE_VERSION', 'post', 'application/xml', 'com.huazie.ffs.pojo.version.input.InputFileVersionInfo', 'com.huazie.ffs.pojo.version.output.OutputFileVersionInfo', 1, NOW(), '文件版本查询服务')
ON DUPLICATE KEY UPDATE `resource_url` = VALUES(`resource_url`), `resource_code` = VALUES(`resource_code`), `service_code` = VALUES(`service_code`), `request_mode` = VALUES(`request_mode`), `media_type` = VALUES(`media_type`), `client_input` = VALUES(`client_input`), `client_output` = VALUES(`client_output`), `state` = VALUES(`state`);

INSERT INTO `flea_jersey_res_client` (`client_code`, `resource_url`, `resource_code`, `service_code`, `request_mode`, `media_type`, `client_input`, `client_output`, `state`, `create_date`, `remarks`)
VALUES ('FLEA_CLIENT_FILE_SEARCH', 'http://localhost:8080/fleafs', 'search', 'FLEA_SERVICE_FILE_SEARCH', 'post', 'application/xml', 'com.huazie.ffs.pojo.search.input.InputFileSearchInfo', 'com.huazie.ffs.pojo.search.output.OutputFileSearchInfo', 1, NOW(), '文件搜索服务')
ON DUPLICATE KEY UPDATE `resource_url` = VALUES(`resource_url`), `resource_code` = VALUES(`resource_code`), `service_code` = VALUES(`service_code`), `request_mode` = VALUES(`request_mode`), `media_type` = VALUES(`media_type`), `client_input` = VALUES(`client_input`), `client_output` = VALUES(`client_output`), `state` = VALUES(`state`);
