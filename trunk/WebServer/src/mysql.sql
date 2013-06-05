
use mysql;

show databases;
create database bookingnow;

grant all privileges on bookingnow.* to 'testUser'@'localhost' identified by '123456';
grant all privileges on bookingnow.* to 'testUser'@'%' identified by '123456';
flush privileges;




create table pitaya_account(aid integer not null auto_increment primary key,name varchar(20),password varchar(20),role varchar(10),sex varchar(2),createDateTime timestamp,birthday timestamp);

insert into pitaya_account values (1,'zhang','123456','boss','m','1971-05-08 01:02:03','1971-05-08  00:00:00');



	
	