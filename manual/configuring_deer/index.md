# Configuring DEER

DEER is configured using a simple RDF vocabulary.
Its namespace is `http://deer.aksw.org/vocabulary/#`.

There are just three predefined predicates that DEER inherits from FARADAY-CAGE:

* `<http://deer.aksw.org/vocabulary/#implementedIn>`
* `<http://deer.aksw.org/vocabulary/#hasInput>`
* `<http://deer.aksw.org/vocabulary/#hasOutput>`

Plugins are associated with unique resources. The default plugins that ship with deer-core live in
the deer namespace and have just their class name as local part, e.g.: 

* `<http://deer.aksw.org/vocabulary/#DefaultModelReader>`
* `<http://deer.aksw.org/vocabulary/#DefaultModelWriter>`
* `<http://deer.aksw.org/vocabulary/#FilterEnrichmentOperator>`

Custom plugins should be identified by resources outside of the default namespace to prevent
naming collisions.

Plugins define their own configuration vocabulary. In the final 1.0.0 release, it will be possible to
get an accurate description of the available parameters of a given plugin using the CLI.

The following example configuration demonstrates how the predefined vocabulary works:  

```turtle
@prefix : <http://deer.aksw.org/vocabulary/#> .
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
@prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> .
@prefix owl: <http://www.w3.org/2002/07/owl#> .

:node_reader1
              :implementedIn     :DefaultModelReader ;
              :fromUri           <http://dbpedia.org/resource/Paderborn> ;
              :useEndpoint       <http://dbpedia.org/sparql> ;
              :outputFile        "input_dbp.ttl" ;
              :outputFormat      "Turtle" ;
              :hasOutput         ( :node_conf ) .
:node_reader2
              :implementedIn     :DefaultModelReader ;
              :fromUri           <http://dbpedia.org/resource/Paderborn> ;
              :useEndpoint       <http://dbpedia.org/sparql> ;
              :outputFile        "input_lgd.ttl" ;
              :outputFormat      "Turtle" ;
              :hasOutput         ( :node_merge ) .
:node_writer
              :implementedIn     :DefaultModelWriter ;
              :outputFile        "output_demo.ttl" ;
              :outputFormat      "Turtle" ;
              :hasInput          ( :node_filter ) .
:node_merge
              :implementedIn     :GeoFusionEnrichmentOperator ;
              :hasInput          ( :node_conf :node_reader2 ) ;
              :hasOutput         ( :node_filter ) ;
              :fusionAction      "takeAll" ;
              :mergeOtherStatements
                                 "true" .
:node_filter
              :implementedIn     :FilterEnrichmentOperator ;
              :hasInput          ( :node_merge ) ;
              :hasOutput         ( :node_writer ) ;
              :selectors         (
                    [ :predicate geo:lat ]
                    [ :predicate geo:long ]
                    [ :predicate rdfs:label ]
                    [ :predicate owl:sameAs ]
              ) .
:node_conf
              :implementedIn     :AuthorityConformationEnrichmentOperator ;
              :hasInput          ( :node_reader1 ) ;
              :hasOutput         ( :node_merge ) ;
              :sourceSubjectAuthority 
                                 "http://dbpedia.org" ;
              :targetSubjectAuthority 
                                 "http://deer.org" .
```