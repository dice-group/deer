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
package org.aksw.deer.learning;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 *
 */
public class FitnessFunction {

  private final double[] weights;
  private final double beta;

  public FitnessFunction(int[] weights, double beta) {
    if (weights.length != 4) {
      throw new IllegalArgumentException("Weights array must have exactly length 4");
    }
    double sum = Arrays.stream(weights).sum();
    this.weights = Arrays.stream(weights).mapToDouble(w -> (double)w/sum).toArray();
    this.beta = beta;
  }

  public double getFitness(EvaluationResult evaluationResult) {
    double[] fMeasures = evaluationResult.getIndividualFMeasures(beta);
    return getFitness(fMeasures);
  }

  public double getFitness(double[] scores) {
    return IntStream.range(0, 4)
      .mapToDouble(i -> scores[i] * weights[i])
      .sum();
  }


}
