CREATE TABLE `car` (
  `id` varchar(45) NOT NULL,
  `name` varchar(50) DEFAULT '',
  `registerDate` varchar(50) DEFAULT '',
  `carShortName` varchar(50) DEFAULT '',
  `pictures` varchar(50) DEFAULT '',
  `shopCode` varchar(50) DEFAULT '',
  `brand` varchar(50) DEFAULT '',
  `price` double DEFAULT '0',
  `priceGuide` double DEFAULT '0',
  `date_create` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` varchar(45) NOT NULL,
  `username` varchar(50) DEFAULT '',
  `password` varchar(200) DEFAULT '',
  `email` varchar(50) DEFAULT '',
  `roles` varchar(500) DEFAULT '',
  `last_password_reset` datetime DEFAULT NULL,
  `date_create` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;