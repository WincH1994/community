create table question
(
	id int auto_increment,
	title varchar(50) not null,
	description text null,
	gmt_create bigint null,
	gmt_modify bigint null,
	creator int not null,
	comment_count int default 0 null,
	view_count int default 0 null,
	like_count int default 0 null,
	tag varchar(256) null,
	constraint question_pk
		primary key (id)
);

