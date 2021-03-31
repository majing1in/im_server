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

 Date: 31/03/2021 11:01:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for private_chat_list
-- ----------------------------
DROP TABLE IF EXISTS `private_chat_list`;
CREATE TABLE `private_chat_list`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of private_chat_list
-- ----------------------------
INSERT INTO `private_chat_list` VALUES (1, 1, 2, '你在干嘛呢，宝贝', '2021-02-16');
INSERT INTO `private_chat_list` VALUES (2, 2, 1, '我在打游戏，勿扰', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (9, 2, 1, 'byb', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (10, 2, 1, 'hhhh', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (11, 2, 1, 'qifei', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (12, 1, 2, 'wertu', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (13, 1, 2, 'techy', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (14, 2, 1, 'were', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (15, 1, 2, 'wet', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (16, 2, 1, 'iOS', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (17, 1, 2, 'android', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (18, 2, 1, 'a Seth', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (19, 1, 2, 'father', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (20, 2, 1, 'HGH', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (21, 1, 2, 'gift', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (22, 2, 1, 'Rudd', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (23, 2, 1, 'chute', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (24, 2, 1, 'refutes', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (25, 2, 1, 'FDNY', '2021-02-17');
INSERT INTO `private_chat_list` VALUES (26, 2, 1, 'eggs HFCS', '2021-02-17');

SET FOREIGN_KEY_CHECKS = 1;
