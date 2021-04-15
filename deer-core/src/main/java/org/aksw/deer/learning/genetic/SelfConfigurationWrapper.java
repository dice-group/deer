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

import org.aksw.deer.DeerExecutionNode;
import org.aksw.deer.ParameterizedDeerExecutionNode;
import org.aksw.deer.decorators.AbstractDeerExecutionNodeDecorator;
import org.aksw.deer.decorators.AbstractParameterizedDeerExecutionNodeDecorator;
import org.aksw.deer.learning.EvaluationResult;
import org.aksw.deer.learning.SelfConfigurable;
import org.aksw.faraday_cage.engine.ExecutionNode;
import org.aksw.faraday_cage.engine.Parameterized;
import org.aksw.faraday_cage.engine.ValidatableParameterMap;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
class SelfConfigurationWrapper {

  private static final Logger logger = LoggerFactory.getLogger(SelfConfigurationWrapper.class);

  private SelfConfigurationWrapper() {

  }

  static DeerExecutionNode wrap(DeerExecutionNode executionNode, Consumer<EvaluationResult> callback, Model target) {
    if (executionNode instanceof Parameterized) {
      return new SelfConfigurationDecorator((ParameterizedDeerExecutionNode) executionNode, callback, target);
    } else {
      return new LearningDecorator(executionNode, callback, target);
    }
  }

  private static class LearningDecorator extends AbstractDeerExecutionNodeDecorator {

    private final Consumer<EvaluationResult> callback;
    private final Model target;

    public LearningDecorator(ExecutionNode<Model> other, Consumer<EvaluationResult> callback, Model target) {
      super(other);
      this.callback = callback;
      this.target = target;
    }

    public List<Model> apply(List<Model> in) {
      List<Model> out = super.apply(in);
      callback.accept(new EvaluationResult(out.get(0), target));
      return out;
    }

  }

  private static class SelfConfigurationDecorator extends AbstractParameterizedDeerExecutionNodeDecorator {

    private final Consumer<EvaluationResult> callback;
    private final Model target;

    public SelfConfigurationDecorator(ParameterizedDeerExecutionNode other, Consumer<EvaluationResult> callback, Model target) {
      super(other);
      this.callback = callback;
      this.target = target;
    }

    public List<Model> apply(List<Model> in) {
      ValidatableParameterMap parameterMap =
        ((SelfConfigurable) getWrapped()).learnParameterMap(in, target, null);
      getWrapped().initParameters(parameterMap);
      List<Model> out = super.apply(in);
      callback.accept(new EvaluationResult(out.get(0), target));
      return out;
    }

  }

}