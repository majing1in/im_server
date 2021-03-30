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

 Date: 30/03/2021 23:39:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_balance_money
-- ----------------------------
DROP TABLE IF EXISTS `user_balance_money`;
CREATE TABLE `user_balance_money`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `money` decimal(65, 2) NULL DEFAULT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_balance_money
-- ----------------------------
INSERT INTO `user_balance_money` VALUES (1, 1, 9200.00, 4);
INSERT INTO `user_balance_money` VALUES (7, 2, 183.22, 6);

SET FOREIGN_KEY_CHECKS = 1;
