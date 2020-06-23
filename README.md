HBase Manager
=============


**HBase-Manager**是一个管理[Apache HBase](http://hbase.apache.org)的工具。基于[Spring Boot](https://spring.io/projects/spring-boot/)和[Apache FreeMarker](https://freemarker.apache.org/)开发。 

[**English Document**](./README_EN.md)

支持以下功能 :

 - 管理多个HBase集群
 - 表的管理（create、alter、delete、enable、disable），定时进行compact
 - 数据预览
 - 数据快照（定时生成快照、管理表快照）
 - 数据迁移（多个集群之间进行表的迁移备份）
 - 数据同步（多个集群之间表的实时同步。基于HBase Replication特性）
 - 数据统计（统计表各列族最大行的字节数、最大行的rowkey、表分区url。爬取HBase Web页面获取）



## 编译&部署指南

### 1 编译环境依赖 

- jdk >= 1.8
- Maven >= 3.3

### 2 编译方法

在源码根目录下，执行:

`mvn package`

完成编译后，在源码根目录下的target目录中会生成发布包`hbase-manager-1.0-assembly.tar.gz`。该发布包解压后的主要目录结构如下：  

- bin：服务启动、停止脚本
- lib：HBase-Manager jar包及所依赖第三方jar包
- conf：HBase-Manager配置文件
- logs：服务运行日志
- docs\db：服务所需的mysql表结构、数据

### 3 部署环境依赖  

- linux
- Java >= 1.8
- Mysql >= 5.6

### 4 部署方法
- 初始化数据库：mysql下，执行docs\db目录下的hbase-manager.sql

在HBase-Manager发布包根目录下的conf目录中，分别配置如下文件：
- application*.yml[必填]：配置环境所需要的服务端口、mysql数据源
- quartz.properties[可选]：quartz相关配置
- log4j.properties[可选]：配置日志级别

### 5 服务启动

- 根目录下，执行`bin/start-hbase-manager.sh`

### 6 服务停止

- 根目录下，执行`bin/shutdown-hbase-manager.sh`



## Usage
根据application.yml配置的server.server和server.port，在浏览器输入控制台地址，账号/密码：admin/admin

### 1 集群管理

![cluster](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/cluster.png)

### 2 表管理

![table](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/table.png)


### 3 数据预览

![data-view](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/data-view.png)

### 4 数据快照

![snapshot](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/snapshot.png)

### 5 数据迁移

![migration](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/migration.png)
备注：配置迁移任务前，需在RunCluster模块中添加任务运行的代理机。

### 6 数据同步

![replication](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/replication.png)

### 7 数据统计

![stat](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/stat.png)

### 8 系统管理
系统集成简单的用户、权限管理（基于shrio），通过服务URL进行权限控制。



## FAQ

### 1 集群版本问题
当前工程是基于HBase（1.2.0-cdh5.11.1）、Hadoop（2.6.0-cdh5.11.1）。可自行修改pom文件，编译自己所需的版本。
Hadoop 2.X版本的common、hdfs模块API相对3.X变化较小。
HBase 2.X版本客户端API相对1.X变化较大。如使用HBase2.X，需修改源码com.meiyou.hbase.manager.utils.HBaseFacade类，适配2.X版本。


## License
The project is licensed under the [Apache 2 license](http://www.apache.org/licenses/LICENSE-2.0).

