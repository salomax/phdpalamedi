SELECT @@max_allowed_packet;
SET GLOBAL max_allowed_packet=16613376;

-- Get the sizes of the tables of a mysql database
SELECT
     table_schema as `Database`,
     table_name AS `Table`,
     round(((data_length + index_length) / 1024 / 1024), 2) `Size in MB`
FROM information_schema.TABLES
ORDER BY (data_length + index_length) DESC;


# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.20)
# Database: phdpalamedi
# Generation Time: 2018-03-25 04:32:44 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table TB_ARTICLE
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TB_ARTICLE`;

CREATE TABLE `TB_ARTICLE` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `publisher_id` int(11) unsigned NOT NULL,
  `status` varchar(8) NOT NULL DEFAULT '',
  `url` varchar(256) NOT NULL DEFAULT '',
  `file_name` varchar(64) DEFAULT '',
  `content` mediumtext,
  `error` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table TB_PUBLISHER
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TB_PUBLISHER`;

CREATE TABLE `TB_PUBLISHER` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table TB_PUBLISHER_FILTER
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TB_PUBLISHER_FILTER`;

CREATE TABLE `TB_PUBLISHER_FILTER` (
  `id` int(10) unsigned NOT NULL,
  `publisher_id` int(11) NOT NULL,
  `url` varchar(128) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table TB_PUBLISHER_URL
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TB_PUBLISHER_URL`;

CREATE TABLE `TB_PUBLISHER_URL` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `publisher_id` int(11) NOT NULL,
  `url` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
