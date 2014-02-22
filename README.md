# HIT: A Hibernate quickstart

HIT - Hibernate Induction Trigger - is a library intended to provide all the configuration and dependencies necessary to begin implementing a stand-alone database-backed Spring/Hibernate application, enabling logging, JMX control, transactional boundaries and second level caching. The [example code](https://github.com/mrstampy/hitman/tree/master/hitman/example/com/github/mrstampy/hit/example) demonstrates the use of the library; a prototype Spring/Hibernate application can be developed in minutes. A Java 6 runtime (minimum) is required.

The steps involved to create a HIT-based application are as follows:

0. Choose your database vendor, database driver, database name, user etc. and create a blank database

1. Obtain the HIT library and its dependencies via Ivy, Maven or Gradle (TBA, the artifacts will be available shortly)
  * Ivy - &lt;dependency org="com.github.mrstampy" name="hit" rev="1.0"/&gt;
  * Maven:<br>
           &lt;dependency&gt;<br>
               &nbsp;&nbsp;&lt;groupId&gt;com.github.mrstampy&lt;/groupId&gt;<br>
               &nbsp;&nbsp;&lt;artifactId&gt;hit&lt;/artifactId&gt;<br>
               &nbsp;&nbsp;&lt;version&gt;1.0&lt;/version&gt;<br>
           &lt;/dependency&gt;

2. Create a [Hibernate entity](http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch05.html)
  * [HIT's entity package](https://github.com/mrstampy/hitman/tree/master/hitman/src/com/github/mrstampy/hit/entity)
  * [example entity](https://github.com/mrstampy/hitman/blob/master/hitman/example/com/github/mrstampy/hit/example/Hit.java)
  
3. Create a subclass of [AbstractDao](https://github.com/mrstampy/hitman/blob/master/hitman/src/com/github/mrstampy/hit/dao/AbstractDao.java) for the entity
  * [example DAO](https://github.com/mrstampy/hitman/blob/master/hitman/example/com/github/mrstampy/hit/example/HitDao.java)
  
4. Create a subclass of [AbstractSpringInitializer](https://github.com/mrstampy/hitman/blob/master/hitman/src/com/github/mrstampy/hit/spring/config/AbstractSpringInitializer.java) and any Spring configuration classes necessary
  * [example initializer](https://github.com/mrstampy/hitman/blob/master/hitman/example/com/github/mrstampy/hit/example/HitInitializer.java)
  * [example configuration](https://github.com/mrstampy/hitman/blob/master/hitman/example/com/github/mrstampy/hit/example/HitConfiguration.java)
  
5. Edit the configuration files and run the application.

# Configuration

Four configuration files are necessary to use HIT and must be available on the classpath:

https://github.com/mrstampy/hitman/tree/master/hitman/config
https://github.com/mrstampy/hitman/tree/master/hitman/example.config

1. hit.properties - defines [hit-specific properties](https://github.com/mrstampy/hitman/blob/master/hitman/src/com/github/mrstampy/hit/spring/config/PropertiesConfiguration.java) and can be appended to to add application specific properties.

2. [hibernate.properties](http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch03.html) - defines Hibernate-specific properties
  
3. [ehcache.xml](http://ehcache.org/documentation/configuration/index ) - configuration for EhCache second level caching
  
4. [logback.xml](http://logback.qos.ch/manual/configuration.html) - configuration for logging

# DataSource

Included in the distribution are four implementations of a DataSource JDBC connection pool:

* [BoneCP](http://jolbox.com/)
* [C3P0](http://www.mchange.com/projects/c3p0/)
* [Tomcat JDBC*](https://people.apache.org/~fhanik/jdbc-pool/jdbc-pool.html)
* [Vibur DBCP](https://code.google.com/p/vibur-dbcp/)

To switch between the four change the value of the property 'data.source.creator.type' in hit.properties. Once a connection pool has been chosen for your application the other CP jar dependencies can be excluded from the application build.
 
*Tomcat JDBC uses the JULI logging framework.  There is no official juli-over-slf4j jar file, however adding the classes from https://github.com/pellcorp/juli-over-slf4j to your application will enable logging for this connection pool.

## DataSource Configuration

For simple, low to medium volume data access typically 6 properties are of importance when configuring a datasource:

1. The JDBC driver class
2. The JDBC URL to the database
3. The username for the database
4. The password for the database
5. The maximum number of connections the datasource can pool
6. The minimum number of connections in the pool when idle

As each datasource implementation varies in its configuration API, the value of these properties are set via the values in 'hibernate.properties' (the first four) and 'hit.properties' (the last two). Should greater configuration control be required a datasource-specific properties file can be made available on the classpath and specified by the 'datasource.property.file' property in 'hit.properties', with the exception of the first four properties which are always set from 'hibernate.properties' values.
