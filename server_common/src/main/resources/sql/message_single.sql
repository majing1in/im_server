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

 Date: 29/03/2021 20:06:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_single
-- ----------------------------
DROP TABLE IF EXISTS `message_single`;
CREATE TABLE `message_single`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  `message_type` int NULL DEFAULT NULL,
  `message_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message_single
-- ----------------------------
INSERT INTO `message_single` VALUES (1, 1, 2, NULL, '你在干嘛呢，宝贝', '2021-02-16');
INSERT INTO `message_single` VALUES (2, 2, 1, NULL, '我在打游戏，勿扰', '2021-02-17');
INSERT INTO `message_single` VALUES (9, 2, 1, NULL, 'byb', '2021-02-17');
INSERT INTO `message_single` VALUES (10, 2, 1, NULL, 'hhhh', '2021-02-17');
INSERT INTO `message_single` VALUES (11, 2, 1, NULL, 'qifei', '2021-02-17');
INSERT INTO `message_single` VALUES (12, 1, 2, NULL, 'wertu', '2021-02-17');
INSERT INTO `message_single` VALUES (13, 1, 2, NULL, 'techy', '2021-02-17');
INSERT INTO `message_single` VALUES (14, 2, 1, NULL, 'were', '2021-02-17');
INSERT INTO `message_single` VALUES (15, 1, 2, NULL, 'wet', '2021-02-17');
INSERT INTO `message_single` VALUES (16, 2, 1, NULL, 'iOS', '2021-02-17');
INSERT INTO `message_single` VALUES (17, 1, 2, NULL, 'android', '2021-02-17');
INSERT INTO `message_single` VALUES (18, 2, 1, NULL, 'a Seth', '2021-02-17');
INSERT INTO `message_single` VALUES (19, 1, 2, NULL, 'father', '2021-02-17');
INSERT INTO `message_single` VALUES (20, 2, 1, NULL, 'HGH', '2021-02-17');
INSERT INTO `message_single` VALUES (21, 1, 2, NULL, 'gift', '2021-02-17');
INSERT INTO `message_single` VALUES (22, 2, 1, NULL, 'Rudd', '2021-02-17');
INSERT INTO `message_single` VALUES (23, 2, 1, NULL, 'chute', '2021-02-17');
INSERT INTO `message_single` VALUES (24, 2, 1, NULL, 'refutes', '2021-02-17');
INSERT INTO `message_single` VALUES (25, 2, 1, NULL, 'FDNY', '2021-02-17');
INSERT INTO `message_single` VALUES (26, 2, 1, NULL, 'eggs HFCS', '2021-02-17');

SET FOREIGN_KEY_CHECKS = 1;
