/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : dbs_db

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 05/04/2020 19:38:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bill
-- ----------------------------
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ORDER_ID` int NOT NULL,
  `PAID` tinyint(1) NOT NULL,
  `PRICE` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `b_order` (`ORDER_ID`),
  CONSTRAINT `b_order` FOREIGN KEY (`ORDER_ID`) REFERENCES `orders` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bill
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for chefs
-- ----------------------------
DROP TABLE IF EXISTS `chefs`;
CREATE TABLE `chefs` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chefs
-- ----------------------------
BEGIN;
INSERT INTO `chefs` VALUES (1, 'Kuchár Janko');
INSERT INTO `chefs` VALUES (2, 'Kuchár Jožko');
INSERT INTO `chefs` VALUES (3, 'Kuchár Marián');
INSERT INTO `chefs` VALUES (4, 'Kuchár Ivan');
COMMIT;

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `PRICE` int NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of food
-- ----------------------------
BEGIN;
INSERT INTO `food` VALUES (1, 'Halušky', 20);
INSERT INTO `food` VALUES (2, 'Pizza', 30);
INSERT INTO `food` VALUES (3, 'Špagety', 10);
INSERT INTO `food` VALUES (4, 'Guľáš', 15);
INSERT INTO `food` VALUES (5, 'Prívarok', 5);
INSERT INTO `food` VALUES (6, 'Chleba', 10);
INSERT INTO `food` VALUES (7, 'Sviečková', 15);
INSERT INTO `food` VALUES (8, 'Rezeň', 7);
INSERT INTO `food` VALUES (9, 'Praženica', 8);
INSERT INTO `food` VALUES (10, 'Jogurt', 3);
INSERT INTO `food` VALUES (11, 'Lievance', 5);
INSERT INTO `food` VALUES (12, 'Palacinky', 10);
INSERT INTO `food` VALUES (13, 'Kaša', 4);
INSERT INTO `food` VALUES (14, 'Rožok', 1);
INSERT INTO `food` VALUES (15, 'Moravský koláč', 3);
INSERT INTO `food` VALUES (16, 'Bábovka', 4);
INSERT INTO `food` VALUES (17, 'Omeleta', 5);
INSERT INTO `food` VALUES (18, 'Toasty', 4);
INSERT INTO `food` VALUES (19, 'Šišky', 2);
INSERT INTO `food` VALUES (20, 'Cereálie', 10);
INSERT INTO `food` VALUES (21, 'Sušienky', 10);
INSERT INTO `food` VALUES (22, 'Melón', 2);
INSERT INTO `food` VALUES (23, 'Jablko', 4);
INSERT INTO `food` VALUES (24, 'Paštéta', 2);
INSERT INTO `food` VALUES (25, 'Kuracie soté', 12);
INSERT INTO `food` VALUES (26, 'Rizoto', 15);
INSERT INTO `food` VALUES (45, 'Hamburger', 10);
COMMIT;

-- ----------------------------
-- Table structure for food_chef
-- ----------------------------
DROP TABLE IF EXISTS `food_chef`;
CREATE TABLE `food_chef` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CHEF_ID` int NOT NULL,
  `FOOD_ID` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fc_chefs` (`CHEF_ID`),
  KEY `fc_food` (`FOOD_ID`),
  CONSTRAINT `fc_chefs` FOREIGN KEY (`CHEF_ID`) REFERENCES `chefs` (`ID`),
  CONSTRAINT `fc_food` FOREIGN KEY (`FOOD_ID`) REFERENCES `food` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of food_chef
-- ----------------------------
BEGIN;
INSERT INTO `food_chef` VALUES (1, 2, 1);
INSERT INTO `food_chef` VALUES (2, 3, 2);
INSERT INTO `food_chef` VALUES (3, 2, 3);
INSERT INTO `food_chef` VALUES (6, 1, 4);
INSERT INTO `food_chef` VALUES (7, 3, 5);
INSERT INTO `food_chef` VALUES (8, 1, 6);
INSERT INTO `food_chef` VALUES (9, 2, 7);
INSERT INTO `food_chef` VALUES (10, 3, 8);
INSERT INTO `food_chef` VALUES (11, 4, 9);
INSERT INTO `food_chef` VALUES (12, 1, 10);
INSERT INTO `food_chef` VALUES (13, 2, 11);
INSERT INTO `food_chef` VALUES (14, 3, 12);
INSERT INTO `food_chef` VALUES (15, 4, 13);
INSERT INTO `food_chef` VALUES (16, 1, 14);
INSERT INTO `food_chef` VALUES (17, 2, 15);
INSERT INTO `food_chef` VALUES (18, 3, 16);
INSERT INTO `food_chef` VALUES (19, 4, 17);
INSERT INTO `food_chef` VALUES (20, 1, 18);
INSERT INTO `food_chef` VALUES (21, 2, 19);
INSERT INTO `food_chef` VALUES (22, 3, 20);
INSERT INTO `food_chef` VALUES (23, 4, 21);
INSERT INTO `food_chef` VALUES (24, 1, 22);
INSERT INTO `food_chef` VALUES (25, 2, 23);
INSERT INTO `food_chef` VALUES (26, 3, 24);
INSERT INTO `food_chef` VALUES (27, 4, 25);
INSERT INTO `food_chef` VALUES (28, 1, 26);
INSERT INTO `food_chef` VALUES (29, 2, 45);
COMMIT;

-- ----------------------------
-- Table structure for food_ingredients
-- ----------------------------
DROP TABLE IF EXISTS `food_ingredients`;
CREATE TABLE `food_ingredients` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `FOOD_ID` int NOT NULL,
  `INGREDIENTS_ID` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fi_food` (`FOOD_ID`),
  KEY `fi_ingredients` (`INGREDIENTS_ID`),
  CONSTRAINT `fi_food` FOREIGN KEY (`FOOD_ID`) REFERENCES `food` (`ID`),
  CONSTRAINT `fi_ingredients` FOREIGN KEY (`INGREDIENTS_ID`) REFERENCES `ingredients` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of food_ingredients
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for food_orders
-- ----------------------------
DROP TABLE IF EXISTS `food_orders`;
CREATE TABLE `food_orders` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `FOOD_ID` int NOT NULL,
  `ORDER_ID` int NOT NULL,
  `COUNT` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fo_order` (`ORDER_ID`),
  KEY `fo_food` (`FOOD_ID`),
  CONSTRAINT `fo_food` FOREIGN KEY (`FOOD_ID`) REFERENCES `food` (`ID`),
  CONSTRAINT `fo_order` FOREIGN KEY (`ORDER_ID`) REFERENCES `orders` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of food_orders
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ingredients
-- ----------------------------
DROP TABLE IF EXISTS `ingredients`;
CREATE TABLE `ingredients` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ingredients
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `TIME` timestamp NOT NULL,
  `USER_ID` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `o_users` (`USER_ID`),
  CONSTRAINT `o_users` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orders
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `ADDRESS` varchar(45) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `EMAIL` varchar(45) NOT NULL,
  `PRIVILEDGED` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `EMAIL_UNIQUE` (`EMAIL`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES (1, 'root', '-', '63a9f0ea7bb98050796b649e85481845', 'root', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
