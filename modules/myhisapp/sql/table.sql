/*建库*/
create database myhis default character set utf8;

DROP TABLE IF EXISTS myproject0;
CREATE TABLE myproject0(
    id INT NOT NULL AUTO_INCREMENT,
    -- 对应于统一认证模块里的iprofile表（所有用户的密码，dbindex,tableindex都集中存于该表）的id
    umid INT NOT NULL,
    -- 项目名
    name VARCHAR(32) NOT NULL ,
    -- 项目创建时间
    createtime DATETIME,
    -- 最后修改时间
    lasttime DATETIME,
    PRIMARY KEY (id),
    UNIQUE KEY (umid,name)
)ENGINE = MYISAM;

DROP TABLE IF EXISTS myprojectdetail0;
CREATE TABLE myprojectdetail0(
    id INT NOT NULL AUTO_INCREMENT,
    -- 对应于myproject0表的id
    projectid INT,
    -- 对应于统一认证模块里的iprofile表（所有用户的密码，dbindex,tableindex都集中存于该表）的id
    umid INT NOT NULL,
    -- 事件名称
    itemname VARCHAR(128) NOT NULL ,
    -- 事件开始时间
    starttime DATETIME NOT NULL ,
    -- 事件结束时间
    endtime DATETIME NULL ,
    -- 事件备注
    comment VARCHAR(256) NULL ,
    PRIMARY KEY (id),
    KEY (projectid,starttime),
    KEY (umid)
)ENGINE = MYISAM;