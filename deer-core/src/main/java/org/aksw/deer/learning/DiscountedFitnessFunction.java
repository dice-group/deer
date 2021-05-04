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

/**
 *
 */
public class DiscountedFitnessFunction extends FitnessFunction {

  private final double omega;

  public DiscountedFitnessFunction(int[] weights, double beta, double omega) {
    super(weights, beta);
    this.omega = omega;
  }

  public double getFitness(EvaluationResult evaluationResult, double complexity) {
    return getFitness(evaluationResult) - omega * complexity;
  }

}
