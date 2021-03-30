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

 Date: 30/03/2021 23:39:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_information
-- ----------------------------
DROP TABLE IF EXISTS `user_information`;
CREATE TABLE `user_information`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
  `user_account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户登录账号',
  `user_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户登录密码',
  `user_nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户昵称',
  `user_sign` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户个性签名',
  `user_gender` int(2) NULL DEFAULT NULL COMMENT '用户性别',
  `user_birthday` date NULL DEFAULT NULL COMMENT '用户生日',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '真实姓名',
  `user_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '邮箱地址',
  `user_head_photo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '头像',
  `user_school_tag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '毕业院校',
  `user_constellation` int(3) NULL DEFAULT NULL COMMENT '星座',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` date NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_information
-- ----------------------------
INSERT INTO `user_information` VALUES (1, '987654321', '987654321', '路飞', '都是梦!!!', 0, '2021-02-08', 'string', '1563269549@qq.com', 'https://server-aliyun-images.oss-cn-shanghai.aliyuncs.com/2021-02-16/4556c5048d4c4ef5a92354d7246b42f3.jpg', 'string', 0, '2021-02-08', '2021-02-17');
INSERT INTO `user_information` VALUES (2, '123456789', '123456789', '小马hahaha', '一场游戏一场梦', 0, '2021-02-10', '小马', '1468835254@qq.com', 'https://server-aliyun-images.oss-cn-shanghai.aliyuncs.com/2021-02-15/e880384b82094ba2ad309ce7f6236f9f.jpg', '', 12, '2021-02-10', '2021-02-15');
INSERT INTO `user_information` VALUES (3, '101010101', '101010101', '柯南', '真正只有一个', 1, '2021-02-17', '芜湖', '9632654153@qq.com', 'https://server-aliyun-images.oss-cn-shanghai.aliyuncs.com/2021-02-15/d359b83d-8dae-4ef1-859d-b8a429f46449_1.jpg', '', 2, '2021-02-11', '2021-02-11');

SET FOREIGN_KEY_CHECKS = 1;
