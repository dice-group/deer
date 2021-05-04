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

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 *
 */
class PopulationEvaluationResult {

  static class DoubleStatistics extends DoubleSummaryStatistics {

    private double sumOfSquare = 0.0d;
    private double sumOfSquareCompensation; // Low order bits of sum
    private double simpleSumOfSquare; // Used to compute right sum for non-finite inputs

    @Override
    public void accept(double value) {
      super.accept(value);
      double squareValue = value * value;
      simpleSumOfSquare += squareValue;
      sumOfSquareWithCompensation(squareValue);
    }

    public DoubleStatistics combine(DoubleStatistics other) {
      super.combine(other);
      simpleSumOfSquare += other.simpleSumOfSquare;
      sumOfSquareWithCompensation(other.sumOfSquare);
      sumOfSquareWithCompensation(other.sumOfSquareCompensation);
      return this;
    }

    private void sumOfSquareWithCompensation(double value) {
      double tmp = value - sumOfSquareCompensation;
      double velvel = sumOfSquare + tmp; // Little wolf of rounding error
      sumOfSquareCompensation = (velvel - sumOfSquare) - tmp;
      sumOfSquare = velvel;
    }

    public double getSumOfSquare() {
      double tmp = sumOfSquare + sumOfSquareCompensation;
      if (Double.isNaN(tmp) && Double.isInfinite(simpleSumOfSquare)) {
        return simpleSumOfSquare;
      }
      return tmp;
    }

    public final double getStandardDeviation() {
      return getCount() > 0 ? Math.sqrt((getSumOfSquare() / getCount()) - Math.pow(getAverage(), 2)) : 0.0d;
    }

  }

  private Genotype best;
  private double min;
  private double max;
  private double average;
  private double standardDeviation;

  PopulationEvaluationResult(List<Genotype> individuals) {
    best = individuals.stream()
      .max(Comparator.comparingDouble(Genotype::getBestFitness))
      .orElseThrow();
    DoubleStatistics statistics = individuals.stream()
      .mapToDouble(Genotype::getBestFitness)
      .collect(
        DoubleStatistics::new,
        DoubleStatistics::accept,
        DoubleStatistics::combine
      );
    min = statistics.getMin();
    max = statistics.getMax();
    average = statistics.getAverage();
    standardDeviation = statistics.getStandardDeviation();
  }

  public Genotype getBest() {
    return best;
  }

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }

  public double getAverage() {
    return average;
  }

  public double getStandardDeviation() {
    return standardDeviation;
  }
}
