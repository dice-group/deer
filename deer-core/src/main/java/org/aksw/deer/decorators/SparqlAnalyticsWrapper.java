/*
 * DEER Core Library - DEER - RDF Dataset Enrichment Framework
 * Copyright © 2013 Data Science Group (DICE) (kevin.dressler@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.deer.decorators;

import org.aksw.deer.DeerAnalyticsStore;
import org.aksw.deer.DeerExecutionNode;
import org.aksw.deer.ParameterizedDeerExecutionNode;
import org.aksw.deer.vocabulary.DEER;
import org.aksw.faraday_cage.engine.ExecutionNode;
import org.aksw.faraday_cage.engine.FaradayCageContext;
import org.aksw.faraday_cage.engine.Parameterized;
import org.aksw.faraday_cage.engine.ValidatableParameterMap;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.json.JSONObject;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
@Extension
public class SparqlAnalyticsWrapper extends AbstractParameterizedDeerExecutionNodeWrapper {

  private static final Logger logger = LoggerFactory.getLogger(SparqlAnalyticsWrapper.class);

  public static final Property SPARQL_SELECT_QUERY = DEER.property("sparqlSelectQuery");

  public static final Property JSON_OUTPUT = DEER.property("jsonOutput");

  @Override
  public ValidatableParameterMap createParameterMap() {
    return ValidatableParameterMap.builder()
      .declareProperty(SPARQL_SELECT_QUERY)
      .declareProperty(JSON_OUTPUT)
      .build();
  }

  private void applyTriggered(Resource id, List<Model> in, List<Model> out) {
    Dataset dataset = DatasetFactory.createGeneral();
    for (int i = 0; i < in.size(); i++) {
      dataset.addNamedModel(DEER.resource("inputGraph" + i).getURI(), in.get(i));
    }
    for (int i = 0; i < out.size(); i++) {
      dataset.addNamedModel(DEER.resource("outputGraph" + i).getURI(), out.get(i));
    }
    final String query = getParameterMap().get(SPARQL_SELECT_QUERY).asLiteral().getString();
    final String[] jsonOutput = { getParameterMap().get(JSON_OUTPUT).asLiteral().getString() };
    QueryExecution queryExecution = QueryExecutionFactory.create(query, dataset);
    ResultSet resultSet = queryExecution.execSelect();
    List<String> resultVars = resultSet.getResultVars();
    resultSet.forEachRemaining(qs ->
      resultVars.stream()
        .filter(qs::contains)
        .forEach(varName ->
          jsonOutput[0] = jsonOutput[0].replace("?" + varName, "\"" + qs.get(varName).toString() + "\"")
        )
    );
    DeerAnalyticsStore.write(FaradayCageContext.getRunId(), id, new JSONObject(jsonOutput[0]));
    logger.info("AnalyticsWrapper {} keeping notes", getId());
  }

  @Override
  public DeerExecutionNode wrap(DeerExecutionNode executionNode) {
    if (executionNode instanceof Parameterized) {
      return new ParameterizedSparqlAnalyticsDecorator((ParameterizedDeerExecutionNode) executionNode);
    } else {
      return new SparqlAnalyticsDecorator(executionNode);
    }
  }

  @Override
  public String getDocumentationURL() {
    return "";
  }

  @Override
  public String getDescription() {
    return "";
  }

  private class SparqlAnalyticsDecorator extends AbstractDeerExecutionNodeDecorator {

    public SparqlAnalyticsDecorator(ExecutionNode<Model> other) {
      super(other);
    }

    public List<Model> apply(List<Model> in) {
      List<Model> out = super.apply(in);
      applyTriggered(getWrapped().getId(), in, out);
      return out;
    }

  }

  private class ParameterizedSparqlAnalyticsDecorator extends AbstractParameterizedDeerExecutionNodeDecorator {

    public ParameterizedSparqlAnalyticsDecorator(ParameterizedDeerExecutionNode other) {
      super(other);
    }

    public List<Model> apply(List<Model> in) {
      List<Model> out = super.apply(in);
      applyTriggered(getWrapped().getId(), in, out);
      return out;
    }

  }

}