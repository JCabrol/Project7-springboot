DROP TABLE if EXISTS Bid_list;
CREATE TABLE Bid_list (
  bid_list_id tinyint(4) NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  bid_quantity DOUBLE,
  ask_quantity DOUBLE,
  bid DOUBLE ,
  ask DOUBLE,
  benchmark VARCHAR(125),
  bid_list_date TIMESTAMP,
  commentary VARCHAR(125),
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  book VARCHAR(125),
  creation_name VARCHAR(125),
  creation_date TIMESTAMP ,
  revision_name VARCHAR(125),
  revision_date TIMESTAMP ,
  deal_name VARCHAR(125),
  deal_type VARCHAR(125),
  source_list_id VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (bid_list_id)
);

DROP TABLE if EXISTS Trade;
CREATE TABLE Trade (
  trade_id tinyint(4) NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  buy_quantity DOUBLE,
  sell_quantity DOUBLE,
  buy_price DOUBLE ,
  sell_price DOUBLE,
  trade_date TIMESTAMP,
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  benchmark VARCHAR(125),
  book VARCHAR(125),
  creation_name VARCHAR(125),
  creation_date TIMESTAMP ,
  revision_name VARCHAR(125),
  revision_date TIMESTAMP ,
  deal_name VARCHAR(125),
  deal_type VARCHAR(125),
  source_list_id VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (trade_id)
);

DROP TABLE if EXISTS Curve_point;
CREATE TABLE Curve_point (
  id tinyint(4) NOT NULL AUTO_INCREMENT,
  curve_id tinyint,
  as_of_date TIMESTAMP,
  term DOUBLE ,
  value DOUBLE ,
  creation_date TIMESTAMP ,

  PRIMARY KEY (id)
);

DROP TABLE if EXISTS Rating;
CREATE TABLE Rating (
  id tinyint(4) NOT NULL AUTO_INCREMENT,
  moodys_rating VARCHAR(125),
  sand_p_rating VARCHAR(125),
  fitch_rating VARCHAR(125),
  order_number tinyint,

  PRIMARY KEY (id)
);

DROP TABLE if EXISTS Rule_name;
CREATE TABLE Rule_name (
  id tinyint(4) NOT NULL AUTO_INCREMENT,
  name VARCHAR(125),
  description VARCHAR(125),
  json VARCHAR(125),
  template VARCHAR(512),
  sql_str VARCHAR(125),
  sql_part VARCHAR(125),

  PRIMARY KEY (id)
);
DROP TABLE if EXISTS Users;
CREATE TABLE Users (
  id tinyint(4) NOT NULL AUTO_INCREMENT,
  username VARCHAR(125),
  password VARCHAR(125),
  fullname VARCHAR(125),
  role VARCHAR(125),

  PRIMARY KEY (id)
);

INSERT INTO Bid_list (account, type, bid_quantity) VALUES
('account1','type1',10.0),
('account2','type2',20.0),
('account3','type3',30.0);

INSERT INTO Curve_point (curve_id, term, value) VALUES
(1,10.0,11.1),
(2,20.0,22.2),
(3,30.0,33.3);

INSERT INTO Rating (moodys_rating, sand_p_rating, fitch_rating, order_number) VALUES
('moodysRating1','sandPRating1','fitchRating1',1),
('moodysRating2','sandPRating2','fitchRating2',2),
('moodysRating3','sandPRating3','fitchRating3',3);

INSERT INTO Rule_name (name, description, json, template,sql_str,sql_part) VALUES
('name1','description1','json1','template1','sqlStr1','sqlPart1'),
('name2','description2','json2','template2','sqlStr2','sqlPart2'),
('name3','description3','json3','template3','sqlStr3','sqlPart3');

INSERT INTO Trade (account, type, buy_quantity) VALUES
('account1','type1',10.0),
('account2','type2',20.0),
('account3','type3',30.0);


insert into Users(fullname, username, password, role) values("Administrator", "admin", "$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa", "ADMIN");
insert into Users(fullname, username, password, role) values("User", "user", "$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa", "USER");