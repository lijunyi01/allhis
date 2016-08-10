/*建库*/
create database allhis default character set utf8;


drop table if exists mailqueue ;
create table mailqueue(
    id int not null auto_increment,
    yearname varchar(100) not null,
    firstyear int,
    lastyear int,
    primary key  (`id`),
    KEY `allkey` (`firstyear`)
)ENGINE = MYISAM;