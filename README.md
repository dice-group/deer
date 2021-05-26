# DEER
[![Build Status](https://github.com/dice-group/deer/actions/workflows/run-tests.yml/badge.svg?branch=master&event=push)](https://github.com/dice-group/deer/actions/workflows/run-tests.yml)
[![DockerHub](https://badgen.net/badge/dockerhub/dicegroup%2Fdeer/blue?icon=docker)](https://hub.docker.com/r/dicegroup/deer)
[![GNU Affero General Public License v3.0](https://badgen.net/badge/license/GNU_Affero_General_Public_License_v3.0/orange)](./LICENSE)
![Java 11+](https://badgen.net/badge/java/11+/gray?icon=maven)

<div style="text-align: center;">

![LOGO](https://raw.githubusercontent.com/dice-group/deer/master/docs/_media/deer_logo.svg)
</div>

The RDF Dataset Enrichment Framework (DEER), is a modular, extensible software system for efficient
computation of arbitrary operations on RDF datasets.  
The atomic operations involved in this process, dubbed *enrichment operators*, 
are configured using RDF, making DEER a native semantic web citizen.  
Enrichment operators are mapped to nodes of a directed acyclic graphs to build complex enrichment
models, in which the connections between two nodes represent intermediary datasets.

## Running DEER

To bundle DEER as a single jar file, do

```bash
mvn clean package shade:shade -Dmaven.test.skip=true
```

Then execute it using

```bash
java -jar deer-cli/target/deer-cli-${current-version}.jar path_to_config.ttl
```

## Using Docker

The Docker image declares two volumes:
- /plugins - this is where plugins are dynamically loaded from
- /data - this is where configuration as well as input/output data will reside

For running DEER server in Docker, we expose port 8080.
The image accepts the same arguments as the deer-cli.jar, i.e. to run a configuration at `./my-configuration`:

```bash
docker run -it --rm \
   -v $(pwd)/plugins:/plugins \
   -v $(pwd):/data dicegroup/deer:latest \
   /data/my-configuration.ttl
```

To run DEER server:

```bash
docker run -it --rm \
   -v $(pwd)/plugins:/plugins \
   -p 8080:8080 \
   -s
```

## Maven

```xml
<dependencies>
  <dependency>
    <groupId>org.aksw.deer</groupId>
    <artifactId>deer-core</artifactId>
    <version>2.3.1</version>
  </dependency>
</dependencies>
```
```xml
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

## Developers

### Release new version

```bash
./release ${new-version} ${new-snapshot-version}
```