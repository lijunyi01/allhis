/*建库*/
create database ah_myhisapp default character set utf8;

DROP TABLE IF EXISTS myproject0;
CREATE TABLE myproject0(
    id INT NOT NULL AUTO_INCREMENT,
    -- 对应于统一认证模块里的iprofile表（所有用户的密码，dbindex,tableindex都集中存于该表）的id
    umid INT NOT NULL,
    -- 项目名
    projectname VARCHAR(32) NOT NULL ,
    -- 项目创建时间
    createtime DATETIME,
    -- 最后修改时间
    lasttime DATETIME,
    PRIMARY KEY (id),
    UNIQUE KEY (umid,projectname),
    KEY (lasttime)
)ENGINE = MYISAM;

DROP TABLE IF EXISTS projectitem0;
CREATE TABLE projectitem0(
    id INT NOT NULL AUTO_INCREMENT,
    -- 对应于myproject0表的id
    projectid INT,
    -- 对应于统一认证模块里的iprofile表（所有用户的密码，dbindex,tableindex都集中存于该表）的id
    umid INT NOT NULL,
    -- 事件名称/标题
    itemname VARCHAR(128) NOT NULL ,
    -- 事件更具体的内容
    itemcontent VARCHAR(512) NULL ,
    -- 事件开始时间
    starttime DATETIME NOT NULL ,
    -- 事件结束时间
    endtime DATETIME NULL ,
    PRIMARY KEY (id),
    KEY (projectid,starttime),
    KEY (umid)
)ENGINE = MYISAM;

DROP TABLE IF EXISTS itemfile0;
CREATE TABLE itemfile0(
    id INT NOT NULL AUTO_INCREMENT,
    -- 对应于projectitem0表的id
    itemid INT,
    -- 以下两项用于冗余，用于方便地按projectid或umid删除数据等
    projectid INT,
    umid INT NOT NULL,
    filename VARCHAR(256) NOT NULL ,
    filesuffix VARCHAR(8) NULL ,
    -- 文件存储时是路径；对象存储时是索引
    filepath VARCHAR(128) NOT NULL ,
    PRIMARY KEY (id),
    KEY (itemid),
    KEY (projectid),
    KEY (umid)
)ENGINE = MYISAM;

-- 用于测试
DROP TABLE IF EXISTS test;
CREATE TABLE test(
    id INT,
    PRIMARY KEY (id)
)ENGINE = MYISAM;