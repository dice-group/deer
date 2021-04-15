# DEER
[![Build Status](https://github.com/dice-group/deer/actions/workflows/run-tests.yml/badge.svg?branch=master&event=push)](https://github.com/dice-group/deer/actions/workflows/run-tests.yml)
[![GNU Affero General Public License v3.0](https://img.shields.io/badge/license-GNU_Affero_General_Public_License_v3.0-blue.svg)](./LICENSE)
![Java 1.9+](https://img.shields.io/badge/java-11+-lightgray.svg)

The RDF Dataset Enrichment Framework (DEER), is a modular, extensible software system for efficient
computation of arbitrary operations on RDF datasets.  
The atomic operations involved in this process, dubbed *enrichment operators*, 
are configured using RDF, making DEER a native semantic web citizen.  
Enrichment operators are mapped to nodes of a directed acyclic graphs to build complex enrichment
models, in which the connections between two nodes represent intermediary datasets.

## Running DEER

To bundle DEER as a single jar file, do

```
mvn clean package shade:shade -Dmaven.test.skip=true
```

Then execute it using

```
java -jar deer-cli/target/deer-cli-2.0.1.jar path_to_config.ttl
```

## Maven

```
<dependencies>
  <dependency>
    <groupId>org.aksw.deer</groupId>
    <artifactId>deer-core</artifactId>
    <version>2.0.1</version>
  </dependency>
</dependencies>

<repositories>
 <repository>
      <id>maven.aksw.internal</id>
      <name>University Leipzig, AKSW Maven2 Internal Repository</name>
      <url>http://maven.aksw.org/repository/internal/</url>
    </repository>

    <repository>
      <id>maven.aksw.snapshots</id>
      <name>University Leipzig, AKSW Maven2 Snapshot Repository</name>
      <url>http://maven.aksw.org/repository/snapshots/</url>
    </repository>
</repositories>
```


## Documentation

For more detailed information about how to run or extend DEER, please read the
[manual](https://dice-group.github.io/deer/) and consult the
[Javadoc](https://dice-group.github.io/deer/javadoc/)
