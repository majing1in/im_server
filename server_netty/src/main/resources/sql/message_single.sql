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

 Date: 28/03/2021 21:46:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_single
-- ----------------------------
DROP TABLE IF EXISTS `message_single`;
CREATE TABLE `message_single`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `senderId` int(10) NOT NULL,
  `receiverId` int(10) NOT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_single
-- ----------------------------
INSERT INTO `message_single` VALUES (1, 1, 2, '你在干嘛呢，宝贝', '2021-02-16');
INSERT INTO `message_single` VALUES (2, 2, 1, '我在打游戏，勿扰', '2021-02-17');
INSERT INTO `message_single` VALUES (9, 2, 1, 'byb', '2021-02-17');
INSERT INTO `message_single` VALUES (10, 2, 1, 'hhhh', '2021-02-17');
INSERT INTO `message_single` VALUES (11, 2, 1, 'qifei', '2021-02-17');
INSERT INTO `message_single` VALUES (12, 1, 2, 'wertu', '2021-02-17');
INSERT INTO `message_single` VALUES (13, 1, 2, 'techy', '2021-02-17');
INSERT INTO `message_single` VALUES (14, 2, 1, 'were', '2021-02-17');
INSERT INTO `message_single` VALUES (15, 1, 2, 'wet', '2021-02-17');
INSERT INTO `message_single` VALUES (16, 2, 1, 'iOS', '2021-02-17');
INSERT INTO `message_single` VALUES (17, 1, 2, 'android', '2021-02-17');
INSERT INTO `message_single` VALUES (18, 2, 1, 'a Seth', '2021-02-17');
INSERT INTO `message_single` VALUES (19, 1, 2, 'father', '2021-02-17');
INSERT INTO `message_single` VALUES (20, 2, 1, 'HGH', '2021-02-17');
INSERT INTO `message_single` VALUES (21, 1, 2, 'gift', '2021-02-17');
INSERT INTO `message_single` VALUES (22, 2, 1, 'Rudd', '2021-02-17');
INSERT INTO `message_single` VALUES (23, 2, 1, 'chute', '2021-02-17');
INSERT INTO `message_single` VALUES (24, 2, 1, 'refutes', '2021-02-17');
INSERT INTO `message_single` VALUES (25, 2, 1, 'FDNY', '2021-02-17');
INSERT INTO `message_single` VALUES (26, 2, 1, 'eggs HFCS', '2021-02-17');

SET FOREIGN_KEY_CHECKS = 1;
