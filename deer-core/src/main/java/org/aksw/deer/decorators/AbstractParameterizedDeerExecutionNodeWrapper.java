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

import org.aksw.deer.DeerExecutionNode;
import org.aksw.deer.DeerExecutionNodeWrapper;
import org.aksw.deer.vocabulary.DEER;
import org.aksw.faraday_cage.decorator.AbstractParameterizedExecutionNodeWrapper;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

/**
 *
 */
public abstract class AbstractParameterizedDeerExecutionNodeWrapper extends AbstractParameterizedExecutionNodeWrapper<DeerExecutionNode, Model> implements DeerExecutionNodeWrapper {

  @Override
  public Resource getType() {
    return DEER.resource(this.getClass().getSimpleName());
  }
}
