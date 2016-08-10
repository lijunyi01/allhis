/*建库*/
create database allhis default character set utf8;


drop table if exists yearbase ;
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
insert into yearbase(yearname,firstyear) values('成化',1465);
insert into yearbase(yearname,firstyear) values('洪宪',1916);
insert into yearbase(yearname,firstyear) values('民国',1912);