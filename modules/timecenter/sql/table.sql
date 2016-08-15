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