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

import org.aksw.deer.vocabulary.DEER;
import org.aksw.faraday_cage.engine.ExecutionNode;
import org.aksw.faraday_cage.engine.ValidatableParameterMap;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 */
@Extension
public class FileModelWriter extends AbstractModelWriter {

  private static final Logger logger = LoggerFactory.getLogger(FileModelWriter.class);

  public static final Property OUTPUT_FILE = DEER.property("outputFile");
  public static final Property OUTPUT_FORMAT = DEER.property("outputFormat");

  @Override
  public String getDescription() {
    return "Serialize input to a file.";
  }

  @Override
  public ValidatableParameterMap createParameterMap() {
    return ValidatableParameterMap.builder()
      .declareProperty(OUTPUT_FILE)
      .declareProperty(OUTPUT_FORMAT)
      .declareValidationShape(getValidationModelFor(FileModelWriter.class))
      .build();
  }

  @Override
  protected List<Model> safeApply(List<Model> data) {
    return ExecutionNode.toMultiExecution(this::write).apply(data);
  }


  private Model write(Model model) {
    final String outputFile = injectWorkingDirectory(
      getParameterMap().get(OUTPUT_FILE).asLiteral().getString()
    );
    final String outputFormat = getParameterMap().getOptional(OUTPUT_FORMAT)
      .map(RDFNode::asLiteral).map(Literal::getString).orElse("TTL");
    try {
      logger.info("Saving dataset to " + outputFile + "...");
      final long starTime = System.currentTimeMillis();
      File writingDir = new File(outputFile).getParentFile();
      if (writingDir != null && !writingDir.exists()) {
        writingDir.mkdirs();
      }
      model.write(new FileWriter(outputFile), outputFormat);
      logger.info("Saving dataset done in " + (System.currentTimeMillis() - starTime) + "ms.");
    } catch (IOException e) {
      throw new RuntimeException("Encountered problem while trying to write dataset to " +
        outputFile, e);
    }
    return model;
  }

}
