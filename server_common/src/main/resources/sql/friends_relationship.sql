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

 Date: 30/03/2021 23:38:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for friends_relationship
-- ----------------------------
DROP TABLE IF EXISTS `friends_relationship`;
CREATE TABLE `friends_relationship`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `friend_id` int(10) NULL DEFAULT NULL COMMENT '好友id',
  `friend_user_id` int(10) NULL DEFAULT NULL,
  `friend_nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `create_time` date NULL DEFAULT NULL,
  `update_time` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of friends_relationship
-- ----------------------------
INSERT INTO `friends_relationship` VALUES (1, 2, 1, '大司马', '2021-02-15', '2021-02-15');
INSERT INTO `friends_relationship` VALUES (2, 1, 2, '卢本伟', '2021-02-17', '2021-02-17');
INSERT INTO `friends_relationship` VALUES (9, 2, 3, '123456789', '2021-02-17', '2021-02-17');
INSERT INTO `friends_relationship` VALUES (10, 3, 2, '101010101', '2021-02-17', '2021-02-17');

SET FOREIGN_KEY_CHECKS = 1;
