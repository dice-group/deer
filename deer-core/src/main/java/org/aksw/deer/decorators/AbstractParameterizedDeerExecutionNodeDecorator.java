package org.aksw.deer.decorators;

import org.aksw.deer.DeerExecutionNode;
import org.aksw.deer.ParameterizedDeerExecutionNode;
import org.aksw.faraday_cage.decorator.AbstractParameterizedExecutionNodeDecorator;
import org.apache.jena.rdf.model.Model;

/**
 *
 */
public abstract class AbstractParameterizedDeerExecutionNodeDecorator extends AbstractParameterizedExecutionNodeDecorator<ParameterizedDeerExecutionNode, Model> implements DeerExecutionNode {

  public AbstractParameterizedDeerExecutionNodeDecorator(ParameterizedDeerExecutionNode other) {
    super(other);
  }
  
  protected String description = "Description coming soon";
  protected String documentationURL = "https://dice-group.github.io/deer/configuring_deer/readers.html";

  public String getDescription() {
    return description;
  }

  @Override
  public String getDocumentationURL() {
    return documentationURL;
  }

}