/*建库*/
create database ah_authlog default character set utf8;

/* loginfrom:  pop3; web ;smtp; imap; app */
drop table if exists `login_history`;
create table `login_history` (
    umid         int not null ,
    logintime    datetime not null,
    loginip      varchar(50) null,
    loginfrom   varchar(50) null,
    key `i_1`(umid,logintime)
)ENGINE = MYISAM;

drop table if exists `login_history_m`;
create table `login_history_m` (
    umid         int not null ,
    logintime    datetime not null,
    loginip      varchar(50) null,
    loginfrom   varchar(50) null,
    key `i_1`(umid,logintime)
)ENGINE = MEMORY;

drop table if exists `loginfaillog`;
create table `loginfaillog` (
    umid         int not null ,
    logintime    datetime not null,
    loginip      varchar(50) null,
    loginfrom   varchar(50) null,
    errorcode   int,
    key `i_1`(umid,logintime)
)ENGINE = MYISAM;

drop table if exists `loginfaillog_m`;
create table `loginfaillog_m` (
    umid         int not null ,
    logintime    datetime not null,
    loginip      varchar(50) null,
    loginfrom   varchar(50) null,
    errorcode   int,
    key `i_1`(umid,logintime),
    key `i_2` (loginip)
)ENGINE = MEMORY;

