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
package org.aksw.deer.enrichments;

import com.google.common.collect.Lists;
import org.aksw.deer.vocabulary.DEER;
import org.aksw.faraday_cage.engine.ValidatableParameterMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.update.UpdateAction;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 *
 *
 */
@Extension
public class SparqlUpdateEnrichmentOperator extends AbstractParameterizedEnrichmentOperator {

  private static final Logger logger = LoggerFactory.getLogger(AuthorityConformationEnrichmentOperator.class);

  public static final Property UPDATE = DEER.property("sparqlUpdateQuery");

  @Override
  public String getDescription() {
    return "Apply a SPARQL UPDATE query to the input graph";
  }

  @Override
  public ValidatableParameterMap createParameterMap() {
    return ValidatableParameterMap.builder()
      .declareProperty(UPDATE)
      .declareValidationShape(getValidationModelFor(SparqlUpdateEnrichmentOperator.class))
      .build();
  }

  @Override
  protected List<Model> safeApply(List<Model> models) {
    Model model = ModelFactory.createDefaultModel().add(models.get(0));
    final String updateStatement = getParameterMap().get(UPDATE).asLiteral().getString();
    UpdateAction.parseExecute(updateStatement, model);
    return Lists.newArrayList(model);
  }

}