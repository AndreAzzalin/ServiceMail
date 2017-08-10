-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Creato il: Ago 10, 2017 alle 20:49
-- Versione del server: 10.1.16-MariaDB
-- Versione PHP: 5.6.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_mail`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `destinatari`
--

CREATE TABLE `destinatari` (
  `IDMAIL` int(30) NOT NULL,
  `letta` tinyint(1) NOT NULL DEFAULT '0',
  `DESTINATARIO` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `destinatari`
--

INSERT INTO `destinatari` (`IDMAIL`, `letta`, `DESTINATARIO`) VALUES
(1, 1, 'cesare@gmail.com'),
(2, 0, 'azza@gmail.com');

-- --------------------------------------------------------

--
-- Struttura della tabella `email`
--

CREATE TABLE `email` (
  `ID` int(11) NOT NULL,
  `Prefered` tinyint(1) DEFAULT '0',
  `Mittente` varchar(30) NOT NULL,
  `Argomento` varchar(50) NOT NULL,
  `Testo` varchar(535) NOT NULL,
  `DataSpedizione` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `email`
--

INSERT INTO `email` (`ID`, `Prefered`, `Mittente`, `Argomento`, `Testo`, `DataSpedizione`) VALUES
(1, 0, 'azza@gmail.com', 'mailAzzaToCesare', 'mailAzzaToCesare\nmailAzzaToCesare\nmailAzzaToCesare\nmailAzzaToCesare', '2017-08-10'),
(2, 0, 'cesare@gmail.com', 'RE: ', 'df', '2017-08-10');

-- --------------------------------------------------------

--
-- Struttura della tabella `mailactive`
--

CREATE TABLE `mailactive` (
  `ID_USER` varchar(30) NOT NULL,
  `ID_MAIL` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `mailactive`
--

INSERT INTO `mailactive` (`ID_USER`, `ID_MAIL`) VALUES
('azza@gmail.com', 1),
('cesare@gmail.com', 1),
('cesare@gmail.com', 2),
('azza@gmail.com', 2);

-- --------------------------------------------------------

--
-- Struttura della tabella `utenti`
--

CREATE TABLE `utenti` (
  `MAIL` varchar(30) NOT NULL,
  `PASSWORD` varchar(30) DEFAULT NULL,
  `user_img` varchar(30) NOT NULL DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `utenti`
--

INSERT INTO `utenti` (`MAIL`, `PASSWORD`, `user_img`) VALUES
('azza@gmail.com', 'azza', 'user1'),
('cesare@gmail.com', 'cesare', 'user2'),
('fabio@gmail.com', 'fabio', 'user3');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `email`
--
ALTER TABLE `email`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `utenti`
--
ALTER TABLE `utenti`
  ADD PRIMARY KEY (`MAIL`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `email`
--
ALTER TABLE `email`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
