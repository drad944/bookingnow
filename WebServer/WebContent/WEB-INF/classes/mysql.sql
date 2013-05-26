
use mysql;

show databases;
create database bookingnow;

grant all privileges on bookingnow.* to 'testUser'@'localhost' identified by '123456';
grant all privileges on bookingnow.* to 'testUser'@'%' identified by '123456';
flush privileges;




create table pitaya_user(uid bigint not null auto_increment primary key,name varchar(20),passwd varchar(20),role varchar(10),sex varchar(2),birthday date);

insert into pitaya_user values (1,'zhang','123456','boss','m','1971/5/8');



	
	