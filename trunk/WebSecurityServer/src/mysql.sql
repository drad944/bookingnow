
show databases;
create database bookingnow;

grant all privileges on bookingnow.* to 'testUser'@'localhost' identified by '123456';
grant all privileges on bookingnow.* to 'testUser'@'%' identified by '123456';
flush privileges;





create table pitaya_table(
	id bigint not null auto_increment primary key,
	status integer,
	minCustomerCount integer,
	maxCustomerCount integer,
	address varchar(50),
	indoorPrice double
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_table values (1,2,1,2,'in the first floor',0);	
insert into pitaya_table values (2,2,1,2,'in the second floor',50);
insert into pitaya_table values (3,2,2,5,'in the first floor',0);
insert into pitaya_table values (4,3,2,5,'in the second floor',50);
insert into pitaya_table values (5,3,4,7,'in the first floor',0);
insert into pitaya_table values (6,3,4,7,'in the second floor',60);
insert into pitaya_table values (7,4,6,9,'in the first floor',0);
insert into pitaya_table values (8,4,6,9,'in the second floor',80);
insert into pitaya_table values (9,4,8,11,'in the second floor',0);
insert into pitaya_table values (10,2,8,11,'in the first floor',0);


create table pitaya_order(
	id bigint not null auto_increment primary key,
	status integer,
	user_id bigint,
	customer_id bigint,
	customer_count integer,
	modifyTime bigint,
	allowance double,
	total_price double,
	prePay double,
	submit_time bigint,
	enabled boolean
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
	
insert into pitaya_order values (1,1,0,1,4,1351232321,0.95,0,0,1351232321,true);
insert into pitaya_order values (2,2,1,0,5,1351232321,0.90,0,0,1351232321,false);
insert into pitaya_order values (3,3,0,2,6,1351232321,0.95,0,0,1351232321,1);
insert into pitaya_order values (4,4,0,3,7,1351232321,1,0,0,1351232321,0);
insert into pitaya_order values (5,5,2,0,4,1351232321,0.90,0,0,1351232321,true);
insert into pitaya_order values (6,6,3,0,5,1351232321,0.95,0,0,1351232321,false);
insert into pitaya_order values (7,3,1,0,4,1351232321,0.90,0,0,1351232321,true);

create table pitaya_order_table_detail(
	id bigint not null auto_increment primary key,
	realCustomerCount integer,
	enabled boolean,
	table_id bigint not null,
	order_id bigint not null,
	KEY fk_order_table_detail_table (table_id),
	KEY fk_order_table_detail_order (order_id),
	CONSTRAINT fk_order_table_detail_table FOREIGN KEY (table_id) REFERENCES pitaya_table (id),
	CONSTRAINT fk_order_table_detail_order FOREIGN KEY (order_id) REFERENCES pitaya_order (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_order_table_detail values (1,0,false,1,1);
insert into pitaya_order_table_detail values (2,4,true,2,2);
insert into pitaya_order_table_detail values (3,3,true,3,2);
insert into pitaya_order_table_detail values (4,7,true,4,3);
insert into pitaya_order_table_detail values (5,0,true,5,4);
insert into pitaya_order_table_detail values (6,8,true,6,5);
insert into pitaya_order_table_detail values (7,7,true,7,5);
insert into pitaya_order_table_detail values (8,0,true,8,6);
insert into pitaya_order_table_detail values (9,0,true,9,6);
insert into pitaya_order_table_detail values (10,3,true,10,7);


create table pitaya_food(
	id bigint not null auto_increment primary key,
	recommendation integer,
	status integer,
	picture_id bigint,
	name varchar(50),
	version bigint,
	period bigint,
	price double,
	category varchar(50),
	description varchar(100)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
	
insert into pitaya_food values (1,5,1,1,'Sweet and sour pork ribs',1351232321,1351232321,11,'chinese food','good food');
insert into pitaya_food values (2,4,1,2,'Tomato soup',1351232321,1351232321,12,'chinese food','good food');
insert into pitaya_food values (3,5,1,3,'Green pepper and potato silk',1351232321,1351232321,13,'chinese food','good food');
insert into pitaya_food values (4,5,1,4,'Griddle chicken',1351232321,1351232321,14,'western food','good food');
insert into pitaya_food values (5,5,1,5,'Saliva chicken',1351232321,1351232321,15,'chinese food','good food');
insert into pitaya_food values (6,5,1,6,'hamburger',1351232321,1351232321,16,'western food','good food');
insert into pitaya_food values (7,5,1,7,'Saliva chicken',1351232321,1351232321,17,'chinese food','good food');
insert into pitaya_food values (8,5,1,8,'Saliva chicken',1351232321,1351232321,18,'chinese food','good food');
insert into pitaya_food values (9,5,1,9,'Saliva chicken',1351232321,1351232321,19,'chinese food','good food');
insert into pitaya_food values (10,5,1,10,'Saliva chicken',1351232321,1351232321,20,'chinese food','good food');
insert into pitaya_food values (11,5,1,11,'Saliva chicken',1351232321,1351232321,21,'western food','good food');
insert into pitaya_food values (12,5,1,12,'Saliva chicken',1351232321,1351232321,22,'sichuan food','good food');
insert into pitaya_food values (13,5,1,13,'Saliva chicken',1351232321,1351232321,23,'chinese food','good food');
insert into pitaya_food values (14,5,1,14,'Saliva chicken',1351232321,1351232321,24,'western food','good food');
insert into pitaya_food values (15,5,1,15,'Saliva chicken',1351232321,1351232321,25,'chinese food','good food');
insert into pitaya_food values (16,5,1,16,'Saliva chicken',1351232321,1351232321,26,'sichuan food','good food');
insert into pitaya_food values (17,5,1,17,'Saliva chicken',1351232321,1351232321,27,'chinese food','good food');
insert into pitaya_food values (18,5,1,18,'Saliva chicken',1351232321,1351232321,28,'sichuan food','good food');
insert into pitaya_food values (19,5,1,19,'Saliva chicken',1351232321,1351232321,28,'sichuan food','good food');
insert into pitaya_food values (20,5,1,20,'fish',1351232321,1351232321,30,'chinese food','good food');


create table pitaya_order_food_detail(
	id bigint not null auto_increment primary key,
	status integer,
	count integer,
	preference integer,
	last_modify_time bigint,
	enabled boolean,
	isFree boolean,
	food_id bigint not null,
	order_id bigint not null,
	KEY fk_order_food_detail_food (food_id),
	KEY fk_order_food_detail_order (order_id),
	CONSTRAINT fk_order_food_detail_food FOREIGN KEY (food_id) REFERENCES pitaya_food (id),
	CONSTRAINT fk_order_food_detail_order FOREIGN KEY (order_id) REFERENCES pitaya_order (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_order_food_detail values (1,301,1,401,1351232321,true,false,1,1);
insert into pitaya_order_food_detail values (2,302,1,402,1351232321,true,true,2,1);
insert into pitaya_order_food_detail values (3,303,1,403,1351232321,true,false,3,1);
insert into pitaya_order_food_detail values (4,304,2,403,1351232321,true,false,4,2);
insert into pitaya_order_food_detail values (5,304,1,401,1351232321,true,true,5,2);
insert into pitaya_order_food_detail values (6,301,3,402,1351232321,true,false,6,2);
insert into pitaya_order_food_detail values (7,301,1,403,1351232321,true,true,7,2);
insert into pitaya_order_food_detail values (8,302,2,403,1351232321,true,false,8,3);
insert into pitaya_order_food_detail values (9,302,1,402,1351232321,true,false,9,4);
insert into pitaya_order_food_detail values (10,303,1,401,1351232321,true,false,10,5);
insert into pitaya_order_food_detail values (11,302,2,402,1351232321,true,true,11,5);
insert into pitaya_order_food_detail values (12,303,1,403,1351232321,true,false,12,6);
insert into pitaya_order_food_detail values (13,304,3,401,1351232321,true,false,13,6);
insert into pitaya_order_food_detail values (14,302,1,402,1351232321,true,false,14,6);
insert into pitaya_order_food_detail values (15,303,4,403,1351232321,true,false,15,6);
insert into pitaya_order_food_detail values (16,301,1,402,1351232321,true,true,16,7);
insert into pitaya_order_food_detail values (17,304,2,401,1351232321,true,false,17,7);
insert into pitaya_order_food_detail values (18,302,1,403,1351232321,true,false,18,7);


create table pitaya_food_picture(
	id bigint not null auto_increment primary key,
	name varchar(50),
	version bigint,
	big_image mediumblob,
	small_image mediumblob,
	enabled boolean
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_food_picture values (1,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (2,'b',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (3,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (4,'c',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (5,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (6,'b',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (7,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (8,'b',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (9,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (10,'c',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (11,'c',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (12,'c',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (13,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (14,'b',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (15,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (16,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (17,'b',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (18,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (19,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);
insert into pitaya_food_picture values (20,'a',1351232321,'\cc\dd\cc\dd','\cc\dd',true);


create table pitaya_customer(
	id bigint not null auto_increment primary key,
	enabled boolean,
	picture_id bigint,
	name varchar(50),
	account varchar(50),
	password varchar(30),
	phone varchar(30),
	sex integer,
	email varchar(50),
	address varchar(100),
	birthday bigint
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_customer values (1,true,1,'zhang','zhang','123456','13579024832',601,'zhang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464);
insert into pitaya_customer values (2,true,2,'li','li','123456','13579024832',602,'li@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464);
insert into pitaya_customer values (3,true,3,'wang di','wang di','123456','13579024832',601,'wang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464);
insert into pitaya_customer values (4,true,4,'cheng','cheng','123456','13579024832',602,'cheng@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464);
insert into pitaya_customer values (5,true,5,'yang','yang','123456','13579024832',601,'yang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464);
insert into pitaya_customer values (6,true,6,'zhouli','zhouli','123456','13579024832',602,'zhou@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464);

create table pitaya_customer_picture(
	id bigint not null auto_increment primary key,
	name varchar(50),
	last_modify_time bigint,
	image mediumblob,
	enabled boolean
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_customer_picture values (1,'a',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_customer_picture values (2,'a',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_customer_picture values (3,'a',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_customer_picture values (4,'a',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_customer_picture values (5,'a',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_customer_picture values (6,'a',1351232321,'\cc\dd\cc\dd',true);



create table pitaya_user(
	id bigint not null auto_increment primary key,
	enabled boolean,
	picture_id bigint,
	account varchar(50),
	name varchar(50),
	password varchar(30),
	phone varchar(30),
	sex integer,
	email varchar(50),
	address varchar(100),
	birthday bigint,
	description varchar(100),
	department integer,
	sub_system integer
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_user values (1,true,1,'hang','hang','123456','13579024832',701,'zhang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464,'I am a cooker',801,1000);
insert into pitaya_user values (2,true,2,'lili','lili','123456','13579024832',702,'li@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464,'I am a waiter',801,1000);
insert into pitaya_user values (3,true,3,'ang di','ang di','123456','13579024832',701,'wang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464,'I am a boss',801,1000);
insert into pitaya_user values (4,true,4,'che','che','123456','13579024832',702,'cheng@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464,'I am a casher',801,1000);
insert into pitaya_user values (5,true,5,'yan','yan','123456','13579024832',701,'yang@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464,'I am a service man',801,1000);
insert into pitaya_user values (6,true,6,'zhoushuai','zhoushuai','123456','13579024832',702,'zhou@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464,'I am a cooker',801,1000);
insert into pitaya_user values (7,true,7,'admin','admin','123456','13579024832',702,'zhou@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464,'I am a cooker',801,1000);
insert into pitaya_user values (8,true,9,'mengfei','mengfei','123456','13579024832',702,'mengfei@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464,'I am a cooker',801,1000);
insert into pitaya_user values (9,true,9,'nidaye','nidaye','123456','13579024832',702,'nidaye@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464,'I am a cooker',801,1000);
insert into pitaya_user values (10,true,10,'laozi','laozi','123456','13579024832',702,'laozi@qq.com','shan xi road,gulou,nanjing city,jiangsu province,china',123465464,'I am a cooker',801,1000);



create table pitaya_user_picture(
	id bigint not null auto_increment primary key,
	name varchar(50),
	last_modify_time bigint,
	image mediumblob,
	enabled boolean
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_user_picture values (1,'a',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_user_picture values (2,'b',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_user_picture values (3,'c',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_user_picture values (4,'d',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_user_picture values (5,'e',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_user_picture values (6,'f',1351232321,'\cc\dd\cc\dd',true);	
insert into pitaya_user_picture values (7,'g',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_user_picture values (8,'h',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_user_picture values (9,'i',1351232321,'\cc\dd\cc\dd',true);
insert into pitaya_user_picture values (10,'g',1351232321,'\cc\dd\cc\dd',true);


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
insert into pitaya_material values (5,'huajiao','big garlic,haha',901);


create table pitaya_food_material_detail(
	id bigint not null auto_increment primary key,
	count integer,
	price double,
	weight double,
	enabled boolean,
	food_id bigint not null,
	material_id bigint not null,
	KEY fk_order_user_detail_user (food_id),
	KEY fk_order_user_detail_order (material_id),
	CONSTRAINT fk_food_material_detail_food FOREIGN KEY (food_id) REFERENCES pitaya_food (id),
	CONSTRAINT fk_food_material_detail_material FOREIGN KEY (material_id) REFERENCES pitaya_material (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_food_material_detail values (1,1,10,0,true,1,1);
insert into pitaya_food_material_detail values (2,1,20,2,true,1,2);
insert into pitaya_food_material_detail values (3,1,10,0,true,1,3);
insert into pitaya_food_material_detail values (4,0,10,3,true,2,1);
insert into pitaya_food_material_detail values (5,1,10,0,true,2,4);
insert into pitaya_food_material_detail values (6,0,10,12,true,3,5);
insert into pitaya_food_material_detail values (7,1,10,0,true,3,4);
insert into pitaya_food_material_detail values (8,0,10,2,true,6,2);
insert into pitaya_food_material_detail values (9,1,10,0,true,9,3);
insert into pitaya_food_material_detail values (10,1,10,0,true,10,1);



create table pitaya_role(
	id bigint not null auto_increment primary key,
	type integer,
	name varchar(50),
	description varchar(100),
	issys boolean,
	module integer
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_role values (1,1301,'ROLE_ANONYMOUS','Anonymous',false,1101);	
insert into pitaya_role values (2,1307,'ROLE_WAITER','waiter',false,1101);
insert into pitaya_role values (3,1303,'ROLE_CUSTOMER_VIP1','customer',false,1101);
insert into pitaya_role values (4,1304,'ROLE_CUSTOMER_VIP2','customer',false,1101);
insert into pitaya_role values (5,1305,'ROLE_WELCOME','welcome',false,1101);
insert into pitaya_role values (6,1306,'ROLE_CHEF','chef',false,1101);
insert into pitaya_role values (7,1307,'ROLE_WAITER','waiter',false,1101);
insert into pitaya_role values (8,1308,'ROLE_CASHIER','cashier',false,1101);
insert into pitaya_role values (9,1309,'ROLE_MANAGER','manager',false,1101);
insert into pitaya_role values (10,1310,'ROLE_ADMIN','administrator',1,1101);




create table pitaya_authority(
	id bigint not null auto_increment primary key,
	type integer,
	name varchar(50),
	description varchar(100),
	issys boolean,
	module integer
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into pitaya_authority values (1,1401,'AUTHORITY_ANONYMOUS','authority Anonymous',false,1101);	
insert into pitaya_authority values (2,1402,'AUTHORITY_CUSTOMER','authority customer',false,1101);
insert into pitaya_authority values (3,1403,'AUTHORITY_CUSTOMER_VIP1','authority customer',false,1101);
insert into pitaya_authority values (4,1404,'AUTHORITY_CUSTOMER_VIP2','authority customer',false,1101);
insert into pitaya_authority values (5,1405,'AUTHORITY_WELCOME','authority welcome',false,1101);
insert into pitaya_authority values (6,1406,'AUTHORITY_CHEF','authority chef',false,1101);
insert into pitaya_authority values (7,1407,'AUTHORITY_WAITER','authority waiter',false,1101);
insert into pitaya_authority values (8,1408,'AUTHORITY_CASHIER','authority cashier',false,1101);
insert into pitaya_authority values (9,1409,'AUTHORITY_MANAGER','authority manager',false,1101);
insert into pitaya_authority values (10,1410,'AUTHORITY_ADMIN','authority administrator',1,1101);


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
insert into pitaya_user_role_detail values (7,true,6,7);
insert into pitaya_user_role_detail values (8,true,8,8);
insert into pitaya_user_role_detail values (9,true,9,9);
insert into pitaya_user_role_detail values (10,true,10,10);



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
insert into pitaya_role_authority_detail values (3,true,8,3);
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
insert into pitaya_role_authority_detail values (15,true,10,2);
insert into pitaya_role_authority_detail values (16,true,6,3);
insert into pitaya_role_authority_detail values (17,true,7,1);
insert into pitaya_role_authority_detail values (18,true,9,2);
insert into pitaya_role_authority_detail values (19,true,7,3);
insert into pitaya_role_authority_detail values (20,true,7,4);
insert into pitaya_role_authority_detail values (21,true,6,5);
insert into pitaya_role_authority_detail values (22,true,7,9);
insert into pitaya_role_authority_detail values (23,true,7,6);
insert into pitaya_role_authority_detail values (24,true,7,7);
insert into pitaya_role_authority_detail values (25,true,8,8);
insert into pitaya_role_authority_detail values (26,true,7,4);
insert into pitaya_role_authority_detail values (27,true,9,9);
insert into pitaya_role_authority_detail values (28,true,7,10);
insert into pitaya_role_authority_detail values (29,true,10,4);
insert into pitaya_role_authority_detail values (30,true,10,8);

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
insert into pitaya_authority_resource_detail values (20,true,6,9);
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
insert into pitaya_authority_resource_detail values (36,true,3,8);
insert into pitaya_authority_resource_detail values (37,true,4,7);
insert into pitaya_authority_resource_detail values (38,true,5,9);
insert into pitaya_authority_resource_detail values (39,true,6,7);
insert into pitaya_authority_resource_detail values (40,true,7,10);
insert into pitaya_authority_resource_detail values (41,true,8,7);
insert into pitaya_authority_resource_detail values (42,true,9,8);
insert into pitaya_authority_resource_detail values (43,true,10,7);





