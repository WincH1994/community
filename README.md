## 宙宙宙的游戏社区

## 资料

[Spring文档](https://spring.io/guides)

[码匠社区](http://www.mawen.co)

[Bootstrap文档](https://v3.bootcss.com/)

[Spring datasource](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-configure-datasource)

## 工具
[Git代码管理工具下载](https://git-scm.com/downloads)

## Github OAuth Login
[Github OAuth 文档](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)

## 脚本
```sql
create table if not exists user
(
	id int auto_increment
		primary key,
	name varchar(50),
	account_id varchar(100),
	token char(36),
	gmt_create bigint,
	gmt_modify bigint
);
```