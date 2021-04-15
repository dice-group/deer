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
package org.aksw.deer.vocabulary;

import org.aksw.faraday_cage.engine.ExecutionNode;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.uuid.UUID_V1;

/**
 *
 */
public class DEERA {

  public static final String NS = "https://w3id.org/deer/config-autogen/";

  public static final String PREFIX = "deera";


  public static Property property(String localName) {
    return ResourceFactory.createProperty(NS + localName);
  }

  public static Resource resource(String localName) {
    return ResourceFactory.createResource(NS + localName);
  }

  public static Resource forExecutionNode(ExecutionNode<Model> executionNode) {
    return ResourceFactory.createResource(NS + executionNode.getClass().getSimpleName() + "-" + UUID_V1.generate().asString());
  }

  public static String getURI() {
    return NS;
  }

}
