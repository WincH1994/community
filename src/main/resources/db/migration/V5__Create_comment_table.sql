create table comment
(
	id int auto_increment,
	parent_id bigint not null comment '父类ID',
	type int not null comment '父类类型',
	commentator int not null,
	gmt_create bigint null comment '创建时间',
	gmt_modify bigint null comment '修改时间',
	like_count bigint default 0 null comment '点赞数',
	constraint comment_pk
		primary key (id)
);

