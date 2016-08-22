/*建库*/
create database ah_authcenter default character set utf8;

drop table if exists `iprofie`;
create table `iprofile` (
    -- 用户的umid
    id INT NOT NULL AUTO_INCREMENT,
    password VARCHAR(256) NOT NULL ,
    -- 数据服务器（websocket server）的ip
    siteip VARCHAR(64),
    siteport int,
    -- 以下两项用于分表分库以便支持大量用户
    -- 每个myhisapp对应一个db,因此dbindex实际是myhisapp的索引；用于websocket server寻址myhisapp
    dbindex int,
    tableindex int,
    -- 用户权限
    status int DEFAULT 1,
    PRIMARY KEY (id)
)ENGINE = MYISAM;

drop table if exists `profie`;
create table `profile` (
    -- 用户的umid,对应于iprofile表的id
    umid INT NOT NULL,
    mobilephone VARCHAR(16) NULL ,
    emailaddress VARCHAR(128) NULL ,
    createtime DATETIME,
    PRIMARY KEY (umid),
    UNIQUE KEY (mobilephone),
    UNIQUE KEY (emailaddress)
)ENGINE = MYISAM;

