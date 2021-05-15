DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS threads;

CREATE TABLE users (
	username VARCHAR(50) NOT NULL PRIMARY KEY,
	password VARCHAR(120) NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
	username VARCHAR(50) NOT NULL,
	authority VARCHAR(50) NOT NULL,
	FOREIGN KEY (username) REFERENCES users (username)
);

CREATE TABLE threads (
	threadId LONG PRIMARY KEY AUTO_INCREMENT,
	title varchar(50) NOT NULL,
	thread VARCHAR(300) NOT NULL,
	username VARCHAR(50) NOT NULL,
	threadDate DATE,
	threadTime TIME
);

CREATE TABLE posts (
	postId LONG PRIMARY KEY AUTO_INCREMENT,
	threadId LONG NOT NULL,
	post VARCHAR(300) NOT NULL,
	username VARCHAR(50) NOT NULL,
	postDate DATE,
	postTime TIME,
	FOREIGN KEY (threadId) REFERENCES threads (threadId)
);