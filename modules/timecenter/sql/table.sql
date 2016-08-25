/*建库*/
create database ah_timecenter default character set utf8;

-- 用于多语言支持
DROP TABLE IF EXISTS multilanguagenames;
CREATE TABLE multilanguagenames(
    id INT NOT NULL AUTO_INCREMENT,
    nameid INT NOT NULL,
    name VARCHAR(32) NOT NULL ,
    area VARCHAR(8) NOT NULL ,
    PRIMARY KEY (id),
    UNIQUE KEY (name)
)ENGINE = MYISAM;

-- 年号名
INSERT INTO multilanguagenames(nameid,name,area) VALUES (1,'鲁隐公','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (1,'luyingong','en');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (2,'鲁庄公','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (2,'luzhuanggong','en');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (3,'周敬王','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (4,'周元王','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (5,'周赧王','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (6,'成化','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (7,'洪宪','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (8,'民国','cn');
-- 朝代／时期名
INSERT INTO multilanguagenames(nameid,name,area) VALUES (100001,'周','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (100001,'Zhou Dinasty','en');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (100002,'西周','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (100003,'东周','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (100004,'春秋','cn');
INSERT INTO multilanguagenames(nameid,name,area) VALUES (100005,'战国','cn');

-- 用于通过年号查公元年份
drop table if exists yearbase;
create table yearbase(
    id int not null auto_increment,
    -- 对应于multilanguagenames表的nameid字段
    nameid int not null,
    firstyear int,
    lastyear int,
    comment VARCHAR(32),
    primary key  (`id`),
    UNIQUE KEY (`nameid`)
)ENGINE = MYISAM;

insert into yearbase(nameid,firstyear,comment) values(1,-722,'鲁隐公');
insert into yearbase(nameid,firstyear,comment) values(2,-693,'鲁庄公');
insert into yearbase(nameid,firstyear,comment) values(3,-519,'周敬王');
insert into yearbase(nameid,firstyear,comment) values(4,-475,'周元王');
insert into yearbase(nameid,firstyear,comment) values(5,-314,'周赧王');
insert into yearbase(nameid,firstyear,comment) values(6,1465,'成化');
insert into yearbase(nameid,firstyear,comment) values(7,1916,'洪宪');
insert into yearbase(nameid,firstyear,comment) values(8,1912,'民国');

-- 历史时期表，用于记录各个历史时期的区间
drop table if exists period;
create table period(
    -- id int not null auto_increment,
    -- 对应于multilanguagenames表的nameid字段
    periodnameid INT not null,
    -- 历史时期的类型，例如：中国历史时期，埃及历史时期等
    periodtype int,
    -- 历史时期的级别，1:朝代或时期  2:子时期  3:二级子时期  10: 帝系  11:年号
    periodlevel int,
    beginyear int not null,
    endyear int not null,
    -- 备注
    COMMENT VARCHAR(256) null,
    PRIMARY KEY (periodnameid),
    KEY (`beginyear`),
    KEY (`endyear`)
)ENGINE = MYISAM;

insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values(100001,1,1,-1047,-256,'周，起始时间有争论');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values(100002,1,2,-1047,-771,'西周，起始时间有争论');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values(100003,1,2,-770,-256,'东周，周赧王末年');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values(100004,1,3,-770,-476,'春秋，与孔子所修订的《春秋》年代（公元前722年-公元前481年）大体相当；且－476年为周敬王末年');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values(100005,1,3,-475,-221,'战国，《史记》以周元王元年为始叙述六国时事');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('秦',1,1,-221,-207,'-207为二世卒年；子婴降');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('西楚',1,1,-206,-202,'');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('汉',1,1,-202,220,'讫于曹丕废汉');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('西汉',1,2,-202,8,'8年12月王莽篡汉');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('新',1,2,9,23,'');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('东汉',1,2,25,220,'讫于曹丕废汉');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('三国',1,1,220,266,'三国时期，不是正式的朝代，一般以曹魏为此时期正统');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('魏',1,2,220,266,'曹丕于220年12月代汉称帝');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('蜀',1,2,221,263,'');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('吴',1,2,229,280,'');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('晋',1,1,266,420,'始于晋代曹魏，初期吴还在');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('西晋',1,2,266,316,'');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('东晋',1,2,317,420,'');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('南北朝',1,1,420,589,'南北朝时期，不是正式的朝代;起始时间未考虑北朝');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('宋',1,2,420,479,'南朝');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('齐',1,2,479,502,'南朝');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('梁',1,2,502,557,'南朝');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('陈',1,2,557,589,'南朝，581年隋已建立，被隋攻灭');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('北魏',1,2,386,557,'北朝');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('东魏',1,3,534,550,'北朝');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('北齐',1,2,550,577,'北朝，高齐，代东魏，被北周攻灭');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('西魏',1,3,534,557,'北朝');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('北周',1,2,557,581,'北朝，宇文周，代西魏');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('隋',1,1,581,618,'');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('唐',1,1,618,907,'618年李渊称帝');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('五代十国',1,1,907,960,'');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('梁',1,2,907,923,'后梁');

insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('宋',1,1,960,1279,'');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('北宋',1,2,960,1127,'');
insert into period(periodnameid, periodtype, periodlevel, beginyear, endyear,comment) values('南宋',1,2,1127,1279,'');