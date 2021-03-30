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

 Date: 30/03/2021 23:38:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_queue_list
-- ----------------------------
DROP TABLE IF EXISTS `message_queue_list`;
CREATE TABLE `message_queue_list`  (
  `message_id` int(255) NOT NULL,
  `is_consumption` int(255) NULL DEFAULT NULL,
  `message_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `create_time` date NULL DEFAULT NULL,
  `update_time` date NULL DEFAULT NULL,
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_queue_list
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
