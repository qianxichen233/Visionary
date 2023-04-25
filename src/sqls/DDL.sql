CREATE TABLE user (
    username VARCHAR(50),
    password VARCHAR(200) NOT NULL,
    PRIMARY KEY(username)
);

CREATE TABLE drawing (
    ID INT AUTO_INCREMENT,
    hash VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL,
    filename VARCHAR(50) NOT NULL,
    createdAt DATETIME NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY(username) REFERENCES user(username)
);

CREATE TABLE session (
    token VARCHAR(256),
    username VARCHAR(50) NOT NULL,
    expiration DATETIME NOT NULL,
    PRIMARY KEY(token),
    FOREIGN KEY(username) REFERENCES user(username)
);