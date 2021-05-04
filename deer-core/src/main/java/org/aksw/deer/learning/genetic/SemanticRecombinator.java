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
import org.aksw.deer.learning.RandomUtil;

/**
 *
 */
public class SemanticRecombinator implements Recombinator {
  @Override
  public Genotype[] recombinate(Genotype parentA, Genotype parentB) {
    parentA = parentA.compactBestResult(false, 0);
    parentB = parentB.compactBestResult(false, 0);
    int v = 0;
    if (RandomUtil.get() < 0.25) {
      v = 1;
    }
    if (parentB.bestResultRow + 1 + parentA.bestResultRow + 1 - parentA.getNumberOfInputs() + v <= Genotype.SIZE) {
      parentB = parentB.compactBestResult(false, parentA.bestResultRow + 1 - parentA.getNumberOfInputs());
      for (int i = parentA.getNumberOfInputs(); i <= parentB.bestResultRow; i++) {
        if (i <= parentA.bestResultRow) {
          parentB.addRow(i, RandomOperatorFactory.reproduce((EnrichmentOperator) parentA.getRawNode(i)), parentA.getRow(i));
        } else {
          parentA.addRow(i, RandomOperatorFactory.reproduce((EnrichmentOperator) parentB.getRawNode(i)), parentB.getRow(i));
        }
      }
      if (v == 1) {
      parentA.addRow(parentB.bestResultRow + 1, RandomOperatorFactory.getForArity(2), new int[]{2, 1, parentA.bestResultRow, 0, parentB.bestResultRow, 0});
      parentB.addRow(parentB.bestResultRow + 1, RandomOperatorFactory.getForArity(2), new int[]{2, 1, parentA.bestResultRow, 0, parentB.bestResultRow, 0});
      }
    }
    return new Genotype[]{parentA, parentB};
  }
}
