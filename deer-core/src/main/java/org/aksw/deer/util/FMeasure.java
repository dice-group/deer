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
package org.aksw.deer.util;

import org.apache.jena.rdf.model.Model;

/**
 * Helper class for FMeasure computation.
 * Deprecated, due to be replaced with LIMES implementation.
 */
@Deprecated
public class FMeasure {

  private double p;
  private double r;
  private double f;

  public FMeasure(Model current, Model target) {
    this(computePrecision(current, target), computeRecall(current, target));
  }

  /**
   */
  public FMeasure(double p, double r) {
    this.p = p;
    this.r = r;
    this.f = 2 * p * r / (p + r);
  }

  public double precision() {
    return p;
  }

  public double recall() {
    return r;
  }

  public double fMeasure() {
    return f;
  }

  private static double computePrecision(Model current, Model target) {
    return (double) current.intersection(target).size() / (double) current.size();
  }

  private static double computeRecall(Model current, Model target) {
    return (double) current.intersection(target).size() / (double) target.size();
  }

  @Override
  public String toString() {
    return "FMeasure [p=" + p + ", r=" + r + ", f=" + f + "]";
  }

}
