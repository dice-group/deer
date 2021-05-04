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
package org.aksw.deer.io;

import org.aksw.deer.vocabulary.DEER;
import org.aksw.faraday_cage.engine.ValidatableParameterMap;
import org.apache.jena.rdf.model.*;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.List;

/**
 *
 *
 *
 */
@Extension
public class EmbeddedModelReader extends AbstractModelReader {

  private static final Logger logger = LoggerFactory.getLogger(EmbeddedModelReader.class);

  public static final Property EMBEDDED_MODEL = DEER.property("embeddedModel");
  public static final Property SERIALIZATION_FORMAT = DEER.property("serializationFormat");
  public static final Property IMPORT_PREFIXES = DEER.property("importPrefixes");

  @Override
  public String getDescription() {
    return "Use a subgraph of the configuration graph as input model";
  }

  @Override
  public ValidatableParameterMap createParameterMap() {
    return ValidatableParameterMap.builder()
      .declareProperty(EMBEDDED_MODEL)
      .declareProperty(SERIALIZATION_FORMAT)
//      .declareValidationShape(getValidationModelFor(EmbeddedModelReader.class))
      .build();
  }

  @Override
  protected List<Model> safeApply(List<Model> data) {
    final String embeddedModel = getParameterMap().get(EMBEDDED_MODEL).asLiteral().getString();
    final String serializationFormat  = getParameterMap().getOptional(SERIALIZATION_FORMAT).map(RDFNode::asLiteral).map(Literal::getString).orElse("TTL");
    final boolean importPrefixes  = getParameterMap().getOptional(SERIALIZATION_FORMAT).map(RDFNode::asLiteral).map(Literal::getBoolean).orElse(true);

    Model result = ModelFactory.createDefaultModel();
    //todo: allow importing prefixes from configuration file
    //result.setNsPrefixes(...);
    result.read(new StringReader(embeddedModel), null, serializationFormat);
    return List.of(result);
  }

}
