create table notification
(
	id bigint auto_increment,
	notifier bigint not null comment '发送人Id',
	receiver bigint not null comment '接收人id',
	outerId bigint not null,
	type int not null,
	gmt_create bigint default 0 not null,
	status int default 0 not null comment '阅读状态 0：未读 1：已读',
	constraint notification_pk
		primary key (id)
)
comment '通知表';