CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `active` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Account enabled or disabled',
  `username` varchar(80) NOT NULL COMMENT 'username, used for logins (POP, SMTP, web)',
  `email` varchar(80) NOT NULL COMMENT 'email address for incoming/outgoing mail',
  `name` varchar(128) NOT NULL COMMENT 'full name of user',
  `password` varchar(128) NOT NULL COMMENT 'user password in any format supported by dovecot',
  `uid` smallint(5) NOT NULL DEFAULT '5000' COMMENT 'UNIX uid; 5000 for virtual mailbox',
  `gid` smallint(5) NOT NULL DEFAULT '5000' COMMENT 'UNIX gid; 5000 for virtual mailbox',
  `home` varchar(128) NOT NULL DEFAULT '/home/vmail/mailboxes' COMMENT 'UNIX home directory; /home/vmail/mailboxes for virtual mailbox',
  `maildir` varchar(255) NOT NULL COMMENT 'Maildir/ for regular users; domain/userid/ for virtual. Remember trailing /',
  `allowemail` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Is email allowed? (0 for web accounts)',
  `client` varchar(16) DEFAULT NULL COMMENT 'current client IP; used for session management',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
