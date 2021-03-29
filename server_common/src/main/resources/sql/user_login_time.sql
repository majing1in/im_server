/*
 Navicat Premium Data Transfer

 Source Server         : localhost_6000
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:6000
 Source Schema         : im_tables

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 29/03/2021 20:06:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_login_time
-- ----------------------------
DROP TABLE IF EXISTS `user_login_time`;
CREATE TABLE `user_login_time`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户登录记录ID',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `user_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户登录ip',
  `login_time` date NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_login_time
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
