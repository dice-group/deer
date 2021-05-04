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

import org.aksw.deer.learning.RandomUtil;

/**
 *
 */
public class DefaultRecombinator implements Recombinator {
  @Override
  public Genotype[] recombinate(Genotype parentA, Genotype parentB) {
    Genotype[] childs = new Genotype[2];
    childs[0] = new Genotype(parentA);
    childs[1] = new Genotype(parentB);
    int crossoverPoint = RandomUtil.get(parentA.getNumberOfInputs()+1, parentA.getSize()-1);
    for (int i = crossoverPoint; i < parentA.getSize(); i++) {
      childs[0].addRow(i, parentB.getRawNode(i), parentB.getRow(i));
      childs[1].addRow(i, parentA.getRawNode(i), parentA.getRow(i));
    }
    return childs;
  }
}
