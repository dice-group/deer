/*
 * DEER Command Line Interface - DEER - RDF Dataset Enrichment Framework
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
package org.aksw.deer;

import org.aksw.deer.enrichments.EnrichmentOperator;
import org.aksw.deer.io.ModelReader;
import org.aksw.deer.io.ModelWriter;
import org.aksw.deer.server.Server;
import org.aksw.deer.vocabulary.DEER;
import org.aksw.faraday_cage.engine.CompiledExecutionGraph;
import org.aksw.faraday_cage.engine.FaradayCageContext;
import org.aksw.faraday_cage.engine.Plugin;
import org.aksw.faraday_cage.engine.PluginFactory;
import org.aksw.faraday_cage.vocabulary.FCAGE;
import org.apache.commons.cli.*;
import org.apache.jena.atlas.web.HttpException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDFS;
import org.json.JSONObject;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.topbraid.shacl.validation.sparql.AbstractSPARQLExecutor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Main class for DEER.
 */
public class DeerController {

  static {
    MDC.put("requestId","main");
  }

  private static final Logger logger = LoggerFactory.getLogger(DeerController.class);

  private static final String HELP_MSG = "deer [OPTION]... <config_file_or_uri>";

  private static final Integer DEFAULT_PORT = 8080;

  private static final Options OPTIONS = new Options()
    .addOption(Option.builder("h")
      .longOpt("help").desc("show help message").build())
    .addOption(Option.builder("l")
      .longOpt("list").desc("list available deer plugins").build())
    .addOption(Option.builder("E")
      .longOpt("explain").desc("enable detailed explanation of graph validation").build())
    .addOption(Option.builder("v")
      .longOpt("validation-graph").desc("if $plugin_id is provided, get SHACL validation graph for $plugin_id, else get the complete validation graph.")
      .hasArg().argName("plugin_id").optionalArg(true).build())
    .addOption(Option.builder("s")
      .longOpt("server").desc("launch server").build())
    .addOption(Option.builder("p")
      .longOpt("port").desc("set port for server to listen on")
      .hasArg().argName("port_number").type(Number.class).build())
    ;

  private static final PluginManager pluginManager = new DefaultPluginManager();

  static {
    File plugins = new File("./plugins/");
    if (plugins.exists() && plugins.isDirectory()) {
      pluginManager.loadPlugins();
      pluginManager.startPlugins();
    }
  }

  private static final FaradayCageContext executionContext = Deer.getExecutionContext(pluginManager);

  private static <U extends Plugin> Model getClassHierarchy(Class<U> clazz, Resource type) {
    PluginFactory<U> uPluginFactory = new PluginFactory<>(clazz, DeerController.pluginManager, type);
    List<Resource> list = uPluginFactory.listAvailable();
    for (Resource r : list) {
      DeerPlugin instance = (DeerPlugin) uPluginFactory.getImplementationOf(r);
      r.getModel().add(r, RDFS.comment, instance.getDescription());
      r.getModel().add(r, RDFS.seeAlso, ResourceFactory.createResource(instance.getDocumentationURL()));
    }
    Model res = (list.size() == 0) ? ModelFactory.createDefaultModel() : list.get(0).getModel();
    res.add(type, RDFS.subClassOf, FCAGE.ExecutionNode);
    return res;
  }

  public static Model getShapes() {
    return executionContext.getFullValidationModel()
      .add(getClassHierarchy(EnrichmentOperator.class, DEER.EnrichmentOperator))
      .add(getClassHierarchy(ModelReader.class, DEER.ModelReader))
      .add(getClassHierarchy(ModelWriter.class, DEER.ModelWriter))
      .add(getClassHierarchy(DeerExecutionNodeWrapper.class, DEER.DeerExecutionNodeWrapper));
  }

