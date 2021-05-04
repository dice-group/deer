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

import org.aksw.deer.ParameterizedDeerExecutionNode;
import org.aksw.deer.decorators.AbstractParameterizedDeerExecutionNodeDecorator;
import org.aksw.deer.io.FileModelReader;
import org.aksw.deer.io.FileModelWriter;
import org.aksw.deer.learning.FitnessFunction;
import org.aksw.deer.vocabulary.DEERA;
import org.aksw.faraday_cage.decorator.AbstractParameterizedExecutionNodeDecorator;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public class TrainingData {

  private static class NoOpModelReaderDecorator extends AbstractParameterizedDeerExecutionNodeDecorator {


    private List<Model> staticReturn;
    public NoOpModelReaderDecorator(ParameterizedDeerExecutionNode other, List<Model> staticReturn) {
      super(other);
      this.staticReturn = staticReturn;
    }

    @Override
    public List<Model> apply(List<Model> data) {
      return staticReturn;
    }
    
    protected String description = "Description coming soon";
    protected String documentationURL = "https://dice-group.github.io/deer/configuring_deer/readers.html";

    public String getDescription() {
      return description;
    }

    @Override
    public String getDocumentationURL() {
      return documentationURL;
    }

  }
  private static class NoOpModelWriterDecorator extends AbstractParameterizedDeerExecutionNodeDecorator {


    private Consumer<Model> callback = m -> {};
    public NoOpModelWriterDecorator(ParameterizedDeerExecutionNode other) {
      super(other);
    }



    @Override
    public List<Model> apply(List<Model> data) {
      callback.accept(data.get(0));
      return List.of();
    }

    public void setCallback(Consumer<Model> callback) {
      this.callback = callback;
    }
    
    protected String description = "Description coming soon";
    protected String documentationURL = "https://dice-group.github.io/deer/configuring_deer/readers.html";

    public String getDescription() {
      return description;
    }

    @Override
    public String getDocumentationURL() {
      return documentationURL;
    }


  }
  private FitnessFunction fitnessFunciton;
  private List<String> trainingSourcePaths;
  private List<String> evaluationSourcePaths;
  private String trainingTargetPath;
  private String evaluationTargetPath;
  private String resultTargetPath;
  private List<Model> trainingSources;
  private List<Model> evaluationSources;
  private Model trainingTarget;
  private Model evaluationTarget;

  private List<ParameterizedDeerExecutionNode> trainingReaders;
  private List<ParameterizedDeerExecutionNode> evaluationReaders;
  private ParameterizedDeerExecutionNode resultWriter;

  public TrainingData(FitnessFunction fitnessFunciton, List<String> trainingSourcePaths, List<String> evaluationSourcePaths, String trainingTargetPath, String evaluationTargetPath, String resultTargetPath) {
    this.fitnessFunciton = fitnessFunciton;
    this.trainingSourcePaths = trainingSourcePaths;
    this.evaluationSourcePaths = evaluationSourcePaths;
    this.trainingTargetPath = trainingTargetPath;
    this.evaluationTargetPath = evaluationTargetPath;
    this.resultTargetPath = resultTargetPath;
    this.trainingSources = new ArrayList<>(trainingSourcePaths.size());
    this.trainingReaders = new ArrayList<>(trainingSourcePaths.size());
    this.evaluationReaders = new ArrayList<>(evaluationSourcePaths.size());
    for (String path : trainingSourcePaths) {
      Model model = ModelFactory.createDefaultModel().read(path);
      trainingSources.add(model);
      trainingReaders.add(getReaderFor(model, path));
    }
    this.evaluationSources = new ArrayList<>(evaluationSourcePaths.size());
    for (String path : evaluationSourcePaths) {
      Model model = ModelFactory.createDefaultModel().read(path);
      evaluationSources.add(model);
      evaluationReaders.add(getReaderFor(model, path));
    }
    this.trainingTarget = ModelFactory.createDefaultModel().read(trainingTargetPath);
    this.evaluationTarget = ModelFactory.createDefaultModel().read(evaluationTargetPath);
    this.resultWriter = getWriterFor(resultTargetPath);
  }

  private ParameterizedDeerExecutionNode getReaderFor(Model m, String path) {
    ParameterizedDeerExecutionNode reader = new FileModelReader();
    reader.initPluginId(DEERA.forExecutionNode(reader));
    reader.initDegrees(0, 1);
    reader.initParameters(
      reader.createParameterMap()
        .add(FileModelReader.FROM_PATH, ResourceFactory.createStringLiteral(path))
        .init()
    );
    return new NoOpModelReaderDecorator(reader, List.of(m));
  }

  private ParameterizedDeerExecutionNode getWriterFor(String path) {
    ParameterizedDeerExecutionNode writer = new FileModelWriter();
    writer.initPluginId(DEERA.forExecutionNode(writer));
    writer.initDegrees(1, 0);
    writer.initParameters(
      writer.createParameterMap()
        .add(FileModelWriter.OUTPUT_FILE, ResourceFactory.createStringLiteral(path))
        .init()
    );
    return new NoOpModelWriterDecorator(writer);
  }

  public List<Model> getTrainingSources() {
    return trainingSources;
  }

  public List<String> getTrainingSourcePaths() {
    return trainingSourcePaths;
  }

  public List<String> getEvaluationSourcePaths() {
    return evaluationSourcePaths;
  }

  public Model getTrainingTarget() {
    return trainingTarget;
  }

  public Model getEvaluationTarget() {
    return evaluationTarget;
  }

  public String getTrainingTargetPath() {
    return trainingTargetPath;
  }

  public String getEvaluationTargetPath() {
    return evaluationTargetPath;
  }

  public String getResultTargetPath() {
    return resultTargetPath;
  }

  public ParameterizedDeerExecutionNode getResultWriter(Consumer<Model> callback) {
    return resultWriter;
  }

  public List<Model> getEvaluationSources() {
    return evaluationSources;
  }

  public List<ParameterizedDeerExecutionNode> getTrainingReaders() {
    return trainingReaders;
  }

  public List<ParameterizedDeerExecutionNode> getEvaluationReaders() {
    return evaluationReaders;
  }

  public FitnessFunction getFitnessFunction() {
    return fitnessFunciton;
  }
}