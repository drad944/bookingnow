
use mysql;

show databases;
create database bookingnow;

grant all privileges on bookingnow.* to 'testUser'@'localhost' identified by '123456';
grant all privileges on bookingnow.* to 'testUser'@'%' identified by '123456';
flush privileges;




create table pitaya_account(aid integer not null auto_increment primary key,name varchar(20),password varchar(20),role varchar(10),level integer,sex varchar(2),image blob,createDateTime timestamp,birthday timestamp);
insert into pitaya_account values (1,'zhang','123456','boss',10,'m','\cc\dd\cc\dd','1971-05-08 01:02:03','1971-05-08  00:00:00');


create table pitaya_dining_table(tid integer not null auto_increment primary key,seatCount integer,maxCustomerCount integer,realCustomerCount integer,tablestatus integer,place integer);
insert into pitaya_dining_table values (1,4,5,3,0,100);	


create table pitaya_order(oid integer not null auto_increment primary key, orderstatus integer,modifyTime timestamp,allowance integer);
insert into pitaya_order values (1,5,'1971-05-08 01:02:03',100);


create table pitaya_food(fid integer not null auto_increment primary key,name varchar(20),material varchar(50),image blob,version varchar(10),period timestamp,Chef_id integer,price DECIMAL(8,2));
insert into pitaya_food values (1,'∫Ï…’÷ÌÃ„','÷ÌÃ„,”Õ£¨—Œ£¨¥◊','\cc\dd\cc\dd','1.1','2011-05-08 01:02:03',1,25.50);


create table pitaya_food_process(pid integer not null auto_increment primary key,food_id integer,order_id integer,processStatus integer, favourite integer,remark varchar(10));
insert into pitaya_food_process values(1,1,1,2,3,'∫√≥‘µ√∫‹');





	