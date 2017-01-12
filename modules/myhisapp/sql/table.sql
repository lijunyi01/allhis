/*建库*/
create database ah_myhisapp default character set utf8;

DROP TABLE IF EXISTS myproject0;
CREATE TABLE myproject0(
    id INT NOT NULL AUTO_INCREMENT,
    -- 对应于统一认证模块里的iprofile表（所有用户的密码，dbindex,tableindex都集中存于该表）的id
    umid INT NOT NULL,
    -- 项目名
    projectname VARCHAR(32) NOT NULL ,
    -- 项目描述
    projectdes VARCHAR(1024) NULL,
    -- 项目创建时间
    createtime DATETIME,
    -- 最后修改时间
    lasttime DATETIME,
    -- 私有还是公开  0:私有  1:公开
    shareflag INT NOT NULL DEFAULT 0,
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
    itemcontent VARCHAR(1024) NULL ,
    -- 时间类型  1: 点时间，公元纪年； 2: 点时间，年号纪年；  3:段时间，公元纪年； 4:段时间，年号纪年
    itemtype INT NOT NULL DEFAULT 0,
    -- 事件开始年份  >0 公元后 ； <0 公元前 ； ＝0 初始值，无意义
    startyear INT NOT NULL DEFAULT 0,
    -- 是否是粗略开始年份  0:不是粗略的；1：是粗略的
    startyearndflag INT NOT NULL DEFAULT 0,
    -- 事件开始年份（文字描述，年号等）
    startyear_des VARCHAR(64),
    -- 事件开始时间  格式：MM-DD hh:mm:ss；null 表示精确到年份
    starttime VARCHAR(64),
    -- 事件结束年份  >0 公元后 ； <0 公元前 ； ＝0 初始值，无意义
    endyear INT NOT NULL DEFAULT 0,
    -- 是否是粗略结束年份  0:不是粗略的；1：是粗略的
    endyearndflag INT NOT NULL DEFAULT 0,
    -- 事件结束年份（文字描述，年号等）
    endyear_des VARCHAR(64),
    -- 事件结束时间  格式：MM-DD hh:mm:ss；null 表示精确到年份
    endtime VARCHAR(64),
    PRIMARY KEY (id),
    KEY (projectid,startyear),
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

DROP TABLE IF EXISTS itemtips0;
CREATE TABLE itemtips0(
    id INT NOT NULL AUTO_INCREMENT,
    -- 对应于projectitem0表的id
    itemid INT,
    -- 以下两项用于冗余，用于方便地按projectid或umid删除数据等
    projectid INT,
    umid INT NOT NULL,
    tipcontent VARCHAR(256) NULL ,
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