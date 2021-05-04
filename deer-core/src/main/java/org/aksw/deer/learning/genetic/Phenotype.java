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
package org.aksw.deer.learning.genetic;

import org.aksw.deer.learning.EvaluationResult;
import org.aksw.faraday_cage.engine.CompiledExecutionGraph;
import org.aksw.faraday_cage.engine.ExecutionGraph;
import org.aksw.faraday_cage.util.ExecutionGraphSerializer;
import org.apache.jena.rdf.model.Model;

/**
 *
 */
class Phenotype extends ExecutionGraph<Model> {

  private EvaluationResult result = null;

  static Phenotype of(Genotype genotype) {
    return new Phenotype(genotype.compactBestResult(true, 0));
  }

  private Phenotype(Genotype g) {
    super(g.getSize()+1);
    for (int i = 0; i < getSize()-1; i++) {
      addRow(i, g.getRawNode(i), g.getRow(i));
    }
    addRow(getSize()-1,
      g.trainingData.getResultWriter(m -> {
        result = new EvaluationResult(m, g.trainingData.getEvaluationTarget());
      }),
      new int[]{1, 0, getSize()-2, 0});
  }

  public EvaluationResult getResult() {
    if (result == null) {
      CompiledExecutionGraph of = CompiledExecutionGraph.of(this);
      of.join();
    }
    return result;
  }

  public Model toModel() {
    return ExecutionGraphSerializer.serialize(this);
  }

}