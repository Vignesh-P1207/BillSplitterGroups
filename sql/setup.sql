CREATE DATABASE bill_splitter;

USE bill_splitter;

CREATE TABLE persons (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE expenses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(100),
    amount DOUBLE,
    payer_id INT,
    FOREIGN KEY (payer_id) REFERENCES persons(id)
);
