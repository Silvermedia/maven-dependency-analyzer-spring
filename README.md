This is fork of fork of https://github.com/toby1984/maven-dependency-analyzer-spring from which comes majority of code.

Introduction
------------

This project enhances the Maven Dependency Plugin 

http://maven.apache.org/plugins/maven-dependency-plugin

by adding support for extracing dependency information
from a project's Spring XML files.

When enabled, a project's resources and test resources (usually located
in src/main/resources and src/test/resources) will be scanned for Spring XML files and all
bean definitions will be considered as project dependencies as well.

Requirements
------------

Java JDK 1.5+ (both for compiling AND RUNNING the plugin)
Maven 2.2.1+ (older versions will probably work as well)
Maven Dependencies Plugin 2.2+

Compiling and installing
------------------------

```
mvn install
```

Usage
-----

To enable scanning Spring XMLs for dependencies you need to add 
this project's artifact as a dependency of the maven-dependency-plugin 
*AND* set the plugin'S new 'analyzer' property to value 'spring'. 

Declare the maven-dependency-plugin in your pom.xml using the following
XML snippet:

```xml
<build>
  <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <version>...</version>
        <dependencies>
          <dependency>
            <groupId>de.codesourcery.maven.shared</groupId>
            <artifactId>maven-dependency-analyzer-spring</artifactId>
            <version>...</version>
          </dependency>
        </dependencies>
        <configuration>
          <analyzer>spring</analyzer>
        </configuration>
      </plugin>
  </plugins>
</build>
```

When running 'mvn dependency:analyze' on a project
with Spring XMLs , you should see the following output:

[INFO] Including dependencies from Spring XMLs in analysis
