package org.aksw.deer.vocabulary;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class DEER {

  public static final String NS = "https://w3id.org/deer/";

  public static final String PREFIX = "deer";

  public static Property property(String localName) {
    return ResourceFactory.createProperty(NS + localName);
  }

  public static Resource resource(String localName) {
    return ResourceFactory.createResource(NS + localName);
  }
  public static Resource DeerExecutionNodeWrapper = resource("DeerExecutionNodeWrapper");
  public static Resource EnrichmentOperator = resource("EnrichmentOperator");
  public static Resource ModelReader = resource("ModelReader");
  public static Resource ModelWriter = resource("ModelWriter");

  public static String getURI() {
    return NS;
  }

}