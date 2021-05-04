# Quickstart

## Installation

DEER is split into two Maven submodules: **deer-core** and **deer-cli**.
While deer-core is intended to be used programmatically from other Java applications,
deer-cli provides a CLI to either run a single configuration or start the DEER server.

You can get the runnable deer-cli.jar from the [GitHub releases page](https://github.com/dice-group/deer/releases).\
Moreover, a Docker image of DEER is provided [on Dockerhub](https://hub.docker.com/r/dicegroup/deer) as `dicegroup/deer:latest` for fast access.

### DEER Docker image

If you just want to run DEER and already have Docker installed on your system, the fastest option is to just pull the image
and use DEER through Docker.

```bash
docker pull dicegroup/deer:latest
```

### Building DEER

In case you want to access the current development version, you can also build DEER locally using Maven.
In order to build DEER, clone the repository and run the following commands in the repositories root directory

```bash
mvn clean package shade:shade -Dmaven.test.skip=true
```

The runnable JAR can then be found in the folder `deer-cli/target/`

## Running DEER

### DEER CLI

The DEER CLI can be run locally through Java or in the Docker container:
```bash
java -jar deer-cli-${version}.jar
```
or

```bash
docker run -it --rm dicegroup/deer
```

In order to see the usage of the DEER CLI, use the `-h` or `--help` flag.

This yields the following description:
```bash
usage: deer [OPTION]... <config_file_or_uri>
 -E,--explain                        enable detailed explanation of graph
                                     validation
 -h,--help                           show help message
 -l,--list                           list available deer plugins
 -p,--port <port_number>             set port for server to listen on
 -s,--server                         launch server
 -v,--validation-graph <plugin_id>   if $plugin_id is provided, get SHACL
                                     validation graph for $plugin_id, else
                                     get the complete validation graph.
```

### Executing single configurations from the command line

In order to execute a DEER configuration, just supply its locator as the only argument to the DEER CLI.
A locator can be a path on your local file system or a URL, for example

```bash
java -jar deer-cli-2.3.1.jar demo.ttl
```

or

```bash
java -jar deer-cli-2.3.1.jar https://raw.githubusercontent.com/dice-group/deer/dev/examples/configurations/demo.ttl
```

#### Docker bindings

The following Docker bindings will be useful for running single configurations:

> [!TIP]
> Note that DEER looks for plugins in the `$(pwd)/plugins` directory

```bash
docker run -it --rm \
   -v $(pwd)/plugins:/plugins \
   -v $(pwd):/data \
   dicegroup/deer:latest \
   /data/my-configuration.ttl
```

### DEER RESTful Server

In order to run DEER as a RESTful server, supply the `-s` or `--server` flag.
You may also combine it with the `-p` or `--port` option to specify the port.
The default port is 8080.

#### Docker bindings

The following Docker bindings will be useful for running DEER RESTful server:

> [!TIP]
> Note that DEER RESTful server writes configurations and output to the `$(pwd)/.server-storage` directory.  
> You might want to bind this directory to your host in order to persist the data.

```bash
docker run -it --rm \
   -v $(pwd)/plugins:/plugins \
   -v $(pwd)/.server-storage:/.server-storage \
   -p 8080:8080 \
   dicegroup/deer:latest \
   -s
```

[comment]: <> (## DEER webUI &#40;coming soon&#41;)

## Writing Configuration Files

DEER is configured using the 
[**FARADAY-CAGE** Configuration Vocabulary](https://dice-group.github.io/faraday-cage/#/vocab).
The predefined plugins that ship with *deer-core* as well as their parameters live in the
`https://w3id.org/deer/` namespace with the canonical prefix `deer`.

Predefined plugins are denominated by their class name, e.g.

* `<https://w3id.org/deer/FileModelReader>` or `deer:FileModelReader`
* `<https://w3id.org/deer/FileModelWriter>` or `deer:FileModelWriter`
* `<https://w3id.org/deer/FilterEnrichmentOperator>` or `deer:FilterEnrichmentOperator`

The parameter vocabulary of our predefined plugins is described more precisely in the following
sections of this manual.

Custom plugins should be identified by resources outside of the default namespace to prevent
naming collisions.

The following example configuration demonstrates how the predefined vocabulary works:  

```turtle
@prefix : <urn:example:demo/> .
@prefix fcage: <https://w3id.org/fcage/> .
@prefix deer: <https://w3id.org/deer/> .
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
@prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> .
@prefix owl: <http://www.w3.org/2002/07/owl#> .
@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .

:node_reader1_dbp
  a deer:SparqlModelReader ;
  deer:useSparqlDescribeOf <http://dbpedia.org/resource/Paderborn> ;
  deer:fromEndpoint       <http://dbpedia.org/sparql> ;
.

:node_reader_lgd
  a deer:SparqlModelReader ;
  deer:useSparqlDescribeOf <http://linkedgeodata.org/triplify/node240114473> ;
  deer:fromEndpoint <http://linkedgeodata.org/sparql> ;
.

:node_conf
  a deer:AuthorityConformationEnrichmentOperator ;
  fcage:hasInput :node_reader_lgd ;
  deer:operation [
    deer:sourceAuthority <http://dbpedia.org> ;
    deer:targetAuthority <http://deer.org> ;
  ] ;
.

:node_geofusion
  a deer:GeoFusionEnrichmentOperator ;
  fcage:hasInput ( :node_conf :node_reader_dbp ) ;
  deer:fusionAction "takeAll" ;
  deer:mergeOtherStatements "true"^^xsd:boolean ;
.

:node_filter
  a deer:FilterEnrichmentOperator ;
  fcage:hasInput :node_geofusion ;
  deer:selector [ deer:predicate geo:lat ] ,
            [ deer:predicate geo:long ] ,
            [ deer:predicate rdfs:label ] ,
            [ deer:predicate owl:sameAs ] ;
.

:node_writer
  a deer:FileModelWriter ;
  fcage:hasInput :node_filter ;
  deer:outputFile "output_demo.ttl" ;
  deer:outputFormat "Turtle" ;
.
```


## Using Plugins

In order to use components from third party plugins in your configuration graphs, just include the plugin JAR files in the `plugins/` folder at your working directory. DEER will automatically look for and load available plugins.\
In the future we plan to implement a central repository for plugins alongside automatic fetching of plugins.

<small style="text-align: right; display: block"> Last updated: {docsify-updated} </small>