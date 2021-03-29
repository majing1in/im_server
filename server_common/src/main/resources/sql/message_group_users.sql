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

 Date: 29/03/2021 20:05:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_group_users
-- ----------------------------
DROP TABLE IF EXISTS `message_group_users`;
CREATE TABLE `message_group_users`  (
  `id` int NOT NULL,
  `group_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `create_time` date NULL DEFAULT NULL,
  `update_time` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message_group_users
-- ----------------------------
INSERT INTO `message_group_users` VALUES (1, 1024, 1, '2021-03-29', '2021-03-29');
INSERT INTO `message_group_users` VALUES (2, 1024, 2, '2021-03-29', '2021-03-29');

SET FOREIGN_KEY_CHECKS = 1;