  public static void main(String args[]) {
    CommandLine cl = parseCommandLine(args);
    if (cl.hasOption('h')) {
      printHelp();
    } else if (cl.hasOption('l')) {
      PrintStream out = System.out;
      out.println(EnrichmentOperator.class.getSimpleName() + ":");
      new PluginFactory<>(EnrichmentOperator.class, pluginManager, FCAGE.ExecutionNode)
        .listAvailable().forEach(out::println);
      out.println(ModelReader.class.getSimpleName() + ":");
      new PluginFactory<>(ModelReader.class, pluginManager, FCAGE.ExecutionNode)
        .listAvailable().forEach(out::println);
      out.println(ModelWriter.class.getSimpleName() + ":");
      new PluginFactory<>(ModelWriter.class, pluginManager, FCAGE.ExecutionNode)
        .listAvailable().forEach(out::println);
      out.println(DeerExecutionNodeWrapper.class.getSimpleName() + ":");
      new PluginFactory<>(DeerExecutionNodeWrapper.class, pluginManager, FCAGE.ExecutionNode)
        .listAvailable().forEach(out::println);
    }  else if (cl.hasOption('v')) {
      PrintStream out = System.out;
      String id = cl.getOptionValue('v',"");
      if (!id.isEmpty()) {
        if (id.startsWith("deer:")) {
          id = DEER.NS + id.substring(5);
        }
        executionContext.getValidationModelFor(ResourceFactory.createResource(id)).write(out, "TTL");
      } else {
        executionContext.getFullValidationModel().write(out, "TTL");
      }
    } else if (cl.hasOption('s')) {
      Object port = cl.hasOption('p') ? cl.getOptionObject('p') : DEFAULT_PORT;
      if (port == null) {
        exitWithError("Expected a number as argument for option: p");
      } else {
        runDeerServer(((Number) port).intValue());
      }
    } else if (cl.getArgList().size() == 0){
      exitWithError("Please specify a configuration file to use!");
    } else {
      if (cl.hasOption('E')) {
        AbstractSPARQLExecutor.createDetails = true;
      }
      runDeer(compileDeer(cl.getArgList().get(0)));
    }
  }

  private static CommandLine parseCommandLine(String[] args) {
    CommandLineParser parser = new DefaultParser();
    CommandLine cl = null;
    try {
      cl = parser.parse(OPTIONS, args, false);
    } catch (ParseException e) {
      exitWithError(e.getMessage());
    }
    return cl;
  }

  private static void exitWithError(String errorMsg) {
    System.out.println(ansi().fg(RED).a("Error:\n\t" + errorMsg).reset());
    printHelp();
    System.exit(1);
  }

  private static void printHelp() {
    new HelpFormatter().printHelp(HELP_MSG, OPTIONS);
  }

  private static void runDeerServer(int port) {
    logger.info("Trying to start DEER server on 0.0.0.0:{} ...", port);
    Server.getInstance().run(port);
  }

  public static CompiledExecutionGraph compileDeer(String fileName, String runId) {
    logger.info("Trying to read DEER configuration from file {}...", fileName);
    try {
      Model configurationModel = ModelFactory.createDefaultModel();
      final long startTime = System.currentTimeMillis();
      RDFDataMgr.read(configurationModel, fileName, Lang.TTL);
      logger.info("Loading {} is done in {}ms.", fileName, (System.currentTimeMillis() - startTime));
      return executionContext.compile(configurationModel, runId);
    } catch (HttpException e) {
      throw new RuntimeException("Encountered HTTPException trying to load model from " + fileName, e);
    }
  }

  public static CompiledExecutionGraph compileDeer(String fileName) {
    logger.info("Trying to read DEER configuration from file {}...", fileName);
    try {
      Model configurationModel = ModelFactory.createDefaultModel();
      final long startTime = System.currentTimeMillis();
      RDFDataMgr.read(configurationModel, fileName, Lang.TTL);
      logger.info("Loading {} is done in {}ms.", fileName, (System.currentTimeMillis() - startTime));
      return executionContext.compile(configurationModel);
    } catch (HttpException e) {
      throw new RuntimeException("Encountered HTTPException trying to load model from " + fileName, e);
    }
  }

  private static void runDeer(CompiledExecutionGraph compiledExecutionGraph) {
    compiledExecutionGraph.andThen(() -> writeAnalytics(Paths.get("deer-analytics.json").toAbsolutePath()));
    executionContext.run(compiledExecutionGraph);
  }

  public static void writeAnalytics(Path analyticsFile) {
    try {
      logger.info("Trying to write analytics data to " + analyticsFile);
      BufferedWriter writer = Files.newBufferedWriter(analyticsFile, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
      JSONObject analyticsForJob = DeerAnalyticsStore.getAnalyticsForJob(FaradayCageContext.getRunId());
      analyticsForJob.write(writer, 2, 0);
      writer.flush();
      writer.close();
    } catch (IOException e) {
      logger.error("Error! Could not write analytics file!");
      e.printStackTrace();
    }
  }

}
