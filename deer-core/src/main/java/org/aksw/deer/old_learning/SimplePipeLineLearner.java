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
/**
 *
 */
package org.aksw.deer.old_learning;


/**
 * @author sherif
 */
public class SimplePipeLineLearner implements PipelineLearner {
//
//  private static final Logger logger = LoggerFactory.getLogger(SimplePipeLineLearner.class);
//  public static Model sourceModel = ModelFactory.createDefaultModel();
//  public static Model targetModel = ModelFactory.createDefaultModel();
//  public final double CHILDREN_PENALTY_WEIGHT = 1;
//  public final double COMPLEXITY_PENALTY_WEIGHT = 1;
//  private final double MAX_FITNESS_THRESHOLD = 1;
//  private final long MAX_TREE_SIZE = 50;
//  public double penaltyWeight = 0.5;// [0, 1]
//  public Tree<RefinementNodeOld> refinementTreeRoot = new Tree<RefinementNodeOld>(
//    new RefinementNodeOld());
//  public int iterationNr = 0;
//  private int datasetIndex = 1;
//  private ConfigBuilder configBuilder = new ConfigBuilder();
//  private ModelReader modelReader = new ModelReader();
//
//
//  /**
//   * Contractors
//   *
//   * @author sherif
//   */
//  public SimplePipeLineLearner() {
//    sourceModel = ModelFactory.createDefaultModel();
//    targetModel = ModelFactory.createDefaultModel();
//  }
//
//  public SimplePipeLineLearner(Model source, Model target) {
//    sourceModel = source;
//    targetModel = target;
//  }
//
//  public SimplePipeLineLearner(Model source, Model target, double penaltyWeight) {
//    this(source, target);
//    this.penaltyWeight = penaltyWeight;
//  }
//
//  public static void main(String args[]) throws IOException {
////		trivialRun(args);
//    new SimplePipeLineLearner().evaluation(args, false, 1);
//  }
//
//  public RefinementNodeOld run() {
//    refinementTreeRoot = createRefinementTreeRoot();
//    refinementTreeRoot = expandNode(refinementTreeRoot);
//    Tree<RefinementNodeOld> mostPromisingNode = getMostPromisingNode(refinementTreeRoot,
//      penaltyWeight);
//    refinementTreeRoot.print();
//    logger.info("Most promising node: " + mostPromisingNode.getValue());
//    iterationNr++;
//    while ((mostPromisingNode.getValue().fitness) < MAX_FITNESS_THRESHOLD
//      && refinementTreeRoot.size() <= MAX_TREE_SIZE) {
//      iterationNr++;
//      mostPromisingNode = expandNode(mostPromisingNode);
//      mostPromisingNode = getMostPromisingNode(refinementTreeRoot, penaltyWeight);
//      refinementTreeRoot.print();
//      if (mostPromisingNode.getValue().fitness == -Double.MAX_VALUE) {
//        // no better solution can be found
//        break;
//      }
//      logger.info("Most promising node: " + mostPromisingNode.getValue());
//    }
//    logger.info("----------------------------------------------");
//    RefinementNodeOld bestSolution = getMostPromisingNode(refinementTreeRoot, 0).getValue();
////		logger.info("Best Solution: " + bestSolution.toString());
////		System.out.println("===== Output Config =====");
////		bestSolution.configModel.write(System.out,"TTL");
////		System.out.println("===== Output Dataset =====");
////		bestSolution.outputModel.write(System.out,"TTL");
////		System.out.println("===== Output Config =====");
////		mostPromisingNode.getValue().configModel.write(System.out,"TTL");
////		System.out.println("===== Output Dataset =====");
////		mostPromisingNode.getValue().outputModel.write(System.out,"TTL");
//    bestSolution.configModel = setIOFiles(bestSolution.configModel, "inputFile.ttl",
//      "outputFile.ttl");
//    return bestSolution;
//  }
//
//  private Tree<RefinementNodeOld> createRefinementTreeRoot() {
//    Resource outputDataset = ResourceFactory
//      .createResource(SPECS.NS + "Dataset_" + datasetIndex++);
//    Model config = ModelFactory.createDefaultModel();
//    double f = -Double.MAX_VALUE;
//    RefinementNodeOld initialNode = new RefinementNodeOld(null, f, sourceModel, sourceModel,
//      outputDataset, outputDataset, config);
//    return new Tree<RefinementNodeOld>(null, initialNode, null);
//  }
//
//  private Tree<RefinementNodeOld> expandNode(Tree<RefinementNodeOld> root) {
//    for (IEnrichmentOperator module : MODULES) {
//      Model inputModel = root.getValue().outputModel;
//      Map<String, String> parameters = module.learnParameterMap(inputModel, targetModel);
//      Resource inputDataset = root.getValue().outputDataset;
//      Model configMdl = ModelFactory.createDefaultModel();
//      RefinementNodeOld node = new RefinementNodeOld();
//      logger.info(module.getClass().getSimpleName() + "' self-config parameter(s):" + parameters);
//      if (parameters == null || parameters.size() == 0) {
//        // mark as dead end, fitness = -2
//        configMdl = root.getValue().configModel;
//        node = new RefinementNodeOld(module, -2, sourceModel, sourceModel, inputDataset,
//          inputDataset, configMdl);
//      } else {
//        module.initDegrees(parameters);
//        Model currentMdl = module.apply(inputModel);
//        double fitness;
//        if (currentMdl == null || currentMdl.size() == 0 || currentMdl
//          .isIsomorphicWith(inputModel)) {
//          fitness = -2;
//        } else {
////					fitness = computeFitness(currentMdl, targetModel);
//          fitness = computeFMeasure(currentMdl, targetModel);
//        }
//        Resource outputDataset = ResourceFactory
//          .createResource(SPECS.NS + "Dataset_" + datasetIndex++);
//        configMdl = configBuilder
//          .addModule(module, parameters, root.getValue().configModel, inputDataset, outputDataset);
//        node = new RefinementNodeOld(module, fitness, root.getValue().outputModel, currentMdl,
//          inputDataset, outputDataset, configMdl);
//      }
//      root.addChild(new Tree<RefinementNodeOld>(node));
//    }
//    return root;
//  }
//
//  /**
//   * Compute the fitness of the generated model by current specs
//   * Simple implementation is difference between current and target
//   *
//   * @author sherif
//   */
//  public double computeFitness(Model currentModel, Model targetModel) {
//    long t_c = targetModel.difference(currentModel).size();
//    long c_t = currentModel.difference(targetModel).size();
//    System.out.println("targetModel.difference(currentModel).size() = " + t_c);
//    System.out.println("currentModel.difference(targetModel).size() = " + c_t);
//    return 1 - ((double) (t_c + c_t) / (double) (currentModel.size() + targetModel.size()));
//  }
//
//  public double computeFMeasure(Model currentModel, Model targetModel) {
//    double p = computePrecision(currentModel, targetModel);
//    double r = computeRecall(currentModel, targetModel);
//    if (p == 0 && r == 0) {
//      return 0;
//    }
//    return 2 * p * r / (p + r);
//
//  }
//
//  public double computePrecision(Model currentModel, Model targetModel) {
//    return (double) currentModel.intersection(targetModel).size() / (double) currentModel.size();
//  }
//
//  public double computeRecall(Model currentModel, Model targetModel) {
//    return (double) currentModel.intersection(targetModel).size() / (double) targetModel.size();
//  }
//
//  private Tree<RefinementNodeOld> getMostPromisingNode(Tree<RefinementNodeOld> root,
//    double penaltyWeight) {
//    // trivial case
//    if (root.getchildren() == null || root.getchildren().size() == 0) {
//      return root;
//    }
//    // get mostPromesyChild of children
//    Tree<RefinementNodeOld> mostPromesyChild = new Tree<RefinementNodeOld>(new RefinementNodeOld());
//    for (Tree<RefinementNodeOld> child : root.getchildren()) {
//      if (child.getValue().fitness >= 0) {
//        Tree<RefinementNodeOld> promesyChild = getMostPromisingNode(child, penaltyWeight);
//        double newFitness;
//        newFitness =
//          promesyChild.getValue().fitness - penaltyWeight * computePenality(promesyChild);
//        if (newFitness > mostPromesyChild.getValue().fitness) {
//          mostPromesyChild = promesyChild;
//        }
//      }
//    }
//    // return the argmax{root, mostPromesyChild}
//    if (penaltyWeight > 0) {
//      return mostPromesyChild;
//    } else if (root.getValue().fitness >= mostPromesyChild.getValue().fitness) {
//      return root;
//    } else {
//      return mostPromesyChild;
//    }
//  }
//
//  /**
//   * @author sherif
//   */
//  private double computePenality(Tree<RefinementNodeOld> promesyChild) {
//    long childrenCount = promesyChild.size() - 1;
//    double childrenPenalty = (CHILDREN_PENALTY_WEIGHT * childrenCount) / refinementTreeRoot.size();
//    long level = promesyChild.level();
//    double complextyPenalty = (COMPLEXITY_PENALTY_WEIGHT * level) / refinementTreeRoot.depth();
//    return childrenPenalty + complextyPenalty;
//  }
//
//  public void trivialRun(String args[]) {
//    String sourceUri = args[0];
//    String targetUri = args[1];
//    SimplePipeLineLearner learner = new SimplePipeLineLearner();
//    SimplePipeLineLearner.sourceModel = modelReader.readModel(sourceUri);
//    SimplePipeLineLearner.targetModel = modelReader.readModel(targetUri);
//    long start = System.currentTimeMillis();
//    learner.run();
//    long end = System.currentTimeMillis();
//    logger.info("Done in " + (end - start) + "ms");
//  }
//
//  public void evaluation(String args[], boolean isBatch, int max) throws IOException {
//    String folder = args[0];
//    String results = "ModuleCount\tTime\tTreeSize\tIterationNr\tP\tR\tF\n";
//    for (int i = 1; i <= max; i++) {
//      SimplePipeLineLearner learner = new SimplePipeLineLearner();
//      if (isBatch) {
//        folder = folder + i;
//      }
//      SimplePipeLineLearner.sourceModel = modelReader.readModel(folder + "/input.ttl");
//      SimplePipeLineLearner.targetModel = modelReader.readModel(folder + "/output.ttl");
//      long start = System.currentTimeMillis();
//      RefinementNodeOld bestSolution = learner.run();
//      long end = System.currentTimeMillis();
//      long time = end - start;
//      results += i + "\t" + time + "\t" +
//        learner.refinementTreeRoot.size() + "\t" +
//        learner.iterationNr + "\t" +
////					bestSolution.fitness + "\t" +
//        learner.computePrecision(bestSolution.outputModel, targetModel) + "\t" +
//        learner.computeRecall(bestSolution.outputModel, targetModel) + "\t" +
//        learner.computeFMeasure
//          (bestSolution.outputModel, targetModel);
//      (new ModelWriter()).writeModel(bestSolution.configModel, "TTL", folder + "/self_config.ttl");
////			bestSolution.outputModel.write(System.out,"TTL");
//      System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//      System.out.println(results);
////			break;
//    }
//    System.out.println(results);
//  }
//
//
//  Model setIOFiles(final Model sConfig, String inputFile, String outputFile) {
//    Model resultModel = ModelFactory.createDefaultModel();
//    resultModel = resultModel.union(sConfig);
//    List<String> datasets = new ArrayList<String>();
//    String sparqlQueryString =
//      "SELECT DISTINCT ?d {?d <" + RDF.type + "> <" + SPECS.Dataset + ">.} ";
//    QueryFactory.create(sparqlQueryString);
//    QueryExecution qexec = QueryExecutionFactory.create(sparqlQueryString, resultModel);
//    ResultSet queryResults = qexec.execSelect();
//    while (queryResults.hasNext()) {
//      QuerySolution qs = queryResults.nextSolution();
//      Resource dataset = qs.getResource("?d");
//      datasets.add(dataset.toString());
//    }
//    qexec.close();
//    Collections.sort(datasets);
//    Resource inputDataset = ResourceFactory.createResource(datasets.get(0));
//    Resource outputDataset = ResourceFactory.createResource(datasets.get(datasets.size() - 1));
//    resultModel.add(inputDataset, SPECS.inputFile, inputFile);
//    resultModel.add(outputDataset, SPECS.outputFile, outputFile);
//    resultModel.setNsPrefixes(sConfig);
//    return resultModel;
//  }

}
