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

 Date: 29/03/2021 20:05:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_group
-- ----------------------------
DROP TABLE IF EXISTS `message_group`;
CREATE TABLE `message_group`  (
  `id` int NOT NULL,
  `group_id` int NULL DEFAULT NULL,
  `group_owner_id` int NULL DEFAULT NULL,
  `group_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `group_manager_id` int NULL DEFAULT NULL,
  `group_sign` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `create_time` date NULL DEFAULT NULL,
  `update_time` date NULL DEFAULT NULL,
  `group_photo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message_group
-- ----------------------------
INSERT INTO `message_group` VALUES (1, 1024, 123456789, '芜湖', 987654321, '起飞', '2021-03-29', '2021-03-29', 'https://server-aliyun-images.oss-cn-shanghai.aliyuncs.com/2021-02-16/4556c5048d4c4ef5a92354d7246b42f3.jpg');

SET FOREIGN_KEY_CHECKS = 1;
