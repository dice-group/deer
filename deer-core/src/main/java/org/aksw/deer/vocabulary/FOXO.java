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

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class FOXO {

  public static final String url = "http://ns.aksw.org/fox/ontology#";
  public static final Resource LOCATION = resource("LOCATION");
  public static final Resource ORGANIZATION = resource("ORGANIZATION");
  public static final Resource PERSON = resource("PERSON");
  public static final Property RELATED_TO = property("relatedTo");

  private static Property property(String name) {
    Property result = ResourceFactory.createProperty(url + name);
    return result;
  }

  private static Resource resource(String name) {
    Resource result = ResourceFactory.createResource(url + name);
    return result;
  }

  public static String getURI() {
    return url;
  }

}