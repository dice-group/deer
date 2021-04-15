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
import org.aksw.deer.learning.FitnessFunction;
import org.aksw.deer.learning.RandomUtil;
import org.aksw.faraday_cage.engine.ThreadlocalInheritingCompletableFuture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 */
public class Population {

  private List<Genotype> backingList;
  private PopulationEvaluationResult evaluationResult = null;

  Population(int size, Supplier<Genotype> genotypeSupplier) {
    this(size);
    fillPopulation(size, genotypeSupplier);
  }

  Population(int size) {
    backingList = new ArrayList<>(size);
  }

  Population(Population population) {
    this.backingList = new ArrayList<>(population.backingList);
  }

  public PopulationEvaluationResult evaluate(FitnessFunction f) {
    if (Objects.isNull(evaluationResult)) {
      CompletableFuture<EvaluationResult> joiner = ThreadlocalInheritingCompletableFuture.completedFuture(null);
      backingList.stream()
        .map(g -> g.getBestEvaluationResult(f))
        .reduce(joiner, (g, h) -> g.thenCombine(h, (x, y) -> null))
        .join();
      evaluationResult = new PopulationEvaluationResult(this.backingList);
    }
    return evaluationResult;
  }

  public void importPopulation(int limit, Supplier<Collection<Genotype>> genotypeSupplier) {
    while (backingList.size() < limit) {
      backingList.addAll(genotypeSupplier.get());
    }
  }

  public void fillPopulation(int limit, Supplier<Genotype> genotypeSupplier) {
    while (backingList.size() < limit) {
      backingList.add(genotypeSupplier.get());
    }
    backingList = new ArrayList<>(backingList);
  }

  public Population getMutatedPopulation(Supplier<Mutator> mutator, double mutationProbability, double mutationRate, Predicate<Genotype> exclude) {
    Population mutated = new Population(backingList.size());
    backingList.forEach(g -> mutated.backingList.add(
      (RandomUtil.get() < mutationProbability && !exclude.test(g)) ? mutator.get().mutate(g, mutationRate) : g
      ));
    return mutated;
  }

  public Genotype getRandomGenotype() {
    return backingList.get(RandomUtil.get(backingList.size()));
  }

  public int size() {
    return backingList.size();
  }

}