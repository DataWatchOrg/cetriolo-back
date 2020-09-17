create schema cetriolo;
 
use cetriolo;

create user 'user'@'localhost' identified by 'fatec';

grant select, insert, delete, update on cetriolo.* to user@'localhost';

create table usu_usuario (
	id bigint unsigned primary key auto_increment,
    usu_nome varchar(50) not null,
    usu_email varchar(50) not null,
    usu_telefone varchar(20)
);
create table mat_materia (
	mat_id bigint unsigned primary key auto_increment,
	mat_nome varchar(50) not null
);

create table uma_usuario_materia (
	usu_id bigint unsigned,
	mat_id bigint unsigned,
	primary key (usu_id, mat_id),
	constraint uma_usu_fk foreign key (usu_id) references usu_usuario (id),
	constraint uma_mat_fk foreign key (mat_id) references mat_materia (mat_id)
);

insert into usu_usuario(usu_nome, usu_email, usu_telefone) values ('Ana', 'ana@email.com', '984556723');
insert into mat_materia(mat_nome) values ('Algoritmos');
insert into uma_usuario_materia(usu_id, mat_id) values (1, 1);