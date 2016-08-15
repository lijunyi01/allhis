/*建库*/
create database allhis default character set utf8;

-- 用于通过年号查公元年份
drop table if exists yearbase;
create table yearbase(
    id int not null auto_increment,
    yearname varchar(100) not null,
    firstyear int,
    lastyear int,
    primary key  (`id`),
    UNIQUE KEY (`yearname`)
)ENGINE = MYISAM;

insert into yearbase(yearname,firstyear) values('鲁隐公',-722);
insert into yearbase(yearname,firstyear) values('鲁庄公',-693);
insert into yearbase(yearname,firstyear) values('周敬王',-519);
insert into yearbase(yearname,firstyear) values('周元王',-475);
insert into yearbase(yearname,firstyear) values('周赧王',-314);
insert into yearbase(yearname,firstyear) values('成化',1465);
insert into yearbase(yearname,firstyear) values('洪宪',1916);
insert into yearbase(yearname,firstyear) values('民国',1912);

-- 历史时期表，用于记录各个历史时期的区间
drop table if exists period;
create table period(
    id int not null auto_increment,
    periodname varchar(100) not null,
    -- 历史时期的类型，例如：中国历史时期，埃及历史时期等
    periodtype int,
    -- 历史时期的级别，1:朝代或时期  2:子时期  3:二级子时期  10: 帝系  11:年号
    periodlevel int,
    beginyear int not null,
    endyear int not null,
    -- 备注
    note VARCHAR(256) null,
    primary key  (`id`),
    KEY (`beginyear`),
    KEY (`endyear`)
)ENGINE = MYISAM;

insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('周',1,1,-1047,-256,'起始时间有争论');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('西周',1,2,-1047,-771,'起始时间有争论');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('东周',1,2,-770,-256,'周赧王末年');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('春秋',1,3,-770,-476,'与孔子所修订的《春秋》年代（公元前722年-公元前481年）大体相当；且－476年为周敬王末年');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('战国',1,3,-475,-221,'《史记》以周元王元年为始叙述六国时事');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('秦',1,1,-221,-207,'-207为二世卒年；子婴降');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('西楚',1,1,-206,-202,'');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('汉',1,1,-202,220,'讫于曹丕废汉');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('西汉',1,2,-202,8,'8年12月王莽篡汉');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('新',1,2,9,23,'');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('东汉',1,2,25,220,'讫于曹丕废汉');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('三国',1,1,220,266,'三国时期，不是正式的朝代，一般以曹魏为此时期正统');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('魏',1,2,220,266,'曹丕于220年12月代汉称帝');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('蜀',1,2,221,263,'');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('吴',1,2,229,280,'');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('晋',1,1,266,420,'始于晋代曹魏，初期吴还在');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('西晋',1,2,266,316,'');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('东晋',1,2,317,420,'');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('南北朝',1,1,420,589,'南北朝时期，不是正式的朝代;起始时间未考虑北朝');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('宋',1,2,420,479,'南朝');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('齐',1,2,479,502,'南朝');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('梁',1,2,502,557,'南朝');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('陈',1,2,557,589,'南朝，581年隋已建立，被隋攻灭');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('北魏',1,2,386,557,'北朝');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('东魏',1,3,534,550,'北朝');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('北齐',1,2,550,577,'北朝，高齐，代东魏，被北周攻灭');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('西魏',1,3,534,557,'北朝');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('北周',1,2,557,581,'北朝，宇文周，代西魏');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('隋',1,1,581,618,'');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('唐',1,1,618,907,'618年李渊称帝');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('五代十国',1,1,907,960,'');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('梁',1,2,907,923,'后梁');

insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('宋',1,1,960,1279,'');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('北宋',1,2,960,1127,'');
insert into period(periodname, periodtype, periodlevel, beginyear, endyear,note) values('南宋',1,2,1127,1279,'');