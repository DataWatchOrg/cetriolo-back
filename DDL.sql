create schema cetriolo;
 
use cetriolo;

create user 'user'@'localhost' identified by 'fatec';

grant select, insert, delete, update on cetriolo.* to user@'localhost';

create table per_permissao (
    per_id bigint unsigned primary key auto_increment,
    per_nome varchar(50) not null
);

create table usu_usuario (
	id bigint unsigned primary key auto_increment,
    usu_nome varchar(50) not null,
    usu_email varchar(50) not null,
    usu_telefone varchar(20),
    usu_senha varchar(100) not null,
    per_id bigint unsigned,
    unique key uni_usuario_email (usu_email),
    constraint per_usu_fk foreign key (per_id) references per_permissao (per_id)
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

create table tar_tarefa (
    tar_id bigint unsigned primary key auto_increment,
    usu_id bigint unsigned,
    mat_id bigint unsigned,
    tar_nome_arquivo varchar(50) not null,
    tar_nota int,
    constraint tar_usu_fk foreign key (usu_id) references usu_usuario (id),
    constraint tar_mat_fk foreign key (mat_id) references mat_materia (mat_id)
);

insert into per_permissao(per_nome) values ('ROLE_ALUNO');
insert into per_permissao(per_nome) values ('ROLE_PROFESSOR');
insert into per_permissao(per_nome) values ('ROLE_ADMIN');
insert into usu_usuario(usu_nome, usu_email, usu_telefone, usu_senha, per_id) values ('Ana', 'ana@email.com', '984556723', '$2a$10$i3.Z8Yv1Fwl0I5SNjdCGkOTRGQjGvHjh/gMZhdc3e7LIovAklqM6C', 1);
insert into usu_usuario(usu_nome, usu_email, usu_telefone, usu_senha, per_id) values ('Claudia', 'claudia@email.com', '457889652', '$2a$10$i3.Z8Yv1Fwl0I5SNjdCGkOTRGQjGvHjh/gMZhdc3e7LIovAklqM6C', 2);
insert into usu_usuario(usu_nome, usu_email, usu_telefone, usu_senha, per_id) values ('Benina', 'benina@email.com', '457123695', '$2a$10$i3.Z8Yv1Fwl0I5SNjdCGkOTRGQjGvHjh/gMZhdc3e7LIovAklqM6C', 3);
insert into mat_materia(mat_nome) values ('Algoritmos');
insert into uma_usuario_materia(usu_id, mat_id) values (1, 1);
insert into tar_tarefa(usu_id, mat_id, tar_nome_arquivo) values (1, 1, 'exAlgoritmos1.pdf');