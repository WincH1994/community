## 宙宙宙的游戏社区

## 部署
### 依赖
- GIT
- JDK
- Maven
- MySQL

##步骤
- yum update
- yum install git
- mkdir App
- cd App
- git clone https://github.com/WincH1994/community.git
- yum install maven
- mvn -v
- mvn compile package
- cp src/main/resources/application.yml src/main/resources/application-production.yml
- vim src/main/resources/application-production.yml
- mvn package
- java -jar -Dspring.profiles.active=production target/community-0.0.1-SNAPSHOT.jar

## 资料
[Spring文档](https://spring.io/guides)

[码匠社区](http://www.mawen.co)

[Bootstrap文档](https://v3.bootcss.com/)

[Spring datasource](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-configure-datasource)

[Elastic Search](https://elasticsearch.cn/)

[thymeleaf](https://www.thymeleaf.org/documentation.html)

[Spring MVC](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/web.html#mvc-config-interceptors)

[editor.md](http://editor.md.ipandao.com/)

[UFile](https://github.com/ucloud/ufile-sdk-java)

## 工具
[Git代码管理工具下载](https://git-scm.com/downloads)

[Postman](https://www.getpostman.com/downloads/)

[Flyway](https://flywaydb.org/getstarted/firststeps/maven)

[Lombok](https://projectlombok.org/)

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
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```