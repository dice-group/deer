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
package org.aksw.deer.io;

import org.aksw.deer.ParameterizedDeerExecutionNode;
import org.aksw.deer.vocabulary.DEER;
import org.aksw.faraday_cage.engine.AbstractParameterizedExecutionNode;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

/**
 *
 *
 *
 */
public abstract class AbstractModelIO extends AbstractParameterizedExecutionNode<Model> implements ParameterizedDeerExecutionNode {

  protected String description = "Description not available";
  protected String documentationURL = "https://dice-group.github.io/deer/configuring_deer/readers.html";

  public String getDescription() {
    return description;
  }

  @Override
  public String getDocumentationURL() {
    return documentationURL;
  }

  private static Supplier<String> workingDirectorySupplier = () -> "";


  /**
   * Specify the supplier of the working directory injection.
   * It <b>must</b> have the necessary information to construct a valid
   * working directory path at ExecutionGraph compileCanonicalForm time.
   * @param supplier the supplier
   */
  public static void takeWorkingDirectoryFrom(Supplier<String> supplier) {
    if (supplier != null) {
      workingDirectorySupplier = supplier;
    }
  }

  /**
   * Use this always when writing to or reading from Files whose paths are specified by parameters.
   * @param pathString Path to inject working directory into
   * @return injected path
   */
  public static String injectWorkingDirectory(String pathString) {
      Path path = Paths.get(pathString);
      Path currentDir = Paths.get(".");
      if (workingDirectorySupplier != null && path.isAbsolute()) {
        path = path.getFileName();
      }
      if (workingDirectorySupplier != null) {
        path = currentDir.resolve(workingDirectorySupplier.get()).resolve(path).normalize();
      }
    return path.toString();
  }

  @Override
  public Model deepCopy(Model data) {
    return ModelFactory.createDefaultModel().add(data);
  }

  @Override
  public Resource getType() {
    return DEER.resource(this.getClass().getSimpleName());
  }

//  @Override
//  protected void writeInputAnalytics(List<Model> data) {
//    if (getInDegree() > 0) {
//      writeAnalytics("input sizes", data.stream().map(m -> String.valueOf(m.size())).reduce("( ", (a, b) -> a + b + " ") + ")");
//    }
//  }
//
//  @Override
//  protected void writeOutputAnalytics(List<Model> data) {
//    if (getOutDegree() > 0) {
//      writeAnalytics("output sizes", data.stream().map(m->String.valueOf(m.size())).reduce("( ", (a, b) -> a + b + " ") + ")");
//    }
//  }

}
