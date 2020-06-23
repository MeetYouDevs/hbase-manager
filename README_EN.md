HBase Manager
=============


**HBase-Manager** is a tool for managing [Apache HBase](http://hbase.apache.org). Development Based on [Spring Boot](https://spring.io/projects/spring-boot/) and [Apache FreeMarker](https://freemarker.apache.org/).

[**中文文档**](./README.md)

It supports the following :

 - Manage multiple clusters
 - Easy to manage HBase table (create、alter、delete、enable、disable), Timed task to compact Table
 - Easy to view data of table 
 - Table Snapshot Management (Timed task to create table snapshot, and manage it)
 - Easy to migration data of table (Timed task to migration data from one cluter to another)
 - Table relication (Based on the replication feature for HBase)
 - Table statistics (statistics max size of row, key of biggest row, url of table region server, for a cloumn family of table. Crawl data from the HBase Web).



## Compilation & Deployment Instructions

### 1 Compilation Environment Requirements 

- jdk >= 1.8
- Maven >= 3.3

### 2 Compilation Method 

Run the following command in the root directory of the source code:  

`mvn package`

After compiling, a distribution package named `hbase-manager-1.0-assembly.tar.gz` will be generated under `target` in the root directory.   
Unpacking the distribution package, the following subdirectories will be generated under the root directory: 

- bin：scripts for server start/shutdown  
- lib：jars for HBase-Manager and dependencies
- conf：configuration files
- logs：run log for server
- docs\db：required data (mysql) for server 

### 3 Deployment Environment Requirements  

- linux
- Java >= 1.8
- Mysql >= 5.6

### 4 Deployment Guide  
- Init mysql db: execute the db file `hbase-manager.sql` (in the docs\db under the root directory)

Under the "conf" directory of the unpacking distribution package , configure the related files:  

- application*.yml [Required]：configure server.port, server.address, and the datasource of mysql
- quartz.properties [Optional]：related configuration for quartz
- log4j.properties [Optional]：configure log level.

### 5 Start Method of Server

- under the root directory, run `bin/start-hbase-manager.sh`

### 6 Shutdown Method of Server

- under the root directory, run `bin/shutdown-hbase-manager.sh`



## Usage
According to the configuration file (application.yml), enter the console address in the browser, **default username/password: admin/admin**.

### 1 Cluster Management

![cluster](/docs/img/cluster.png)

### 2 Table Management

![table](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/table.png)

### 3 Data View

![data-view](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/data-view.png)

### 4 Table Snapshot

![snapshot](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/snapshot.png)

### 5 Table Migration

![migration](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/migration.png)
NOTE: before configure the migration task, you should add an agent machine under the module named `RunCluster`.

### 6 Table Replication

![replication](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/replication.png)

### 7 Table Statistics

![stat](https://github.com/MeetYouDevs/hbase-manager/raw/master/docs/img/stat.png)

### 8 System Management
System integration simple user, permission management (Based on Apache Shrio), permission control through the service URL.



## FAQ

### 1 How to use the different version of the cluster (HBase, Hadoop)
Current version of the project based on HBase (1.2.0-cdh5.11.1), Hadoop (2.6.0-cdh5.11.1). You can modify the file (`pom.xml`) yourself, and compile the version you need.
The change of the `common`, `hdfs` module API for Hadoop 2.X version are small, compared with the 3.X.
The version of client API for HBase 2.X changes greatly, compared with the 1.X .
If you use HBase 2.X, you must modify the java class (`com.meiyou.hbase.manager.utils.HBaseFacade`) to adapt the 2.X API.


## License
The project is licensed under the [Apache 2 license](http://www.apache.org/licenses/LICENSE-2.0).

