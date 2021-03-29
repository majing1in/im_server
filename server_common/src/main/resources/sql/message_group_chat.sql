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

 Date: 29/03/2021 20:05:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_group_chat
-- ----------------------------
DROP TABLE IF EXISTS `message_group_chat`;
CREATE TABLE `message_group_chat`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_id` int NULL DEFAULT NULL,
  `sender_id` int NULL DEFAULT NULL,
  `message_type` int NULL DEFAULT NULL,
  `message_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `create_time` date NULL DEFAULT NULL,
  `update_time` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message_group_chat
-- ----------------------------
INSERT INTO `message_group_chat` VALUES (1, 1024, 1, 15, 'dc471cb543bd4262886fe207e86fa773', '2021-03-29', NULL);
INSERT INTO `message_group_chat` VALUES (2, 1024, 1, 15, 'd889fb7ecdc84dbc862254e237889bac', '2021-03-29', NULL);
INSERT INTO `message_group_chat` VALUES (3, 1024, 1, 15, 'ee698b43832a451a9b06db2cc2bfd3aa', '2021-03-29', NULL);
INSERT INTO `message_group_chat` VALUES (4, 1024, 1, 15, 'cc39cd945cc74edfbdf7c80e1e313daf', '2021-03-29', NULL);

SET FOREIGN_KEY_CHECKS = 1;
