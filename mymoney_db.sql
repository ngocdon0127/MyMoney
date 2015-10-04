-- phpMyAdmin SQL Dump
-- version 3.5.8.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 04, 2015 at 04:22 PM
-- Server version: 5.1.65
-- PHP Version: 5.3.28

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `hoasinhvie_dondb`
--

-- --------------------------------------------------------

--
-- Table structure for table `mymoney_transaction`
--

CREATE TABLE IF NOT EXISTS `mymoney_transaction` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `type` int(3) NOT NULL,
  `money` int(30) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `details` varchar(10000) COLLATE utf8_unicode_ci NOT NULL,
  `target` int(3) NOT NULL,
  `position` int(1) NOT NULL,
  `wallet` int(30) NOT NULL,
  `atm` int(30) NOT NULL,
  `visa` int(30) NOT NULL,
  `debt` int(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=140 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
