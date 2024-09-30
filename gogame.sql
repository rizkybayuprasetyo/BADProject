-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 11, 2023 at 12:11 PM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gogame`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `UserID` char(5) NOT NULL,
  `ItemID` char(5) NOT NULL,
  `Quantity` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `mscourier`
--

CREATE TABLE `mscourier` (
  `CourierID` char(5) NOT NULL,
  `CourierName` varchar(30) NOT NULL,
  `CourierPrice` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mscourier`
--

INSERT INTO `mscourier` (`CourierID`, `CourierName`, `CourierPrice`) VALUES
('CO001', 'HubertExprss', 20000),
('CO002', 'Speed', 19000),
('CO003', 'FetEx', 22000),
('CO004', 'GoGrabUberSend', 30000),
('CO005', 'Magically Appear', 150000);

-- --------------------------------------------------------

--
-- Table structure for table `msitem`
--

CREATE TABLE `msitem` (
  `ItemID` char(5) NOT NULL,
  `ItemName` varchar(50) DEFAULT NULL,
  `ItemPrice` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `msitem`
--

INSERT INTO `msitem` (`ItemID`, `ItemName`, `ItemPrice`) VALUES
('I0001', 'HyperX Fury Gaming Mouse', 75000),
('I0002', 'Razer BlackWidow Mechanical Keyboard', 450000),
('I0003', 'Logitech Pro X Gamer Headset', 120000),
('I0004', 'NVIDIA GeForce RTX 3080 Graphics Card', 850000),
('I0005', 'Cyberpunk 2077 Game Title', 6000),
('I0006', 'DXRacer Formula Series Gaming Chair', 220000),
('I0007', 'Blue Yeti Streaming Microphone', 175000),
('I0008', 'ASUS ROG Swift Gaming Monitor', 420000),
('I0009', 'PlayStation 5 Console', 900000),
('I0010', 'Oculus Rift S VR Headset', 320000),
('I0011', 'Rexis Headset', 250000);

-- --------------------------------------------------------

--
-- Table structure for table `msuser`
--

CREATE TABLE `msuser` (
  `UserID` char(5) NOT NULL,
  `Username` varchar(30) NOT NULL,
  `UserEmail` varchar(50) NOT NULL,
  `UserPassword` varchar(20) NOT NULL,
  `UserGender` varchar(10) NOT NULL,
  `UserRole` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `msuser`
--

INSERT INTO `msuser` (`UserID`, `Username`, `UserEmail`, `UserPassword`, `UserGender`, `UserRole`) VALUES
('US001', 'Brandy', 'brandy123@gmail.com', 'Brandy123', 'Male', 'Users'),
('US002', 'T203admin', 't203@gmail.com', 'T203Struggles', 'Male', 'Admin');

-- --------------------------------------------------------

--
-- Table structure for table `transactiondetail`
--

CREATE TABLE `transactiondetail` (
  `TransactionID` char(5) NOT NULL,
  `ItemID` char(5) NOT NULL,
  `Quantity` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactiondetail`
--

INSERT INTO `transactiondetail` (`TransactionID`, `ItemID`, `Quantity`) VALUES
('TR001', 'I0005', 1),
('TR001', 'I0010', 1),
('TR001', 'I0008', 1),
('TR001', 'I0004', 1),
('TR001', 'I0010', 1);

-- --------------------------------------------------------

--
-- Table structure for table `transactionheader`
--

CREATE TABLE `transactionheader` (
  `TransactionID` char(5) NOT NULL,
  `UserID` char(5) NOT NULL,
  `CourierID` char(5) NOT NULL,
  `TransactionDate` date NOT NULL,
  `UseDeliveryInsurance` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactionheader`
--

INSERT INTO `transactionheader` (`TransactionID`, `UserID`, `CourierID`, `TransactionDate`, `UseDeliveryInsurance`) VALUES
('TR001', 'US001', 'CO004', '2023-12-10', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
