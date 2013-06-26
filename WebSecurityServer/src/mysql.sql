
use mysql;

show databases;
create database bookingnow;

grant all privileges on bookingnow.* to 'testUser'@'localhost' identified by '123456';
grant all privileges on bookingnow.* to 'testUser'@'%' identified by '123456';
flush privileges;





create table pitaya_table(
	id bigint not null auto_increment primary key,
	seatCount integer,
	maxCustomerCount integer,
	address varchar(50)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_table(id,seatCount,maxCustomerCount,address) values (1,4,6,'in the first floor');	
insert into pitaya_table(id,seatCount,maxCustomerCount,address) values (2,4,6,'in the second floor');
insert into pitaya_table(id,seatCount,maxCustomerCount,address) values (3,6,8,'in the first floor');
insert into pitaya_table(id,seatCount,maxCustomerCount,address) values (4,6,8,'in the second floor');
insert into pitaya_table(id,seatCount,maxCustomerCount,address) values (5,8,8,'in the first floor');
insert into pitaya_table(id,seatCount,maxCustomerCount,address) values (6,8,8,'in the second floor');
insert into pitaya_table(id,seatCount,maxCustomerCount,address) values (7,10,6,'in the first floor');
insert into pitaya_table(id,seatCount,maxCustomerCount,address) values (8,10,6,'in the second floor');
insert into pitaya_table(id,seatCount,maxCustomerCount,address) values (9,4,6,'第一层');
insert into pitaya_table(id,seatCount,maxCustomerCount,address) values (10,4,6,'第二层');


create table pitaya_order(
	id bigint not null auto_increment primary key,
	status integer,
	modifyTime timestamp,
	allowance integer,
	submit_time timestamp,
	enabled boolean
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
	
insert into pitaya_order values (1,1,'2013-05-08 01:12:03',95,'2013-05-08 01:02:03',true);
insert into pitaya_order values (2,2,'2013-05-08 01:12:03',90,'2013-05-08 01:02:03',false);
insert into pitaya_order values (3,3,'2013-05-08 01:12:03',95,'2013-05-08 01:02:03',1);
insert into pitaya_order values (4,4,'2013-05-08 01:12:03',100,'2013-05-08 01:02:03',0);
insert into pitaya_order values (5,5,'2013-05-08 01:12:03',90,'2013-05-08 01:02:03',true);
insert into pitaya_order values (6,6,'2013-05-08 01:12:03',95,'2013-05-08 01:02:03',false);
insert into pitaya_order values (7,3,'2013-05-08 01:12:03',90,'2013-05-08 01:02:03',true);

create table pitaya_order_table_detail(
	id bigint not null auto_increment primary key,
	status integer,
	realCustomerCount integer,
	enabled boolean,
	table_id bigint not null,
	order_id bigint not null,
	KEY fk_order_table_detail_table (table_id),
	KEY fk_order_table_detail_order (order_id),
	CONSTRAINT fk_order_table_detail_table FOREIGN KEY (table_id) REFERENCES pitaya_table (id),
	CONSTRAINT fk_order_table_detail_order FOREIGN KEY (order_id) REFERENCES pitaya_order (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_order_table_detail values (1,101,0,false,1,1);
insert into pitaya_order_table_detail values (2,102,4,true,2,2);
insert into pitaya_order_table_detail values (3,102,3,true,3,2);
insert into pitaya_order_table_detail values (4,103,7,true,4,3);
insert into pitaya_order_table_detail values (5,102,0,true,5,4);
insert into pitaya_order_table_detail values (6,102,8,true,6,5);
insert into pitaya_order_table_detail values (7,103,7,true,7,5);
insert into pitaya_order_table_detail values (8,102,0,true,8,6);
insert into pitaya_order_table_detail values (9,101,0,true,9,6);
insert into pitaya_order_table_detail values (10,103,3,true,10,7);


create table pitaya_food(
	id bigint not null auto_increment primary key,
	name varchar(50),
	version integer,
	period timestamp,
	price decimal(8,2),
	category integer,
	description varchar(100)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
	
insert into pitaya_food values (1,'Sweet and sour pork ribs',1,'2013-05-01 01:02:03',21.50,501,'good food');
insert into pitaya_food values (2,'Tomato soup',1,'2013-05-01 01:02:03',11.50,501,'good food');
insert into pitaya_food values (3,'Green pepper and potato silk',1,'2013-05-01 01:02:03',10,501,'good food');
insert into pitaya_food values (4,'Griddle chicken',1,'2013-05-01 01:02:03',21.50,501,'good food');
insert into pitaya_food values (5,'Saliva chicken',1,'2013-05-01 01:02:03',61.50,501,'good food');
insert into pitaya_food values (6,'hamburger',1,'2013-05-01 01:02:03',11.50,501,'good food');
insert into pitaya_food values (7,'Saliva chicken',1,'2013-05-01 01:02:03',21.50,501,'good food');
insert into pitaya_food values (8,'Saliva chicken',1,'2013-05-01 01:02:03',21.50,501,'good food');
insert into pitaya_food values (9,'Saliva chicken',1,'2013-05-01 01:02:03',21.50,501,'good food');
insert into pitaya_food values (10,'Saliva chicken',1,'2013-05-01 01:02:03',31.50,501,'good food');
insert into pitaya_food values (11,'Saliva chicken',1,'2013-05-01 01:02:03',41.50,501,'good food');
insert into pitaya_food values (12,'Saliva chicken',1,'2013-05-01 01:02:03',11.50,501,'good food');
insert into pitaya_food values (13,'Saliva chicken',1,'2013-05-01 01:02:03',21.50,501,'good food');
insert into pitaya_food values (14,'Saliva chicken',1,'2013-05-01 01:02:03',11.50,501,'good food');
insert into pitaya_food values (15,'Saliva chicken',1,'2013-05-01 01:02:03',21.50,501,'good food');
insert into pitaya_food values (16,'Saliva chicken',1,'2013-05-01 01:02:03',13.50,501,'good food');
insert into pitaya_food values (17,'Saliva chicken',1,'2013-05-01 01:02:03',24.50,501,'good food');
insert into pitaya_food values (18,'Saliva chicken',1,'2013-05-01 01:02:03',25.50,501,'good food');
insert into pitaya_food values (19,'Saliva chicken',1,'2013-05-01 01:02:03',26.50,501,'good food');
insert into pitaya_food values (20,'鱼香茄子',1,'2013-05-01 01:02:03',27.50,501,'good food');


create table pitaya_order_food_detail(
	id bigint not null auto_increment primary key,
	status integer,
	count integer,
	preference integer,
	last_modify_time timestamp,
	enabled boolean,
	food_id bigint not null,
	order_id bigint not null,
	KEY fk_order_food_detail_food (food_id),
	KEY fk_order_food_detail_order (order_id),
	CONSTRAINT fk_order_food_detail_food FOREIGN KEY (food_id) REFERENCES pitaya_food (id),
	CONSTRAINT fk_order_food_detail_order FOREIGN KEY (order_id) REFERENCES pitaya_order (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_order_food_detail values (1,301,1,401,'2013-05-01 01:02:03',true,1,1);
insert into pitaya_order_food_detail values (2,302,1,402,'2013-05-01 01:02:03',true,2,1);
insert into pitaya_order_food_detail values (3,303,1,403,'2013-05-01 01:02:03',true,3,1);
insert into pitaya_order_food_detail values (4,304,2,403,'2013-05-01 01:02:03',true,4,2);
insert into pitaya_order_food_detail values (5,304,1,401,'2013-05-01 01:02:03',true,5,2);
insert into pitaya_order_food_detail values (6,301,3,402,'2013-05-01 01:02:03',true,6,2);
insert into pitaya_order_food_detail values (7,301,1,403,'2013-05-01 01:02:03',true,7,2);
insert into pitaya_order_food_detail values (8,302,2,403,'2013-05-01 01:02:03',true,8,3);
insert into pitaya_order_food_detail values (9,302,1,402,'2013-05-01 01:02:03',true,9,4);
insert into pitaya_order_food_detail values (10,303,1,401,'2013-05-01 01:02:03',true,10,5);
insert into pitaya_order_food_detail values (11,302,2,402,'2013-05-01 01:02:03',true,11,5);
insert into pitaya_order_food_detail values (12,303,1,403,'2013-05-01 01:02:03',true,12,6);
insert into pitaya_order_food_detail values (13,304,3,401,'2013-05-01 01:02:03',true,13,6);
insert into pitaya_order_food_detail values (14,302,1,402,'2013-05-01 01:02:03',true,14,6);
insert into pitaya_order_food_detail values (15,303,4,403,'2013-05-01 01:02:03',true,15,6);
insert into pitaya_order_food_detail values (16,301,1,402,'2013-05-01 01:02:03',true,16,7);
insert into pitaya_order_food_detail values (17,304,2,401,'2013-05-01 01:02:03',true,17,7);
insert into pitaya_order_food_detail values (18,302,1,403,'2013-05-01 01:02:03',true,18,7);


create table pitaya_food_picture(
	id bigint not null auto_increment primary key,
	big_image mediumblob,
	small_image mediumblob,
	enabled boolean,
	food_id bigint not null,
	KEY fk_food_picture_food (food_id),
	CONSTRAINT fk_food_picture_food FOREIGN KEY (food_id) REFERENCES pitaya_food (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_food_picture values (1,'\cc\dd\cc\dd','\cc\dd',true,1);
insert into pitaya_food_picture values (2,'\cc\dd\cc\dd','\cc\dd',true,2);
insert into pitaya_food_picture values (3,'\cc\dd\cc\dd','\cc\dd',true,3);
insert into pitaya_food_picture values (4,'\cc\dd\cc\dd','\cc\dd',true,4);
insert into pitaya_food_picture values (5,'\cc\dd\cc\dd','\cc\dd',true,5);
insert into pitaya_food_picture values (6,'\cc\dd\cc\dd','\cc\dd',true,6);
insert into pitaya_food_picture values (7,'\cc\dd\cc\dd','\cc\dd',true,7);
insert into pitaya_food_picture values (8,'\cc\dd\cc\dd','\cc\dd',true,8);
insert into pitaya_food_picture values (9,'\cc\dd\cc\dd','\cc\dd',true,9);
insert into pitaya_food_picture values (10,'\cc\dd\cc\dd','\cc\dd',true,10);
insert into pitaya_food_picture values (11,'\cc\dd\cc\dd','\cc\dd',true,11);
insert into pitaya_food_picture values (12,'\cc\dd\cc\dd','\cc\dd',true,12);
insert into pitaya_food_picture values (13,'\cc\dd\cc\dd','\cc\dd',true,13);
insert into pitaya_food_picture values (14,'\cc\dd\cc\dd','\cc\dd',true,14);
insert into pitaya_food_picture values (15,'\cc\dd\cc\dd','\cc\dd',true,15);
insert into pitaya_food_picture values (16,'\cc\dd\cc\dd','\cc\dd',true,16);
insert into pitaya_food_picture values (17,'\cc\dd\cc\dd','\cc\dd',true,17);
insert into pitaya_food_picture values (18,'\cc\dd\cc\dd','\cc\dd',true,18);
insert into pitaya_food_picture values (19,'\cc\dd\cc\dd','\cc\dd',true,19);
insert into pitaya_food_picture values (20,'\cc\dd\cc\dd','\cc\dd',true,20);


create table pitaya_customer(
	id bigint not null auto_increment primary key,
	name varchar(50),
	account varchar(50),
	password varchar(30),
	phone varchar(30),
	sex integer,
	email varchar(50),
	address varchar(100),
	birthday timestamp
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_customer values (1,'zhang','zhang','123456','13579024832',601,'zhang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03');
insert into pitaya_customer values (2,'li','li','123456','13579024832',602,'li@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03');
insert into pitaya_customer values (3,'wang di','wang di','123456','13579024832',601,'wang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03');
insert into pitaya_customer values (4,'cheng','cheng','123456','13579024832',602,'cheng@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03');
insert into pitaya_customer values (5,'yang','yang','123456','13579024832',601,'yang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03');
insert into pitaya_customer values (6,'周丽','周丽','123456','13579024832',602,'zhou@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03');

create table pitaya_customer_picture(
	id bigint not null auto_increment primary key,
	image mediumblob,
	enabled boolean,
	customer_id bigint not null,
	KEY fk_customer_picture_customer (customer_id),
	CONSTRAINT fk_customer_picture_customer FOREIGN KEY (customer_id) REFERENCES pitaya_customer (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_customer_picture values (1,'\cc\dd\cc\dd',true,1);
insert into pitaya_customer_picture values (2,'\cc\dd\cc\dd',true,2);
insert into pitaya_customer_picture values (3,'\cc\dd\cc\dd',true,3);
insert into pitaya_customer_picture values (4,'\cc\dd\cc\dd',true,4);
insert into pitaya_customer_picture values (5,'\cc\dd\cc\dd',true,5);
insert into pitaya_customer_picture values (6,'\cc\dd\cc\dd',true,6);

create table pitaya_order_customer_detail(
	id bigint not null auto_increment primary key,
	count integer,
	enabled boolean,
	customer_id bigint not null,
	order_id bigint not null,
	KEY fk_order_customer_detail_customer (customer_id),
	KEY fk_order_customer_detail_order (order_id),
	CONSTRAINT fk_order_customer_detail_customer FOREIGN KEY (customer_id) REFERENCES pitaya_customer (id),
	CONSTRAINT fk_order_customer_detail_order FOREIGN KEY (order_id) REFERENCES pitaya_order (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
	
insert into pitaya_order_customer_detail values (1,4,true,1,1);	
insert into pitaya_order_customer_detail values (2,3,true,1,2);	
insert into pitaya_order_customer_detail values (3,2,true,1,4);	
insert into pitaya_order_customer_detail values (4,6,true,1,6);	
insert into pitaya_order_customer_detail values (5,8,true,1,7);	
	
	


create table pitaya_user(
	id bigint not null auto_increment primary key,
	account varchar(50),
	name varchar(50),
	password varchar(30),
	phone varchar(30),
	sex integer,
	email varchar(50),
	address varchar(100),
	birthday timestamp,
	description varchar(100),
	department integer,
	sub_system integer
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_user values (1,'hang','hang','123456','13579024832',701,'zhang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03','I am a cooker',801,1000);
insert into pitaya_user values (2,'lili','lili','123456','13579024832',702,'li@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03','I am a waiter',801,1000);
insert into pitaya_user values (3,'ang di','ang di','123456','13579024832',701,'wang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03','I am a boss',801,1000);
insert into pitaya_user values (4,'che','che','123456','13579024832',702,'cheng@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03','I am a casher',801,1000);
insert into pitaya_user values (5,'yan','yan','123456','13579024832',701,'yang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03','I am a service man',801,1000);
insert into pitaya_user values (6,'周帅','周帅','123456','13579024832',702,'zhou@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china','1986-05-01 01:02:03','I am a cooker',801,1000);

create table pitaya_user_picture(
	id bigint not null auto_increment primary key,
	image mediumblob,
	enabled boolean,
	user_id bigint not null,
	KEY fk_user_picture_user (user_id),
	CONSTRAINT fk_user_picture_user FOREIGN KEY (user_id) REFERENCES pitaya_user (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_user_picture values (1,'\cc\dd\cc\dd',true,1);
insert into pitaya_user_picture values (2,'\cc\dd\cc\dd',true,2);
insert into pitaya_user_picture values (3,'\cc\dd\cc\dd',true,3);
insert into pitaya_user_picture values (4,'\cc\dd\cc\dd',true,4);
insert into pitaya_user_picture values (5,'\cc\dd\cc\dd',true,5);
insert into pitaya_user_picture values (6,'\cc\dd\cc\dd',true,6);	

create table pitaya_order_user_detail(
	id bigint not null auto_increment primary key,
	enabled boolean,
	user_id bigint not null,
	order_id bigint not null,
	KEY fk_order_user_detail_user (user_id),
	KEY fk_order_user_detail_order (order_id),
	CONSTRAINT fk_order_user_detail_user FOREIGN KEY (user_id) REFERENCES pitaya_user (id),
	CONSTRAINT fk_order_user_detail_order FOREIGN KEY (order_id) REFERENCES pitaya_order (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_order_user_detail values (1,true,1,3);
insert into pitaya_order_user_detail values (2,true,1,5);



create table pitaya_material(
	id bigint not null auto_increment primary key,
	name varchar(50),
	description varchar(100),
	category integer
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_material values (1,'Garlic','big garlic,haha',901);
insert into pitaya_material values (2,'Onions','big garlic,haha',901);
insert into pitaya_material values (3,'salt','big garlic,haha',901);
insert into pitaya_material values (4,'Soy sauce','big garlic,haha',901);
insert into pitaya_material values (5,'花椒','big garlic,haha',901);


create table pitaya_food_material_detail(
	id bigint not null auto_increment primary key,
	enabled boolean,
	food_id bigint not null,
	material_id bigint not null,
	KEY fk_order_user_detail_user (food_id),
	KEY fk_order_user_detail_order (material_id),
	CONSTRAINT fk_food_material_detail_food FOREIGN KEY (food_id) REFERENCES pitaya_food (id),
	CONSTRAINT fk_food_material_detail_material FOREIGN KEY (material_id) REFERENCES pitaya_material (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_food_material_detail values (1,true,1,1);
insert into pitaya_food_material_detail values (2,true,1,2);
insert into pitaya_food_material_detail values (3,true,1,3);
insert into pitaya_food_material_detail values (4,true,2,1);
insert into pitaya_food_material_detail values (5,true,2,4);
insert into pitaya_food_material_detail values (6,true,3,5);
insert into pitaya_food_material_detail values (7,true,3,4);
insert into pitaya_food_material_detail values (8,true,6,2);
insert into pitaya_food_material_detail values (9,true,9,3);
insert into pitaya_food_material_detail values (10,true,10,1);



create table pitaya_role(
	id bigint not null auto_increment primary key,
	name varchar(20),
	description varchar(100),
	issys boolean,
	module integer
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_role values (1,'ROLE_USER','normal user',false,1101);
insert into pitaya_role values (2,'ROLE_ANONYMOUS','Anonymous',false,1101);
insert into pitaya_role values (3,'ROLE_CHEF','chef',false,1101);
insert into pitaya_role values (4,'ROLE_WAITER','waiter',false,1101);
insert into pitaya_role values (5,'ROLE_CASHIER','cashier',false,1101);
insert into pitaya_role values (6,'ROLE_MANAGER','manager',false,1101);
insert into pitaya_role values (7,'ROLE_ADMIN','超级用户',false,1101);




create table pitaya_authority(
	id bigint not null auto_increment primary key,
	name varchar(20),
	description varchar(100),
	issys boolean,
	module integer
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
	
insert into pitaya_authority values (1,'ROLE_USER','authority user',false,1101);
insert into pitaya_authority values (2,'ROLE_ANONYMOUS','authority Anonymous',false,1101);
insert into pitaya_authority values (3,'ROLE_CHEF','authority chef',false,1101);
insert into pitaya_authority values (4,'ROLE_WAITER','authority waiter',false,1101);
insert into pitaya_authority values (5,'ROLE_CASHIER','authority cashier',false,1101);
insert into pitaya_authority values (6,'ROLE_MANAGER','authority manager',false,1101);
insert into pitaya_authority values (7,'ROLE_ADMIN','授权超级用户',1,1101);


create table pitaya_resource(
	id bigint not null auto_increment primary key,
	name varchar(50),
	type integer,
	priority integer,
	string varchar(100),
	description varchar(100),
	issys boolean,
	module integer
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
	
insert into pitaya_resource values (1,'order resource',1204,1,'/Page/Common/error.jsp','error pages',true,1101);
insert into pitaya_resource values (2,'login resource',1204,1,'/Page/Common/login.jsp','order pages',true,1101);
insert into pitaya_resource values (3,'register user resource',1204,1,'/Page/Common/registerUser.jsp','order pages',true,1101);
insert into pitaya_resource values (4,'session time out resource',1204,1,'/Page/Common/sessionTimeout.jsp','order pages',true,1101);
insert into pitaya_resource values (5,'success resource',1204,1,'/Page/Common/success.jsp','order pages',true,1101);
insert into pitaya_resource values (6,'delete resource',1204,1,'/Page/Common/deleteUser.jsp','order pages',true,1101);
insert into pitaya_resource values (7,'food resource',1204,1,'/Page/Common/food.jsp','order pages',true,1101);
insert into pitaya_resource values (8,'menu resource',1204,1,'/Page/Common/menu.jsp','order pages',true,1101);
insert into pitaya_resource values (9,'table resource',1204,1,'/Page/Common/table.jsp','order pages',true,1101);
insert into pitaya_resource values (10,'update user resource',1204,1,'/Page/Common/updateUser.jsp','order pages',true,1101);




create table pitaya_user_role_detail(
	id bigint not null auto_increment primary key,
	enabled boolean,
	role_id bigint not null,
	user_id bigint not null,
	KEY fk_user_role_detail_role (role_id),
	KEY fk_user_role_detail_user (user_id),
	CONSTRAINT fk_user_role_detail_role FOREIGN KEY (role_id) REFERENCES pitaya_role (id),
	CONSTRAINT fk_user_role_detail_user FOREIGN KEY (user_id) REFERENCES pitaya_user (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_user_role_detail values (1,true,1,1);
insert into pitaya_user_role_detail values (2,true,2,2);
insert into pitaya_user_role_detail values (3,true,3,3);
insert into pitaya_user_role_detail values (4,true,4,4);
insert into pitaya_user_role_detail values (5,true,5,5);
insert into pitaya_user_role_detail values (6,true,7,6);




create table pitaya_role_authority_detail(
	id bigint not null auto_increment primary key,
	enabled boolean,
	role_id bigint not null,
	authority_id bigint not null,
	KEY fk_role_authority_detail_role (role_id),
	KEY fk_role_authority_detail_authority (authority_id),
	CONSTRAINT fk_role_authority_detail_role FOREIGN KEY (role_id) REFERENCES pitaya_role (id),
	CONSTRAINT fk_role_authority_detail_authority FOREIGN KEY (authority_id) REFERENCES pitaya_authority (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_role_authority_detail values (1,true,1,1);
insert into pitaya_role_authority_detail values (2,true,1,2);
insert into pitaya_role_authority_detail values (3,true,1,3);
insert into pitaya_role_authority_detail values (4,true,2,1);
insert into pitaya_role_authority_detail values (5,true,2,2);
insert into pitaya_role_authority_detail values (6,true,2,4);
insert into pitaya_role_authority_detail values (7,true,3,1);
insert into pitaya_role_authority_detail values (8,true,3,2);
insert into pitaya_role_authority_detail values (9,true,4,1);
insert into pitaya_role_authority_detail values (10,true,4,5);
insert into pitaya_role_authority_detail values (11,true,5,1);
insert into pitaya_role_authority_detail values (12,true,5,2);
insert into pitaya_role_authority_detail values (13,true,5,3);
insert into pitaya_role_authority_detail values (14,true,6,1);
insert into pitaya_role_authority_detail values (15,true,6,2);
insert into pitaya_role_authority_detail values (16,true,6,3);
insert into pitaya_role_authority_detail values (17,true,7,1);
insert into pitaya_role_authority_detail values (18,true,7,2);
insert into pitaya_role_authority_detail values (19,true,7,3);
insert into pitaya_role_authority_detail values (20,true,7,4);


SET FOREIGN_KEY_CHECKS=0;

create table pitaya_authority_resource_detail(
	id bigint not null auto_increment primary key,
	enabled boolean,
	resource_id bigint not null,
	authority_id bigint not null,
	KEY fk_authority_resource_detail_resource (resource_id),
	KEY fk_authority_resource_detail_authority (authority_id),
	CONSTRAINT fk_authority_resource_detail_resource FOREIGN KEY (resource_id) REFERENCES pitaya_resource (id),
	CONSTRAINT fk_authority_resource_detail_authority FOREIGN KEY (authority_id) REFERENCES pitaya_authority (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_authority_resource_detail values (1,true,1,1);
insert into pitaya_authority_resource_detail values (2,true,2,1);
insert into pitaya_authority_resource_detail values (3,true,3,1);
insert into pitaya_authority_resource_detail values (4,true,4,1);
insert into pitaya_authority_resource_detail values (5,true,5,1);
insert into pitaya_authority_resource_detail values (6,true,6,1);
insert into pitaya_authority_resource_detail values (7,true,7,1);
insert into pitaya_authority_resource_detail values (8,true,8,1);
insert into pitaya_authority_resource_detail values (9,true,9,1);
insert into pitaya_authority_resource_detail values (10,true,10,1);
insert into pitaya_authority_resource_detail values (11,true,1,2);
insert into pitaya_authority_resource_detail values (12,true,2,2);
insert into pitaya_authority_resource_detail values (13,true,3,2);
insert into pitaya_authority_resource_detail values (14,true,4,2);
insert into pitaya_authority_resource_detail values (15,true,5,2);
insert into pitaya_authority_resource_detail values (16,true,6,2);
insert into pitaya_authority_resource_detail values (17,true,7,2);
insert into pitaya_authority_resource_detail values (18,true,8,2);
insert into pitaya_authority_resource_detail values (19,true,5,3);
insert into pitaya_authority_resource_detail values (20,true,6,3);
insert into pitaya_authority_resource_detail values (21,true,7,3);
insert into pitaya_authority_resource_detail values (22,true,8,3);
insert into pitaya_authority_resource_detail values (23,true,9,3);
insert into pitaya_authority_resource_detail values (24,true,10,3);
insert into pitaya_authority_resource_detail values (25,true,6,4);
insert into pitaya_authority_resource_detail values (26,true,7,4);
insert into pitaya_authority_resource_detail values (27,true,9,4);
insert into pitaya_authority_resource_detail values (28,true,3,5);
insert into pitaya_authority_resource_detail values (29,true,4,5);
insert into pitaya_authority_resource_detail values (30,true,5,5);
insert into pitaya_authority_resource_detail values (31,true,6,6);
insert into pitaya_authority_resource_detail values (32,true,7,6);
insert into pitaya_authority_resource_detail values (33,true,8,6);
insert into pitaya_authority_resource_detail values (34,true,1,7);
insert into pitaya_authority_resource_detail values (35,true,2,7);
insert into pitaya_authority_resource_detail values (36,true,3,7);
insert into pitaya_authority_resource_detail values (37,true,4,7);
insert into pitaya_authority_resource_detail values (38,true,5,7);
insert into pitaya_authority_resource_detail values (39,true,6,7);
insert into pitaya_authority_resource_detail values (40,true,7,7);
insert into pitaya_authority_resource_detail values (41,true,8,7);
insert into pitaya_authority_resource_detail values (42,true,9,7);
insert into pitaya_authority_resource_detail values (43,true,10,7);







