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

import org.aksw.deer.enrichments.EnrichmentOperator;
import org.aksw.deer.learning.Learnable;
import org.aksw.deer.learning.RandomUtil;

/**
 *
 */
public class RandomGenotype extends Genotype {

  public RandomGenotype(TrainingData trainingData) {
    super(trainingData);
    int currentRow = getNumberOfInputs();
    while (currentRow < getSize()) {
      addRandomRow(this, currentRow++);
    }
  }

  static void addRandomRow(Genotype g, int i) {
    EnrichmentOperator op = RandomOperatorFactory.getForMaxArity(RandomUtil.get(1,Math.min(i, RandomOperatorFactory.getMaxArity())+1));
    int arity = ((Learnable) op).getLearnableDegreeBounds().minIn();
    op.initDegrees(arity, 1);
    int[] row = new int[2+arity*2];
    row[0] = arity;
    row[1] = 1;
    for (int j = 0; j < arity; j++) {
      row[2+j*2] = RandomUtil.get(i);
      row[2+j*2+1] = 0;
    }
    g.addRow(i, op, row);
  }

}
