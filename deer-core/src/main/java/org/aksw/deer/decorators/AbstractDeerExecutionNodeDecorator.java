package org.aksw.deer.decorators;

import org.aksw.deer.DeerExecutionNode;
import org.aksw.faraday_cage.decorator.AbstractExecutionNodeDecorator;
import org.aksw.faraday_cage.engine.ExecutionNode;
import org.apache.jena.rdf.model.Model;

/**
 *
 */
public abstract class AbstractDeerExecutionNodeDecorator extends AbstractExecutionNodeDecorator<Model> implements DeerExecutionNode {

  public AbstractDeerExecutionNodeDecorator(ExecutionNode<Model> other) {
    super(other);
  }

  public String getDocumentationURL() {
    return ((DeerExecutionNode)getWrapped()).getDocumentationURL();
  }

  public String getDescription() {
    return ((DeerExecutionNode)getWrapped()).getDescription();
  }

}
