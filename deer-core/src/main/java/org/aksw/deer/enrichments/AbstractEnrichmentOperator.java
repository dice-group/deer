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

import org.aksw.deer.vocabulary.DEER;
import org.aksw.faraday_cage.engine.AbstractExecutionNode;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

public abstract class AbstractEnrichmentOperator extends AbstractExecutionNode<Model> implements EnrichmentOperator {

  protected String description = "Description coming soon";
  protected String documentationURL = "https://dice-group.github.io/deer/configuring_deer/enrichment_operators.html";

  public String getDescription() {
    return description;
  }

  @Override
  public String getDocumentationURL() {
    return documentationURL;
  }

  @Override
  public DegreeBounds getDegreeBounds() {
    return new DegreeBounds(1,1,1,1);
  }

  public final Model deepCopy(Model model) {
    return ModelFactory.createDefaultModel().add(model);
  }

  @Override
  public Resource getType() {
    return DEER.resource(this.getClass().getSimpleName());
  }

}
