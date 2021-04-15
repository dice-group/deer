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

import org.aksw.deer.learning.FitnessFunction;
import org.aksw.deer.learning.RandomUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class GeneticProgrammingAlgorithm {

  private final Population startingPopulation;
  private final FitnessFunction fitnessFunction;
  private final TournamentSelector selector;
  private final List<Recombinator> recombinators;
  private final double offspringFraction;
  private final List<Mutator> mutators;
  private final double mutationProbability;
  private final double mutationRate;


  public GeneticProgrammingAlgorithm(Population startingPopulation, FitnessFunction fitnessFunction,
                                     TournamentSelector selector, List<Recombinator> recombinators,
                                     double offspringFraction, List<Mutator> mutators,
                                     double mutationProbability, double mutationRate) {
    this.fitnessFunction = fitnessFunction;
    this.mutationProbability = mutationProbability;
    this.mutationRate = mutationRate;
    this.mutators = mutators;
    this.recombinators = recombinators;
    this.offspringFraction = offspringFraction;
    this.startingPopulation = startingPopulation;
    this.selector = selector;
  }

  public List<PopulationEvaluationResult> run() {
    int generation = 0;
    int convergenceCounter = 0;
    int convergenceIgnore = 100;
    double localMP = mutationProbability;
    double localMR = mutationRate;
    final List<PopulationEvaluationResult> evolutionHistory = new ArrayList<>();
    evolutionHistory.add(startingPopulation.evaluate(fitnessFunction));
    Population currentPopulation = startingPopulation;
    while (!mustTerminate(generation, evolutionHistory.get(generation).getBest(), convergenceCounter)) {
      // find elite
      final Genotype bestInGeneration = evolutionHistory.get(generation).getBest();
      Population nextPopulation = new Population(1, () -> bestInGeneration);
      // generate offspring
      final Population selectionPopulation = currentPopulation;
      nextPopulation.importPopulation((int)((currentPopulation.size())*offspringFraction),
        () -> {
          Genotype parent1 = selector.select(selectionPopulation);
          Genotype parent2 = selector.select(selectionPopulation);
          Genotype[] childs = getRecombinator().recombinate(parent1, parent2);
          return Arrays.asList(childs);
        });
      // select survivors
      nextPopulation.fillPopulation(currentPopulation.size(),
        () -> {
        if (RandomUtil.get() < 0.5) {
          return selector.select(selectionPopulation).compactBestResult(false, 0);
        } else {
          return selector.select(selectionPopulation).getEvaluatedCopy();
        }
        });
      // mutation, preserve elite
      nextPopulation.evaluate(fitnessFunction);
      currentPopulation = nextPopulation
        .getMutatedPopulation(this::getMutator, localMP, localMR, g -> g == bestInGeneration);
      // evaluation & storage of results
      evolutionHistory.add(currentPopulation.evaluate(fitnessFunction));
      // repeat
      generation++;
      if (localMP != mutationProbability) {
        localMP = Math.max(mutationProbability, localMP/50*49);
      }
      if (localMR != mutationRate) {
        localMR = Math.max(mutationRate, localMR/50*49);
      }
      convergenceIgnore--;
      if (convergenceIgnore <= 0 && converged(evolutionHistory)) {
//        System.out.println(evolutionHistory.get(evolutionHistory.size()-1).getMax());
        convergenceCounter++;
        convergenceIgnore = 100;
        localMP = 1.0;
        localMR = 1.0;
      }
    }
    return evolutionHistory;
  }

  private boolean converged(List<PopulationEvaluationResult> history) {
    int lookAhead = 100;
    if (history.size() < lookAhead) {
      return false;
    }
    double f = history.get(history.size()-1).getMax();
//    List<Resource> smell = history.get(history.size()-1).getBest().getSmell(true);
    for (int i = history.size()-lookAhead; i < history.size(); i++) {
      if (history.get(i).getMax() != f) //|| history.get(i).getStandardDeviation() > 0.15
        return false;
    }
    return true;
  }

  private boolean mustTerminate(int generation, Genotype bestInGeneration, int convergenceCounter) {
    return generation >= 2000-1 || bestInGeneration.getBestFitness() == 1.0 || convergenceCounter == 5;
  }

  private Mutator getMutator() {
    return mutators.get(RandomUtil.get(mutators.size()));
  }

  private Recombinator getRecombinator() {
    return recombinators.get(RandomUtil.get(recombinators.size()));
  }

}
