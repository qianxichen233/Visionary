CREATE TABLE user (
    username VARCHAR(50),
    password VARCHAR(200) NOT NULL,
    PRIMARY KEY(username)
);

CREATE TABLE drawing (
    ID INT AUTO_INCREMENT,
    hash VARCHAR(100),
    username VARCHAR(50) NOT NULL,
    filename VARCHAR(50) NOT NULL,
    createdAt DATETIME NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY(username) REFERENCES user(username)
);