/*
 Navicat Premium Data Transfer

 Source Server         : 60mysql
 Source Server Type    : MySQL
 Source Server Version : 50637
 Source Host           : 192.168.10.60:3306
 Source Schema         : wyait

 Target Server Type    : MySQL
 Target Server Version : 50637
 File Encoding         : 65001

 Date: 07/08/2020 14:01:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父菜单id',
  `zindex` int(2) NULL DEFAULT NULL COMMENT '菜单排序',
  `istype` int(1) NULL DEFAULT NULL COMMENT '权限分类（0 菜单；1 功能）',
  `descpt` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单编号',
  `icon` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标名称',
  `page` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单url',
  `insert_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '系统管理', 0, 100, 0, '系统管理', 'system', '', '/', '2020-08-05 16:22:43', '2020-08-05 20:58:34');
INSERT INTO `permission` VALUES (2, '用户管理', 1, 1100, 0, '用户管理', 'usermanage', '', '/user/userList', '2020-08-05 16:27:03', '2020-08-05 20:58:34');
INSERT INTO `permission` VALUES (3, '角色管理', 1, 1200, 0, '角色管理', 'rolemanage', '', '/auth/roleManage', '2020-08-05 16:27:03', '2020-08-05 20:58:34');
INSERT INTO `permission` VALUES (4, '权限管理', 1, 1300, 0, '权限管理', 'permmanage', NULL, '/auth/permList', '2020-08-05 19:17:32', '2020-08-05 20:58:34');
INSERT INTO `permission` VALUES (6, '父菜单', 0, 200, 0, '父菜单', 'fatherMenu', NULL, '/', '2020-08-05 11:07:17', '2020-08-05 20:58:34');
INSERT INTO `permission` VALUES (22, '子菜单1', 6, 2100, 0, '子菜单1', 'menuOne', NULL, '/', '2020-08-05 20:50:42', '2020-08-05 20:58:34');
INSERT INTO `permission` VALUES (23, '子菜单2', 6, 2200, 0, '子菜单2', 'menuTwo', NULL, '/', '2020-08-05 20:58:34', '2020-08-05 20:58:34');
INSERT INTO `permission` VALUES (25, '开通用户', 2, 11100, 1, '开通用户', 'setUserPermission', NULL, '/user/setUser', '2020-08-06 17:48:08', NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `descpt` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编号',
  `insert_uid` int(11) NULL DEFAULT NULL COMMENT '操作用户id',
  `insert_time` datetime(0) NULL DEFAULT NULL COMMENT '添加数据时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理', '超级管理员', 'superman', NULL, '2020-08-05 20:58:34', '2020-08-06 17:48:14');
INSERT INTO `role` VALUES (2, '高级管理员', '高级管理员', 'highmanage', NULL, '2020-08-05 20:58:34', '2020-08-05 20:58:34');
INSERT INTO `role` VALUES (3, '经理', '经理', 'bdmanage', NULL, '2020-08-05 20:58:34', '2020-08-05 20:58:34');
INSERT INTO `role` VALUES (7, '测试', '测试', 'test', NULL, '2020-08-06 13:43:24', NULL);

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `permit_id` int(5) NOT NULL AUTO_INCREMENT,
  `role_id` int(5) NOT NULL,
  PRIMARY KEY (`permit_id`, `role_id`) USING BTREE,
  INDEX `perimitid`(`permit_id`) USING BTREE,
  INDEX `roleid`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 1);
INSERT INTO `role_permission` VALUES (1, 2);
INSERT INTO `role_permission` VALUES (1, 3);
INSERT INTO `role_permission` VALUES (1, 7);
INSERT INTO `role_permission` VALUES (2, 1);
INSERT INTO `role_permission` VALUES (2, 2);
INSERT INTO `role_permission` VALUES (2, 3);
INSERT INTO `role_permission` VALUES (2, 7);
INSERT INTO `role_permission` VALUES (3, 1);
INSERT INTO `role_permission` VALUES (3, 2);
INSERT INTO `role_permission` VALUES (4, 1);
INSERT INTO `role_permission` VALUES (5, 2);
INSERT INTO `role_permission` VALUES (6, 1);
INSERT INTO `role_permission` VALUES (6, 2);
INSERT INTO `role_permission` VALUES (8, 2);
INSERT INTO `role_permission` VALUES (10, 2);
INSERT INTO `role_permission` VALUES (11, 2);
INSERT INTO `role_permission` VALUES (12, 2);
INSERT INTO `role_permission` VALUES (13, 2);
INSERT INTO `role_permission` VALUES (14, 2);
INSERT INTO `role_permission` VALUES (22, 1);
INSERT INTO `role_permission` VALUES (23, 1);
INSERT INTO `role_permission` VALUES (25, 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户名',
  `mobile` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '邮箱',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
  `insert_uid` int(11) NULL DEFAULT NULL COMMENT '添加该用户的用户id',
  `insert_time` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除（0：正常；1：已删）',
  `is_job` tinyint(1) NULL DEFAULT 0 COMMENT '是否在职（0：正常；1，离职）',
  `mcode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '短信验证码',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '短信发送时间',
  `version` int(10) NULL DEFAULT 0 COMMENT '更新版本',
  `user_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定用户客户端ip等信息（用于锁定用户设备）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `name`(`username`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  INDEX `mobile`(`mobile`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'wyait', '12316596566', 'aaa211', 'c33367701511b4f6020ec61ded352059', 1, '2020-08-05 20:58:34', '2020-08-06 13:57:44', 0, 0, '645390', '2020-08-05 20:58:34', 35, '18516596566_Sogou Explorer 2.x_Personal computer_Windows_Browser');
INSERT INTO `user` VALUES (2, 'test', '10999999999', '', 'c33367701511b4f6020ec61ded352059', 1, '2020-08-05 20:58:34', '2020-08-06 17:04:05', 0, 0, '185282', '2020-08-05 20:58:34', 26, '123');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_id` int(11) NOT NULL,
  `role_id` int(5) NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `userid`(`user_id`) USING BTREE,
  INDEX `roleid`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1);
INSERT INTO `user_role` VALUES (2, 3);
INSERT INTO `user_role` VALUES (3, 6);
INSERT INTO `user_role` VALUES (4, 6);
INSERT INTO `user_role` VALUES (5, 6);
INSERT INTO `user_role` VALUES (27, 1);

SET FOREIGN_KEY_CHECKS = 1;
