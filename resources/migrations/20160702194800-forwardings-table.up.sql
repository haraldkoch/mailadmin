CREATE TABLE `forwardings` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `source` varchar(80) NOT NULL,
  `destination` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `source` (`source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 COMMENT='email forwardings aka aliases';
