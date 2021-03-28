/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : im_tables

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 28/03/2021 21:46:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_group_chat
-- ----------------------------
DROP TABLE IF EXISTS `message_group_chat`;
CREATE TABLE `message_group_chat`  (
  `id` int(10) NOT NULL,
  `group_id` int(10) NULL DEFAULT NULL,
  `sender_id` int(10) NULL DEFAULT NULL,
  `message_type` int(255) NULL DEFAULT NULL,
  `message_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `create_time` date NULL DEFAULT NULL,
  `update_time` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_group_chat
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
